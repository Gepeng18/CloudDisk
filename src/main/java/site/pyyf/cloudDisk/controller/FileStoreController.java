package site.pyyf.cloudDisk.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import site.pyyf.cloudDisk.config.AliyunConfig;
import site.pyyf.cloudDisk.entity.FileFolder;
import site.pyyf.cloudDisk.entity.MyFile;
import site.pyyf.cloudDisk.entity.UploadResult;
import site.pyyf.cloudDisk.entity.User;
import site.pyyf.cloudDisk.service.IResolveHeaderService;
import site.pyyf.cloudDisk.utils.FtpUtil;
import site.pyyf.cloudDisk.utils.LogUtils;

import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: UserController
 * @Description: 文件仓库控制器
 * @author: xw
 * @date 2020/2/6 16:04
 * @Version: 1.0
 **/
@Controller
@Scope("prototype")
public class FileStoreController extends BaseController {

    private Logger logger = LogUtils.getInstance(FileStoreController.class);


    @Autowired
    protected IResolveHeaderService iResolveHeaderService;

    /**
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Description 网盘的文件上传
     * @Author xw
     * @Date 23:10 2020/2/10
     * @Param [file]
     **/
    @PostMapping("/uploadFile")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile originalFile) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        List<String> htmlSupAudio = new ArrayList<>(Arrays.asList("mp3", "flac", "wav"));
        List<String> htmlSupVideo = new ArrayList<>(Arrays.asList("mp4"));
        Integer folderId = Integer.valueOf(request.getHeader("id"));

        //获取当前目录下的所有文件，用来判断是否已经存在
        List<MyFile> myFiles = null;
        if (folderId == 0) {
            //当前目录为根目录
            myFiles = iMyFileService.getRootFilesByUserId(loginUser.getUserId());
        } else {
            //当前目录为其他目录
            myFiles = iMyFileService.getFilesByParentFolderId(folderId);
        }

        String name = originalFile.getOriginalFilename().replaceAll(" ", "");
        for (int i = 0; i < myFiles.size(); i++) {
            if (myFiles.get(i).getMyFileName().equals(name)) {
                logger.error("当前文件已存在!上传失败...");
                resultMap.put("code", 501);
                return resultMap;
            }
        }

        if (!checkTarget(name)) {
            logger.error("上传失败!文件名不符合规范...");
            resultMap.put("code", 502);
            return resultMap;
        }

        User user = iUserService.getUserById(loginUser.getUserId());
        long size = originalFile.getSize();
        String insertSize = StringUtils.substringBeforeLast(String.valueOf(size / 1024.0), ".");
        Integer sizeInt = Math.toIntExact(originalFile.getSize() / 1024);
        //是否仓库放不下该文件
        if (user.getCurrentSize() + sizeInt > user.getMaxSize()) {
            logger.error("上传失败!仓库已满。");
            resultMap.put("code", 503);
            return resultMap;
        }

        String insertPostfix = StringUtils.substringAfterLast(name, ".").toLowerCase();
        int insertType = iFileStoreService.getType(insertPostfix);

        MyFile fileItem = MyFile.builder()
                .myFileName(name).userId(user.getUserId())
                .downloadTime(0).uploadTime(new Date()).parentFolderId(folderId).
                        size(Integer.valueOf(insertSize)).type(insertType).postfix(insertPostfix).build();


        if ((insertType == 4) && (size < cloudDiskConfig.getMaxShowSize() * 1024 * 1024) && (!htmlSupAudio.contains(insertPostfix))) {
            //音乐文件，小于MaxShowSize MB，且不是htmlSupAudio则转码后存储
            iFileStoreService.uploadTAudioFile(originalFile, fileItem);
        } else if ((insertType == 3) && (size < cloudDiskConfig.getMaxShowSize() * 1024 * 1024) && (!htmlSupVideo.contains(insertPostfix))) {
            iFileStoreService.uploadTVideoFile(originalFile, fileItem);
        } else if (htmlSupAudio.contains(insertPostfix) || htmlSupVideo.contains(insertPostfix)) {
            iFileStoreService.uploadAudioAndVideoFile(originalFile, fileItem);
        } else if ((insertType == 2)) {
           iFileStoreService.uploadImgFile(originalFile, fileItem);
        } else {
           iFileStoreService.uploadOtherFile(originalFile, fileItem);
        }

        if (fileItem == null) {
            logger.error("当前文件上传失败...");
            resultMap.put("code", 504);
            return resultMap;
        }

        //向数据库文件表写入数据
        iMyFileService.addFileByUserId(fileItem);
        //更新仓库表的当前大小
        iUserService.addSize(user.getUserId(), Integer.valueOf(fileItem.getSize()));

        //如果是markdown，则再传一份到library表中
        if (fileItem.getPostfix().equals("md"))
            iResolveHeaderService.readFile(originalFile.getInputStream(), originalFile.getOriginalFilename(), fileItem.getMyFileId());

        resultMap.put("code", 200);
        return resultMap;

    }


    /**
     * @return void
     * @Description 网盘的文件下载
     * @Author xw
     * @Date 23:13 2020/2/10
     * @Param [fId]
     **/
    @GetMapping("/downloadFile")
    public void downloadFile(@RequestParam Integer fId) {
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取reponse的输出流失败");
            return;
        }
        //获取文件信息
        MyFile myFile = iMyFileService.getFileByFileId(fId);
        String remotePath = myFile.getMyFilePath();
        String fileName = myFile.getMyFileName();
        response.setCharacterEncoding("utf-8");
        // 设置返回类型
        response.setContentType("multipart/form-data");
        // 文件名转码一下，不然会出现中文乱码
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("文件名编码失败");
            return;
        }
