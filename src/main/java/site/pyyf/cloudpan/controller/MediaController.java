package site.pyyf.cloudpan.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.pyyf.cloudpan.entity.MyFile;
import site.pyyf.cloudpan.utils.FtpUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@Controller
public class MediaController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    @RequestMapping("/video")
    public void videoShow(@RequestParam(value = "fId") Integer fId,
                          HttpServletResponse response) {
        //获取文件信息
        MyFile myFile = myFileService.getFileByFileId(fId);


        //可以将条件合并，但为方便理解这里没有合并
        if(myFile.getShowPath().equals("")||myFile.getShowPath()==null)
            if(!myFile.getPostfix().equals("mp4"))
                return ;
        String remotePath = myFile.getShowPath();
        String fileName = myFile.getMyFileName();
        try {
            //去FTP上拉取
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            logger.info("开始下载");
            response.setCharacterEncoding("utf-8");
            // 设置返回类型
            response.setContentType("video/mpeg4");
            // 文件名转码一下，不然会出现中文乱码
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


    @RequestMapping("/audio")
    public void mp3Show(@RequestParam(value = "fId") Integer fId, HttpServletResponse response) {

        //获取文件信息
        MyFile myFile = myFileService.getFileByFileId(fId);

        //可以将条件合并，但为方便理解这里没有合并
        if(myFile.getShowPath().equals("")||myFile.getShowPath()==null)
            if(!myFile.getPostfix().equals("mp3"))
                return ;

        String remotePath = myFile.getShowPath();
        String fileName = myFile.getMyFileName();
        try {
            //去FTP上拉取
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            logger.info("开始下载");
            response.setCharacterEncoding("utf-8");
            // 设置返回类型
            response.setContentType("audio/mp3");
            // 文件名转码一下，不然会出现中文乱码
            boolean flag = FtpUtil.downloadFile("/" + remotePath, fileName, os);
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
}

