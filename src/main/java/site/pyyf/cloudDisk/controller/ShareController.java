package site.pyyf.cloudDisk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.pyyf.cloudDisk.entity.FileFolder;
import site.pyyf.cloudDisk.entity.FileStore;
import site.pyyf.cloudDisk.entity.MyFile;
import site.pyyf.cloudDisk.service.IResolveHeaderService;
import site.pyyf.cloudDisk.utils.CommunityUtil;
import site.pyyf.cloudDisk.utils.FtpUtil;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@Scope("prototype")
public class ShareController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    protected IResolveHeaderService iResolveHeaderService;

    /**
     * @return void
     * @Description 分享文件
     * @Author xw
     * @Date 14:23 2020/2/12
     * @Param [fId]
     **/
    @GetMapping("/file/share")
    public void shareFile(Integer f, String p, String t) {
        //获取文件信息
        MyFile myFile = myFileService.getFileByFileId(f);
        String pwd = myFile.getUploadTime().getTime() + "" + myFile.getSize();
        if (t == null) {
            return;
        }
        if (!pwd.equals(p)) {
            return;
        }
        if (myFile == null) {
            return;
        }
        String remotePath = myFile.getMyFilePath();
        String fileName = myFile.getMyFileName();
        System.out.println("文件位置" + remotePath + fileName);
        try {

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


    @ResponseBody
    @RequestMapping(value = "/getShareUrl", method = RequestMethod.POST)
    public String getShareUrl(@RequestParam(value = "fId") Integer id,
                              @RequestParam(value = "type") String type,
                              Model model) {
        String pwd = UUID.randomUUID().toString().replaceAll("-", "");
        String msg = pwd + "-" + type + "-" + id;
        return CommunityUtil.getJSONString(200, msg);

    }

    @ResponseBody
    @RequestMapping(value = "/share")
    public String share(@RequestParam(value = "fileFolderId") Integer toFileFolderId,
                        @RequestParam(value = "typeAndfid") String typeAndfid) {
        Map<Integer, String> map = new HashMap();
        map.put(200, "转存完成！");
        map.put(501, "当前文件已存在!上传失败！");
        map.put(503, "上传失败!仓库已满！");
        map.put(504, "文件转存失败！");
        map.put(500, "应该不会出现这个情况吧！");
        String[] split = typeAndfid.split("-");

        if (split[1].equals("folder")) {
            //分享的是文件夹
            FileFolder fileFolder = fileFolderService.getFileFolderByFileFolderId(Integer.valueOf(split[2]));
            int result = transferSaveFolder(fileFolder, toFileFolderId);
            return CommunityUtil.getJSONString(result, map.get(result));
        } else if (split[1].equals("file")) {
            MyFile shareFile = myFileService.getFileByFileId(Integer.valueOf(split[2]));
            int result = transferSaveFile(shareFile, toFileFolderId);
            return CommunityUtil.getJSONString(result, map.get(result));

        } else {
            System.out.println("传来一个不知名的内容");
        }
        return null;
    }

    //将文件夹fileFolder放到fileFolderId中
    public int transferSaveFolder(FileFolder fileFolder, Integer toFileFolderId) {
        FileStore store = fileStoreService.getFileStoreByUserId(loginUser.getUserId());
        List<MyFile> files = myFileService.getFilesByParentFolderId(fileFolder.getFileFolderId());
        //设置文件夹信息
        FileFolder thisFolder = FileFolder.builder()
                .fileFolderName(fileFolder.getFileFolderName()).parentFolderId(toFileFolderId).fileStoreId(store.getFileStoreId())
                .time(new Date()).build();
        fileFolderService.addFileFolder(thisFolder);
        for (MyFile file : files) {
            transferSaveFile(file, thisFolder.getFileFolderId());
        }

        List<FileFolder> folders = fileFolderService.getFileFolderByParentFolderId(fileFolder.getFileFolderId());
        for (FileFolder folder : folders) {
            transferSaveFolder(folder, thisFolder.getFileFolderId());
        }
        return 200;
    }

    //将shareFile放在fileFolderId中
    public int transferSaveFile(MyFile shareFile, Integer toFileFolderId) {
        FileStore store = fileStoreService.getFileStoreByUserId(loginUser.getUserId());
        //获取当前目录下的所有文件，用来判断是否已经存在
        List<MyFile> myFiles = null;
        if (toFileFolderId == 0) {
            //当前目录为根目录
            myFiles = myFileService.getRootFilesByFileStoreId(loginUser.getFileStoreId());
        } else {
            //当前目录为其他目录
            myFiles = myFileService.getFilesByParentFolderId(toFileFolderId);
        }
        for (int i = 0; i < myFiles.size(); i++) {
            if (myFiles.get(i).getMyFileName().equals(shareFile.getMyFileName())) {
                logger.error("当前文件已存在!上传失败...");
                return 501;
            }
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());
        String path = loginUser.getUserId() + "/" + dateStr + "/" + toFileFolderId;

        Integer sizeInt = Math.toIntExact(shareFile.getSize() / 1024);
        //是否仓库放不下该文件
        if (store.getCurrentSize() + sizeInt > store.getMaxSize()) {
            logger.error("上传失败!仓库已满。");
            return 503;
        }



        boolean transferFileSuccess = true;
        try {
            //提交到FTP服务器
            transferFileSuccess = FtpUtil.transferFile("/" + shareFile.getMyFilePath(), "/" + path, shareFile.getMyFileName());
            logger.info("转存完成");
        } catch (Exception e) {
            logger.error("转存失败");
            return 504;
        }
        if (transferFileSuccess) {
            //上传成功
            logger.info("文件转存成功!" + shareFile.getMyFileName());

            boolean saveShowFile = true;
            if (!(shareFile.getShowPath() == null || shareFile.getShowPath().equals(""))) {
                if (shareFile.getPostfix().equals("mp3") || (shareFile.getPostfix().equals("mp4"))) {
                    try {
                        saveShowFile = FtpUtil.transferFile("/" + shareFile.getShowPath(), "/" + path, shareFile.getMyFileName());
                    } catch (Exception e) {
                        logger.error("转存在线播放的mp3或mp4文件失败");
                    }
                } else {
                    try {
                        saveShowFile = FtpUtil.transferFile("/" + shareFile.getShowPath(), "/" + path + "_mp", shareFile.getMyFileName());
                    } catch (Exception e) {
                        logger.error("转存在线播放的非mp3或mp4文件失败");
                    }
                }

            }

            //文件转存成功后则需要插入数据库
            MyFile insertFile = null;
            if (saveShowFile) {

                String insertShowPath = null;
                if (shareFile.getPostfix().equals("mp3") || shareFile.getPostfix().equals("mp4"))
                    insertShowPath = path;
                else
                    insertShowPath = path + "_mp";

                insertFile = MyFile.builder()
                        .myFileName(shareFile.getMyFileName()).fileStoreId(loginUser.getFileStoreId()).myFilePath(path)
                        .downloadTime(0).uploadTime(new Date()).parentFolderId(toFileFolderId).
                                size(shareFile.getSize()).type(shareFile.getType()).postfix(shareFile.getPostfix()).showPath(insertShowPath).build();
            } else {
                insertFile = MyFile.builder()
                        .myFileName(shareFile.getMyFileName()).fileStoreId(loginUser.getFileStoreId()).myFilePath(path)
                        .downloadTime(0).uploadTime(new Date()).parentFolderId(toFileFolderId).
                                size(shareFile.getSize()).type(shareFile.getType()).postfix(shareFile.getPostfix()).build();

            }

            //向数据库文件表写入数据
            myFileService.addFileByFileStoreId(insertFile);
            //更新仓库表的当前大小
            fileStoreService.addSize(store.getFileStoreId(), shareFile.getSize());
            logger.info("转存文件插入数据库成功");


            //如果是markdown，则再传一份到library表中
            if (insertFile.getPostfix().equals(".md"))
                try {
                    iResolveHeaderService.readFile(insertFile.getMyFilePath(), insertFile.getMyFileName(), insertFile.getMyFileId());
                    logger.info("markdown插入表成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("markdown转化失败");
                }
            return 200;
        } else {
            return 504;
        }
    }
}
