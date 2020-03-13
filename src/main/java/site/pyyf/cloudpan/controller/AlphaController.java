package site.pyyf.cloudpan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import site.pyyf.cloudpan.entity.MyFile;
import site.pyyf.cloudpan.utils.FtpUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Controller
public class AlphaController extends BaseController{
    private static final Logger logger= LoggerFactory.getLogger(AlphaController.class);

    @RequestMapping("/test")
    public String getImg() {
        return "ebook/test";
    }

    @RequestMapping(value = "/img_preview",method = RequestMethod.GET)
    public void downloadFile(@RequestParam(value = "fId") Integer fId) {

        //获取文件信息
        MyFile myFile = myFileService.getFileByFileId(fId);
        String remotePath = myFile.getMyFilePath();
        String fileName = myFile.getMyFileName();
        try {
            response.setCharacterEncoding("utf-8");
            // 设置返回类型
            response.setContentType("image/png");
            // 文件名转码一下，不然会出现中文乱码
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));

            //去FTP上拉取
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            logger.info("开始下载");
            boolean flag = FtpUtil.downloadFile("/" + remotePath, fileName, os);

            if (flag) {
                logger.info("文件下载成功!" + myFile);
                myFileService.updateFile(
                        MyFile.builder().myFileId(myFile.getMyFileId()).downloadTime(myFile.getDownloadTime() + 1).build());
                os.flush();
                os.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
