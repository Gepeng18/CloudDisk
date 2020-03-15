package site.pyyf.cloudDisk.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.pyyf.cloudDisk.config.AliyunConfig;
import site.pyyf.cloudDisk.entity.FileFolder;
import site.pyyf.cloudDisk.entity.FileStore;
import site.pyyf.cloudDisk.entity.MyFile;
import site.pyyf.cloudDisk.entity.UploadResult;
import site.pyyf.cloudDisk.service.IResolveHeaderService;
import site.pyyf.cloudDisk.utils.CommunityUtil;
import site.pyyf.cloudDisk.utils.FtpUtil;
import site.pyyf.cloudDisk.utils.QRCodeUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@Scope("prototype")
public class ShareController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(ShareController.class);

    @Autowired
    private AliyunConfig aliyunConfig;
    @Autowired
    protected IResolveHeaderService iResolveHeaderService;


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
            MyFile file = iMyFileService.getFileByFileId(id);
            if (file != null) {
                try {
                    String rootPath = this.getClass().getResource("/").getPath().replaceAll("^\\/", "") + "user_img";
                    url = url + "/file/linearDownload?t=" + UUID.randomUUID().toString().substring(0, 10) + "&f=" + file.getMyFileId() + "&p=" + file.getUploadTime().getTime() + "" + file.getSize();
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
                    UploadResult upload = iossService.upload(f, "cloudDisk/QrCode");

                    map.put("imgPath", upload.getUrl());
                    map.put("url", url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }



    /**
     * @return void
     * @Description 分享文件
     * @Author xw
     * @Date 14:23 2020/2/12
     * @Param [fId]
     **/
    @GetMapping("/file/linearDownload")
    public void shareFile(Integer f, String p, String t) {
        //获取文件信息
        MyFile myFile = iMyFileService.getFileByFileId(f);
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
            boolean flag = FtpUtil.downloadFile("/" + remotePath, os);
            logger.info("下载完成");
            if (flag) {
                iMyFileService.updateFile(
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
                              @RequestParam(value = "type") String type
    ) {
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
            FileFolder fileFolder = iFileFolderService.getFileFolderByFileFolderId(Integer.valueOf(split[2]));
            int result = transferSaveFolder(fileFolder, toFileFolderId);
            return CommunityUtil.getJSONString(result, map.get(result));
        } else if (split[1].equals("file")) {
            MyFile shareFile = iMyFileService.getFileByFileId(Integer.valueOf(split[2]));
            int result = transferSaveFile(shareFile, toFileFolderId);
            return CommunityUtil.getJSONString(result, map.get(result));

        } else {
            logger.error("传来一个不知名的内容");
        }
        return null;
    }

    //将文件夹fileFolder放到fileFolderId中
    public int transferSaveFolder(FileFolder fileFolder, Integer toFileFolderId) {
        FileStore store = iFileStoreService.getFileStoreByUserId(loginUser.getUserId());
        List<MyFile> files = iMyFileService.getFilesByParentFolderId(fileFolder.getFileFolderId());
        //设置文件夹信息
        FileFolder thisFolder = FileFolder.builder()
                .fileFolderName(fileFolder.getFileFolderName()).parentFolderId(toFileFolderId).fileStoreId(store.getFileStoreId())
                .time(new Date()).build();
        iFileFolderService.addFileFolder(thisFolder);
        for (MyFile file : files) {
            transferSaveFile(file, thisFolder.getFileFolderId());
        }

        List<FileFolder> folders = iFileFolderService.getFileFolderByParentFolderId(fileFolder.getFileFolderId());
        for (FileFolder folder : folders) {
            transferSaveFolder(folder, thisFolder.getFileFolderId());
        }
        return 200;
    }

    //将shareFile放在fileFolderId中
    public int transferSaveFile(MyFile shareFile, Integer toFileFolderId) {
        FileStore store = iFileStoreService.getFileStoreByUserId(loginUser.getUserId());
        //获取当前目录下的所有文件，用来判断是否已经存在
        List<MyFile> myFiles = null;
        if (toFileFolderId == 0) {
            //当前目录为根目录
            myFiles = iMyFileService.getRootFilesByFileStoreId(loginUser.getFileStoreId());
        } else {
            //当前目录为其他目录
            myFiles = iMyFileService.getFilesByParentFolderId(toFileFolderId);
        }
        for (int i = 0; i < myFiles.size(); i++) {
            if (myFiles.get(i).getMyFileName().equals(shareFile.getMyFileName())) {
                logger.error("当前文件已存在!上传失败...");
                return 501;
            }
        }


        Integer sizeInt = Math.toIntExact(shareFile.getSize() / 1024);
        //是否仓库放不下该文件
        if (store.getCurrentSize() + sizeInt > store.getMaxSize()) {
            logger.error("上传失败!仓库已满。");
            return 503;
        }

        String insertRemotePath = null;
        String insertShowPath = null;
        if (cloudDiskConfig.getType().equals("OSS")||shareFile.getType()==2) {
            //提交到OSS服务器
            UploadResult OSStransferRes = iossService.transfer(shareFile.getMyFilePath().substring(aliyunConfig.getUrlPrefix().length()),
                    StringUtils.substringBeforeLast(shareFile.getMyFilePath().substring(aliyunConfig.getUrlPrefix().length()), "/"));
            if (OSStransferRes.getStatus().equals("done")) {
                logger.info("OSS中remote文件转存完成");
                insertRemotePath = OSStransferRes.getUrl();
                insertShowPath = OSStransferRes.getUrl();
            } else {
                logger.error("OSS中remote文件转存失败");
                return 504;
            }


            if (!shareFile.getShowPath().equals(shareFile.getMyFilePath())) {
                //提交到FTP服务器
                OSStransferRes = iossService.transfer(shareFile.getShowPath().substring(shareFile.getShowPath().indexOf(aliyunConfig.getUrlPrefix()) + 1),
                        StringUtils.substringBeforeLast(shareFile.getShowPath().substring(shareFile.getShowPath().indexOf(aliyunConfig.getUrlPrefix()) + 1), "/"));
                if (OSStransferRes.getStatus().equals("done")) {
                    logger.info("OSS中show文件转存完成");
                    insertShowPath = OSStransferRes.getUrl();
                } else {
                    logger.error("OSS中show文件转存失败");
                    return 504;
                }

            }


        } else {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = format.format(new Date());
            String remoteFilePath = loginUser.getUserId() + "/" + dateStr + "/" + toFileFolderId+"/"+UUID.randomUUID().toString()+"."+shareFile.getPostfix();

            try {
                //提交到FTP服务器
                FtpUtil.transferFile("/" + shareFile.getMyFilePath(), "/" + remoteFilePath);
                logger.info("FTP中remote文件转存完成");
                insertRemotePath = remoteFilePath;
                insertShowPath = remoteFilePath;
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("FTP中remote文件转存失败");
                return 504;
            }

            if (!shareFile.getShowPath().equals(shareFile.getMyFilePath())) {
                try {
                    FtpUtil.transferFile("/" + shareFile.getShowPath(), "/" + remoteFilePath + "_mp");
                    logger.error("FTP中showfile转存成功");
                    insertShowPath = remoteFilePath + "_mp";
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("FTP中showfile转存失败");
                    return 504;
                }

            }

        }


        //文件转存成功后则需要插入数据库
        MyFile insertFile = MyFile.builder()
                .myFileName(shareFile.getMyFileName()).fileStoreId(loginUser.getFileStoreId()).myFilePath(insertRemotePath)
                .downloadTime(0).uploadTime(new Date()).parentFolderId(toFileFolderId).
                        size(shareFile.getSize()).type(shareFile.getType()).postfix(shareFile.getPostfix()).showPath(insertShowPath).build();


        //向数据库文件表写入数据
        iMyFileService.addFileByFileStoreId(insertFile);
        //更新仓库表的当前大小
        iFileStoreService.addSize(store.getFileStoreId(), shareFile.getSize());
        logger.info("转存文件插入数据库成功");


        //如果是markdown，则再传一份到library表中
        if (insertFile.getPostfix().equals(".md")) {
            try {
                iResolveHeaderService.readFile(insertFile.getMyFilePath(), insertFile.getMyFileName(), insertFile.getMyFileId());
                logger.info("文件转存过程中markdown转化成功");
                return 200;
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("文件转存过程中markdown转化失败");
                return 504;
            }

        }
        return 200;
    }
}