//        规定是OSS或者是图片则从OSS中下载，因为图片始终存放在OSS中
        if ((cloudDiskConfig.getType().equals("OSS")) || myFile.getType() == 2) {
            try {
                logger.info("开始下载");
                iossService.download(remotePath.substring(aliyunConfig.getUrlPrefix().length()), os);
                logger.info("文件从OSS下载成功");
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("文件从OSS下载失败");
                return;
            }
        } else {
            //去FTP上拉取
            logger.info("开始下载");
            boolean FTPdownLoadRes = FtpUtil.downloadFile("/" + remotePath, os);
            if (FTPdownLoadRes)
                logger.info("文件从FTP下载成功");
            else {
                logger.info("文件从FTP下载失败!" + myFile);
                return;
            }
        }

        iMyFileService.updateFile(
                MyFile.builder().myFileId(myFile.getMyFileId()).downloadTime(myFile.getDownloadTime() + 1).build());
        try {
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("reponse输出流关闭错误");
        }
    }

    /**
     * @return java.lang.String
     * @Description 删除文件
     * @Author xw
     * @Date 23:14 2020/2/10
     * @Param [fId, folder]
     **/
    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam Integer fId, Integer folder) {
        //获得文件信息
        MyFile myFile = iMyFileService.getFileByFileId(fId);

        iFileStoreService.deleteFile(myFile);

        if (StringUtils.substringAfterLast(myFile.getMyFileName(), ".").equals("md")) {
            iLibraryService.deleteByBookId(fId);
            iEbookContentService.deleteByBookId(fId);
        }

        //删除成功,返回空间
        iUserService.subSize(myFile.getUserId(), Integer.valueOf(myFile.getSize()));
        //删除文件表对应的数据
        iMyFileService.deleteByFileId(fId);

        return "redirect:/files?fId=" + folder;
    }

    /**
     * @return java.lang.String
     * @Description 删除文件夹并清空文件
     * @Author xw
     * @Date 15:22 2020/2/12
     * @Param [fId]
     **/
    @GetMapping("/deleteFolder")
    public String deleteFolder(@RequestParam Integer fId) {
        FileFolder folder = iFileFolderService.getFileFolderByFileFolderId(fId);
        //强制删除
        deleteFolderF(folder);
        return folder.getParentFolderId() == 0 ? "redirect:/files" : "redirect:/files?fId=" + folder.getParentFolderId();
    }

    /**
     * @return void
     * @Description 迭代删除文件夹里面的所有文件和子文件夹
     * @Author xw
     * @Date 9:17 2020/2/29
     * @Param [folder]
     **/
    public void deleteFolderF(FileFolder folder) {
        //获得当前文件夹下的所有子文件夹
        List<FileFolder> folders = iFileFolderService.getFileFolderByParentFolderId(folder.getFileFolderId());
        //删除当前文件夹的所有的文件
        List<MyFile> files = iMyFileService.getFilesByParentFolderId(folder.getFileFolderId());
        if (files.size() != 0) {
            for (int i = 0; i < files.size(); i++) {
                MyFile thisFile = files.get(i);
                iFileStoreService.deleteFile(thisFile);

                if (StringUtils.substringAfterLast(thisFile.getMyFileName(), ".").equals("md")) {
                    iLibraryService.deleteByBookId(thisFile.getMyFileId());
                    iEbookContentService.deleteByBookId(thisFile.getMyFileId());
                }

                //删除成功,返回空间
                iUserService.subSize(thisFile.getUserId(), Integer.valueOf(thisFile.getSize()));
                //删除文件表对应的数据
                iMyFileService.deleteByFileId(thisFile.getMyFileId());
            }
        }
        if (folders.size() != 0) {
            for (int i = 0; i < folders.size(); i++) {
                deleteFolderF(folders.get(i));
            }
        }
        iFileFolderService.deleteFileFolderById(folder.getFileFolderId());
    }

    /**
     * @return java.lang.String
     * @Description 添加文件夹
     * @Author xw
     * @Date 23:16 2020/2/10
     * @Param [folder, map]
     **/
    @PostMapping("/addFolder")
    public String addFolder(FileFolder folder, Map<String, Object> map) {
        //设置文件夹信息
        folder.setUserId(loginUser.getUserId());
        folder.setTime(new Date());
        //获得当前目录下的所有文件夹,检查当前文件夹是否已经存在
        List<FileFolder> fileFolders = null;
        if (folder.getParentFolderId() == 0) {
            //向用户根目录添加文件夹
            fileFolders = iFileFolderService.getRootFoldersByUserId(loginUser.getUserId());
        } else {
            //向用户的其他目录添加文件夹
            fileFolders = iFileFolderService.getFileFolderByParentFolderId(folder.getParentFolderId());
        }
        for (int i = 0; i < fileFolders.size(); i++) {
            FileFolder fileFolder = fileFolders.get(i);
            if (fileFolder.getFileFolderName().equals(folder.getFileFolderName())) {
                logger.info("添加文件夹失败!文件夹已存在...");
                return "redirect:/files?error=1&fId=" + folder.getParentFolderId();
            }
        }
        //向数据库写入数据
        Integer integer = iFileFolderService.addFileFolder(folder);
        logger.info("添加文件夹成功!" + folder);
        return "redirect:/files?fId=" + folder.getParentFolderId();
    }

    /**
     * @return java.lang.String
     * @Description 重命名文件夹
     * @Author xw
     * @Date 23:18 2020/2/10
     * @Param [folder, map]
     **/
    @PostMapping("/updateFolder")
    public String updateFolder(FileFolder folder, Map<String, Object> map) {
        //获得文件夹的数据库信息
        FileFolder fileFolder = iFileFolderService.getFileFolderByFileFolderId(folder.getFileFolderId());
        fileFolder.setFileFolderName(folder.getFileFolderName());
        //获得当前目录下的所有文件夹,用于检查文件夹是否已经存在
        List<FileFolder> fileFolders = iFileFolderService.getFileFolderByParentFolderId(fileFolder.getParentFolderId());
        for (int i = 0; i < fileFolders.size(); i++) {
            FileFolder folder1 = fileFolders.get(i);
            if (folder1.getFileFolderName().equals(folder.getFileFolderName()) && folder1.getFileFolderId() != folder.getFileFolderId()) {
                logger.info("重命名文件夹失败!文件夹已存在...");
                return "redirect:/files?error=2&fId=" + fileFolder.getParentFolderId();
            }
        }
        //向数据库写入数据
        Integer integer = iFileFolderService.updateFileFolderById(fileFolder);
        logger.info("重命名文件夹成功!" + folder);
        return "redirect:/files?fId=" + fileFolder.getParentFolderId();
    }

    /**
     * @return java.lang.String
     * @Description 重命名文件
     * @Author xw
     * @Date 12:47 2020/2/12
     * @Param [file, map]
     **/
    @PostMapping("/updateFileName")
    public String updateFileName(MyFile file) {
        MyFile myFile = iMyFileService.getFileByFileId(file.getMyFileId());
        if (myFile != null) {
            String oldName = myFile.getMyFileName();
            String newName = file.getMyFileName();
            if (!oldName.equals(newName)) {
                Integer integer = iMyFileService.updateFile(
                        MyFile.builder().myFileId(myFile.getMyFileId()).myFileName(newName).build());
                if (integer == 1) {
                    if (StringUtils.substringAfterLast(file.getMyFileName(), ".").equals("md")) {
                        iLibraryService.updateEbookNameByBookId(myFile.getMyFileId(), newName);
                    }
                    logger.info("修改文件名成功!原文件名:" + oldName + "  新文件名:" + newName);
                } else {
                    logger.error("修改文件名失败!原文件名:" + oldName + "  新文件名:" + newName);
                }

            }
        }
        return "redirect:/files?fId=" + myFile.getParentFolderId();
    }


    /**
     * @return boolean
     * @Description 正则验证文件名是否合法 [汉字,字符,数字,下划线,英文句号,横线]
     * @Author xw
     * @Date 23:22 2020/2/10
     * @Param [target]
     **/
    public boolean checkTarget(String target) {
        String format = "[^\\u4E00-\\u9FA5\\uF900-\\uFA2D\\w-_.]";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(target);
        return !matcher.find();
    }

    /**
     * @return void
     * @Description 每天0、1、2 点清除缓存
     * @Author xw
     * @Date 21:56 2020/2/26
     * @Param []
     **/
    @Scheduled(cron = "0 0 1,2,3 * * ?")
    public void flushCache() {
        File file = new File("temp");
        if (file == null || !file.exists()) {
            return;
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return;
        } else {
            for (File file1 : files) {
                boolean delete = file1.delete();
            }
            file.delete();
        }
    }

}
