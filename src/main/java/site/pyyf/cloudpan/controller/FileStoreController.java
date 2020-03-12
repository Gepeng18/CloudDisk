package site.pyyf.cloudpan.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import site.pyyf.cloudpan.entity.FileFolder;
import site.pyyf.cloudpan.entity.FileStore;
import site.pyyf.cloudpan.entity.MyFile;
import site.pyyf.cloudpan.entity.PicUploadResult;
import site.pyyf.cloudpan.service.IfilePreviewService;
import site.pyyf.cloudpan.service.ResolveHeaderService;
import site.pyyf.cloudpan.utils.*;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Folder;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: FileStoreController
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
    protected ResolveHeaderService resolveHeaderService;

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
//        if(postfix.equals("md"))
//            resolveHeaderService.readFile(file.getInputStream(), file.getOriginalFilename());
        Map<String, Object> map = new HashMap<>();
        FileStore store = fileStoreService.getFileStoreByUserId(loginUser.getUserId());
        Integer folderId = Integer.valueOf(request.getHeader("id"));
        String name = originalFile.getOriginalFilename().replaceAll(" ", "");
        //获取当前目录下的所有文件，用来判断是否已经存在
        List<MyFile> myFiles = null;
        if (folderId == 0) {
            //当前目录为根目录
            myFiles = myFileService.getRootFilesByFileStoreId(loginUser.getFileStoreId());
        } else {
            //当前目录为其他目录
            myFiles = myFileService.getFilesByParentFolderId(folderId);
        }
        for (int i = 0; i < myFiles.size(); i++) {
            if (myFiles.get(i).getMyFileName().equals(name)) {
                logger.error("当前文件已存在!上传失败...");
                map.put("code", 501);
                return map;
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        String path = loginUser.getUserId() + "/" + dateStr + "/" + folderId;
        if (!checkTarget(name)) {
            logger.error("上传失败!文件名不符合规范...");
            map.put("code", 502);
            return map;
        }
        Integer sizeInt = Math.toIntExact(originalFile.getSize() / 1024);
        //是否仓库放不下该文件
        if (store.getCurrentSize() + sizeInt > store.getMaxSize()) {
            logger.error("上传失败!仓库已满。");
            map.put("code", 503);
            return map;
        }
        long size = originalFile.getSize();
        //处理文件大小
        String insertSize = StringUtils.substringBeforeLast(String.valueOf(size / 1024.0), ".");
        String insertPostfix = StringUtils.substringAfterLast(name, ".").toLowerCase();

        //获得文件类型
        int insertType = getType(insertPostfix);
        File srcFile = null;
        InputStream uploadStream = originalFile.getInputStream();
        boolean transferSuccess = true;
        //音乐文件，小于10MB，且不是MP3则转码后存储
        if ((insertType == 4) && (size < 10 * 1024 * 1024) && (!insertPostfix.equals("mp3"))) {
            String rootPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
            final File tmpFolder = new File(rootPath + "data/audio");
            if (!tmpFolder.exists())
                tmpFolder.mkdirs();

            srcFile = new File(tmpFolder.getAbsolutePath() + "/" + UUID.randomUUID().toString().replaceAll("-", ""));

            FileOutputStream fos = new FileOutputStream(srcFile);
            byte[] buf = new byte[1024];
            int length;
            while ((length = uploadStream.read(buf)) > 0) {
                fos.write(buf, 0, length);
            }
            fos.close();

            logger.info("音乐文件存储成功");


            File dstFile = new File(tmpFolder.getAbsolutePath() + "/" + UUID.randomUUID().toString().replaceAll("-", ""));
            boolean isSuccess = iMediaTranfer.tranferAudio(srcFile, dstFile);
            if (isSuccess) {
                FileInputStream dstStream = new FileInputStream(dstFile);
                FtpUtil.uploadFile("/" + path + "_mp", name, dstStream);
                dstStream.close();
                logger.info("转码文件上传完毕");
            } else {
                transferSuccess = false;
                logger.info("转码失败");
            }
            dstFile.delete();

        } else if ((insertType == 3) && (size < 10 * 1024 * 1024) && (!(insertPostfix.equals("mp4")))) {
            //视频文件，小于10MB，且不是MP4则转码后存储
            String rootPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
            final File tmpFolder = new File(rootPath + "data/video");
            if (!tmpFolder.exists())
                tmpFolder.mkdirs();


            srcFile = new File(tmpFolder.getAbsolutePath() + "/" + UUID.randomUUID().toString().replaceAll("-", ""));
            FileOutputStream fos = new FileOutputStream(srcFile);
            byte[] buf = new byte[1024];
            int length;
            while ((length = uploadStream.read(buf)) > 0) {
                fos.write(buf, 0, length);
            }
            fos.close();
            logger.info("视频文件存储成功");

            File dstFile = new File(tmpFolder.getAbsolutePath() + "/" + UUID.randomUUID().toString().replaceAll("-", ""));

            boolean isSuccess = iMediaTranfer.tranferVideo(srcFile, dstFile);
            if (isSuccess) {
                FileInputStream dstStream = new FileInputStream(dstFile);
                FtpUtil.uploadFile("/" + path + "_mp", name, dstStream);
                dstStream.close();
                logger.info("转码文件上传完毕");
            } else {
                transferSuccess = false;
                logger.info("转码失败");
            }

            dstFile.delete();
        }



        if (srcFile != null) {
            uploadStream = new FileInputStream(srcFile);
            srcFile.delete();
        }
        //提交到FTP服务器
        boolean b = FtpUtil.uploadFile("/" + path, name, uploadStream);
        if (b) {
            //上传成功
            logger.info("文件上传成功!" + name);
            MyFile fileItem = null;
            if (((((insertType == 4) && (size < 10 * 1024 * 1024)) || ((insertType == 3) && (size < 10 * 1024 * 1024)))) && (transferSuccess)) {
                String insertShowPath = null;
                if (insertPostfix.equals("mp3") || insertPostfix.equals("mp4"))
                    insertShowPath = path;
                else
                    insertShowPath = path + "_mp";
                fileItem = MyFile.builder()
                        .myFileName(name).fileStoreId(loginUser.getFileStoreId()).myFilePath(path)
                        .downloadTime(0).uploadTime(new Date()).parentFolderId(folderId).
                                size(Integer.valueOf(insertSize)).type(insertType).postfix(insertPostfix).showPath(insertShowPath).build();
            } else {
                fileItem = MyFile.builder()
                        .myFileName(name).fileStoreId(loginUser.getFileStoreId()).myFilePath(path)
                        .downloadTime(0).uploadTime(new Date()).parentFolderId(folderId).
                                size(Integer.valueOf(insertSize)).type(insertType).postfix(insertPostfix).build();
            }

            //向数据库文件表写入数据
            myFileService.addFileByFileStoreId(fileItem);
            //更新仓库表的当前大小
            fileStoreService.addSize(store.getFileStoreId(), Integer.valueOf(insertSize));

            //如果是markdown，则再传一份到library表中
            if (fileItem.getPostfix().equals("md"))
                resolveHeaderService.readFile(originalFile.getInputStream(), originalFile.getOriginalFilename(), fileItem.getMyFileId());
            map.put("code", 200);

        } else {
            logger.error("文件上传失败!" + name);
            map.put("code", 504);
        }
        return map;
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
        //获取文件信息
        MyFile myFile = myFileService.getFileByFileId(fId);
        String remotePath = myFile.getMyFilePath();
        String fileName = myFile.getMyFileName();
        try {
            File temp = new File("temp");
            if (!temp.exists()) {
                temp.mkdirs();
            }
            //去FTP上拉取
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            logger.info("开始下载");
            response.setCharacterEncoding("utf-8");
            // 设置返回类型
            response.setContentType("multipart/form-data");
            // 文件名转码一下，不然会出现中文乱码
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            boolean flag = FtpUtil.downloadFile("/" + remotePath, fileName, os);
            logger.info("下载完成");
            if (flag) {
                myFileService.updateFile(
                        MyFile.builder().myFileId(myFile.getMyFileId()).downloadTime(myFile.getDownloadTime() + 1).build());
                os.flush();
                os.close();
                logger.info("文件下载成功!" + myFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        MyFile myFile = myFileService.getFileByFileId(fId);
        String remotePath = myFile.getMyFilePath();
        String ShowPath = myFile.getShowPath();
        String fileName = myFile.getMyFileName();
        //从FTP文件服务器上删除文件
        boolean b = FtpUtil.deleteFile("/" + remotePath, fileName);
        if (b) {

            if (!(myFile.getShowPath().equals("") || myFile.getShowPath() == null)) {
                boolean isSuccess = true;
                if (!(myFile.getPostfix().equals("mp3") || (myFile.getPostfix().equals("mp4"))))
                    isSuccess = FtpUtil.deleteFile("/" + ShowPath, fileName);
                if (isSuccess)
                    logger.info("转存文件的播放文件删除成功");
                else
                    logger.info("转存文件的播放文件删除失败");
            }

            if (StringUtils.substringAfterLast(myFile.getMyFileName(), ".").equals("md")) {

                libraryService.deleteByBookId(fId);
                ebookContentService.deleteByBookId(fId);
            }
            //删除成功,返回空间
            fileStoreService.subSize(myFile.getFileStoreId(), Integer.valueOf(myFile.getSize()));
            //删除文件表对应的数据
            myFileService.deleteByFileId(fId);
        }
        logger.info("删除文件成功!" + myFile);
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
        FileFolder folder = fileFolderService.getFileFolderByFileFolderId(fId);
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
        List<FileFolder> folders = fileFolderService.getFileFolderByParentFolderId(folder.getFileFolderId());
        //删除当前文件夹的所有的文件
        List<MyFile> files = myFileService.getFilesByParentFolderId(folder.getFileFolderId());
        if (files.size() != 0) {
            for (int i = 0; i < files.size(); i++) {
                Integer fileId = files.get(i).getMyFileId();
                boolean b = FtpUtil.deleteFile("/" + files.get(i).getMyFilePath(), files.get(i).getMyFileName());
                if (b) {
                    if (StringUtils.substringAfterLast(files.get(i).getMyFileName(), ".").equals("md")) {

                        libraryService.deleteByBookId(fileId);
                        ebookContentService.deleteByBookId(fileId);
                    }
                    myFileService.deleteByFileId(fileId);
                    fileStoreService.subSize(folder.getFileStoreId(), Integer.valueOf(files.get(i).getSize()));
                }
            }
        }
        if (folders.size() != 0) {
            for (int i = 0; i < folders.size(); i++) {
                deleteFolderF(folders.get(i));
            }
        }
        fileFolderService.deleteFileFolderById(folder.getFileFolderId());
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
        folder.setFileStoreId(loginUser.getFileStoreId());
        folder.setTime(new Date());
        //获得当前目录下的所有文件夹,检查当前文件夹是否已经存在
        List<FileFolder> fileFolders = null;
        if (folder.getParentFolderId() == 0) {
            //向用户根目录添加文件夹
            fileFolders = fileFolderService.getRootFoldersByFileStoreId(loginUser.getFileStoreId());
        } else {
            //向用户的其他目录添加文件夹
            fileFolders = fileFolderService.getFileFolderByParentFolderId(folder.getParentFolderId());
        }
        for (int i = 0; i < fileFolders.size(); i++) {
            FileFolder fileFolder = fileFolders.get(i);
            if (fileFolder.getFileFolderName().equals(folder.getFileFolderName())) {
                logger.info("添加文件夹失败!文件夹已存在...");
                return "redirect:/files?error=1&fId=" + folder.getParentFolderId();
            }
        }
        //向数据库写入数据
        Integer integer = fileFolderService.addFileFolder(folder);
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
        FileFolder fileFolder = fileFolderService.getFileFolderByFileFolderId(folder.getFileFolderId());
        fileFolder.setFileFolderName(folder.getFileFolderName());
        //获得当前目录下的所有文件夹,用于检查文件夹是否已经存在
        List<FileFolder> fileFolders = fileFolderService.getFileFolderByParentFolderId(fileFolder.getParentFolderId());
        for (int i = 0; i < fileFolders.size(); i++) {
            FileFolder folder1 = fileFolders.get(i);
            if (folder1.getFileFolderName().equals(folder.getFileFolderName()) && folder1.getFileFolderId() != folder.getFileFolderId()) {
                logger.info("重命名文件夹失败!文件夹已存在...");
                return "redirect:/files?error=2&fId=" + fileFolder.getParentFolderId();
            }
        }
        //向数据库写入数据
        Integer integer = fileFolderService.updateFileFolderById(fileFolder);
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
    public String updateFileName(MyFile file, Map<String, Object> map) {
        MyFile myFile = myFileService.getFileByFileId(file.getMyFileId());
        if (myFile != null) {
            String oldName = myFile.getMyFileName();
            String newName = file.getMyFileName();
            if (!oldName.equals(newName)) {
                boolean b = FtpUtil.reNameFile(myFile.getMyFilePath() + "/" + oldName, myFile.getMyFilePath() + "/" + newName);
                if (b) {

                    if (!(myFile.getShowPath().equals("") || myFile.getShowPath() == null)) {
                        boolean isSuccess=true;
                        if (!(myFile.getPostfix().equals("mp3") || (myFile.getPostfix().equals("mp4"))))
                            isSuccess = FtpUtil.reNameFile(myFile.getShowPath() + "/" + oldName, myFile.getShowPath() + "/" + newName);
                        if (isSuccess)
                            logger.info("转存文件的播放文件更名成功");
                        else
                            logger.info("转存文件的播放文件更名失败");
                    }


                    Integer integer = myFileService.updateFile(
                            MyFile.builder().myFileId(myFile.getMyFileId()).myFileName(newName).build());
                    if (integer == 1) {
                        if (StringUtils.substringAfterLast(file.getMyFileName(), ".").equals("md")) {
                            libraryService.updateEbookNameByBookId(myFile.getMyFileId(), newName);
                        }
                        logger.info("修改文件名成功!原文件名:" + oldName + "  新文件名:" + newName);
                    } else {
                        logger.error("修改文件名失败!原文件名:" + oldName + "  新文件名:" + newName);
                    }

                }
            }


        }
        return "redirect:/files?fId=" + myFile.getParentFolderId();
    }

    /**
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Description 获得二维码
     * @Author xw
     * @Date 15:20 2020/2/12
     * @Param [id, url]
     **/
    @GetMapping("getQrCode")
    @ResponseBody
    public Map<String, Object> getQrCode(@RequestParam Integer id, @RequestParam String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("imgPath", "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=2654852821,3851565636&fm=26&gp=0.jpg");
        if (id != null) {
            MyFile file = myFileService.getFileByFileId(id);
            if (file != null) {
                try {
                    String rootPath = this.getClass().getResource("/").getPath().replaceAll("^\\/", "") + "user_img";
                    url = url + "/file/share?t=" + UUID.randomUUID().toString().substring(0, 10) + "&f=" + file.getMyFileId() + "&p=" + file.getUploadTime().getTime() + "" + file.getSize();
                    File targetFile = new File(rootPath, "");
                    if (!targetFile.exists()) {
                        targetFile.mkdirs();
                    }
                    File f = new File(rootPath, id + ".jpg");
                    if (!f.exists()) {
                        //文件不存在,开始生成二维码并保存文件
                        OutputStream os = new FileOutputStream(f);
                        QRCodeUtil.encode(url, new URL("https://pyyf.oss-cn-hangzhou.aliyuncs.com/community/cloud.png"), os, true);
                        os.close();
                    }
                    PicUploadResult upload = uploadInstance.upload(f);

                    map.put("imgPath", upload.getName());
                    map.put("url", url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }


    @GetMapping("/file/preview")
    public String preview(@RequestParam(value = "fId") Integer id, Model model) {
        Map<String, String> supportPreviewLang = new HashMap<>();
        supportPreviewLang.put("cpp", "cpp");
        supportPreviewLang.put("java", "java");
        supportPreviewLang.put("html", "html");
        supportPreviewLang.put("py", "python");


        MyFile file = myFileService.getFileByFileId(id);
        String fileName = file.getMyFileName();
        String suffix = StringUtils.substringAfterLast(fileName, ".");
        if (suffix.equals("md"))
            return "redirect:/ebook/getbook/" + id;
        if (supportPreviewLang.containsKey(suffix)) {
            if (suffix.equals("java")) {
                StringBuilder fileContentByMyFile = fileStoreService.getFileContentByMyFile(file);
                String code = ifilePreviewService.addQuotationMarks(supportPreviewLang.get(suffix), fileContentByMyFile);
                String htmlContent = MarkdownUtils.markdownToHtmlExtensions(code);

                StringBuilder addJavaCompileHtml = ifilePreviewService.addHtmlCompileModule(new StringBuilder(htmlContent), "java");

                StringBuilder newCode = ifilePreviewService.addHtmlShowStyle(addJavaCompileHtml, Arrays.asList("java"));
                model.addAttribute("code", newCode.toString());
                return "show-code";
            } else {
                StringBuilder fileContentByMyFile = fileStoreService.getFileContentByMyFile(file);
                String code = ifilePreviewService.addQuotationMarks(supportPreviewLang.get(suffix), fileContentByMyFile);
                // 其他语言 启动mardown显示
                String htmlContent = MarkdownUtils.markdownToHtmlExtensions(code);
                StringBuilder newCode = ifilePreviewService.addHtmlShowStyle(new StringBuilder(htmlContent), new ArrayList<>(supportPreviewLang.values()));
                model.addAttribute("code", newCode.toString());
                return "show-code";
            }
        }
        return "redirect:/files";
    }


    /**
     * @return int 1:文本类型   2:图像类型  3:视频类型  4:音乐类型  5:其他类型
     * @Description 根据文件的后缀名获得对应的类型
     * @Author xw
     * @Date 23:20 2020/2/10
     * @Param [type]
     **/
    public int getType(String type) {
        if ("txt".equals(type) || "doc".equals(type) || "docx".equals(type)
                || "wps".equals(type) || "word".equals(type) || "html".equals(type) || "pdf".equals(type)) {
            return 1;
        } else if ("bmp".equals(type) || "gif".equals(type) || "jpg".equals(type)
                || "pic".equals(type) || "png".equals(type) || "jepg".equals(type) || "webp".equals(type)
                || "svg".equals(type)) {
            return 2;
        } else if ("avi".equals(type) || "mov".equals(type) || "qt".equals(type)
                || "asf".equals(type) || "rm".equals(type) || "navi".equals(type) || "wav".equals(type)
                || "mp4".equals(type) || "flv".equals(type)  ) {
            return 3;
        } else if ("mp3".equals(type) || "wma".equals(type)) {
            return 4;
        } else {
            return 5;
        }
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
