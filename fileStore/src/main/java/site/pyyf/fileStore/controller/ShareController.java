package site.pyyf.fileStore.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.pyyf.fileStore.config.AliyunConfig;
import site.pyyf.fileStore.entity.FileFolder;
import site.pyyf.fileStore.entity.MyFile;
import site.pyyf.fileStore.entity.UploadResult;
import site.pyyf.fileStore.entity.User;
import site.pyyf.fileStore.service.IResolveHeaderService;
import site.pyyf.fileStore.utils.CommunityUtil;
import site.pyyf.fileStore.utils.FtpUtil;
import site.pyyf.fileStore.utils.QRCodeUtil;
import site.pyyf.fileStore.utils.RedisKeyUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.TimeUnit;


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
     * @Description 获得二维码 和直链下载地址
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
     * @Description 获取直链下载
     * @Author xw
     * @Date 14:23 2020/2/12
     * @Param [fId]
     **/
    @GetMapping("/file/linearDownload")
    public void linearDownload(Integer f, String p, String t) {
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
                              @RequestParam(value = "type") String type) {
        String pwd = UUID.randomUUID().toString().replaceAll("-", "");
        String typeAndId = type + "-" + id;

        // 将验证码存入Redis
        String shareKey = RedisKeyUtil.getShareKey(pwd);
        redisTemplate.opsForValue().set(shareKey, typeAndId, 7, TimeUnit.DAYS);
        return CommunityUtil.getJSONString(200, pwd);

    }


    @ResponseBody
    @RequestMapping(value = "/share")
    public String share(@RequestParam(value = "fileFolderId") Integer toFileFolderId,
                        @RequestParam(value = "pwd") String pwd) {
        Map<Integer, String> map = new HashMap();
        map.put(200, "转存完成！");
        map.put(501, "当前文件已存在!上传失败！");
        map.put(502, "分享码已过期！");
        map.put(503, "上传失败!仓库已满！");
        map.put(504, "文件转存失败！");
        map.put(500, "系统错误，请联系管理员！");

        String shareKey = RedisKeyUtil.getShareKey(pwd);
        String typeAndFid = (String) redisTemplate.opsForValue().get(shareKey);
        if (StringUtils.isBlank(typeAndFid))
            return CommunityUtil.getJSONString(502, map.get(502));


        String[] split = typeAndFid.split("-");
        if (split[0].equals("folder")) {
            //分享的是文件夹
            FileFolder fileFolder = iFileFolderService.getFileFolderByFileFolderId(Integer.valueOf(split[1]));
            int result = transferSaveFolder(fileFolder, toFileFolderId);
            return CommunityUtil.getJSONString(result, map.get(result));
        } else if (split[0].equals("file")) {
            MyFile shareFile = iMyFileService.getFileByFileId(Integer.valueOf(split[1]));
            int result = transferSaveFile(shareFile, toFileFolderId);
            return CommunityUtil.getJSONString(result, map.get(result));

        } else {
            logger.error("传来一个不知名的内容");
            return CommunityUtil.getJSONString(500, map.get(500));
        }
    }

    //将文件夹fileFolder放到fileFolderId中
    public int transferSaveFolder(FileFolder fileFolder, Integer toFileFolderId) {
        User user = iUserService.getUserByUserId(loginUser.getUserId());
        List<MyFile> files = iMyFileService.getFilesByParentFolderId(fileFolder.getFileFolderId());
        //设置文件夹信息
        FileFolder thisFolder = FileFolder.builder()
                .fileFolderName(fileFolder.getFileFolderName()).parentFolderId(toFileFolderId).userId(user.getUserId())
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
        User user = iUserService.getUserByUserId(loginUser.getUserId());
        //获取当前目录下的所有文件，用来判断是否已经存在
        List<MyFile> myFiles = null;
        if (toFileFolderId == 0) {
            //当前目录为根目录
            myFiles = iMyFileService.getRootFilesByUserId(loginUser.getUserId());
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
        if (user.getCurrentSize() + sizeInt > user.getMaxSize()) {
            logger.error("上传失败!仓库已满。");
            return 503;
        }


        //文件转存成功后则需要插入数据库
        MyFile insertFile = MyFile.builder().parentFolderId(toFileFolderId)
                .myFileName(shareFile.getMyFileName()).userId(loginUser.getUserId())
                .downloadTime(0).uploadTime(new Date()).size(shareFile.getSize())
                .type(shareFile.getType()).postfix(shareFile.getPostfix()).build();

        iFileStoreService.transeferFile(shareFile, insertFile);

        if (insertFile == null) {
            logger.error("当前文件上传失败...");
            return 504;
        }
        //向数据库文件表写入数据
        iMyFileService.addFileByUserId(insertFile);
        //更新仓库表的当前大小
        iUserService.addSize(user.getUserId(), shareFile.getSize());
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

