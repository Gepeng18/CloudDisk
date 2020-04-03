package site.pyyf.fileStore.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import site.pyyf.fileStore.entity.MyFile;
import site.pyyf.fileStore.utils.FtpUtil;
import site.pyyf.fileStore.utils.MarkdownToHtmlUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PreviewController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PreviewController.class);

    @RequestMapping("/video")
    public void videoShow(@RequestParam(value = "fId") Integer fId,
                          HttpServletResponse response) {
        //获取文件信息
        MyFile myFile = iMyFileService.getFileByFileId(fId);


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
            boolean flag = FtpUtil.downloadFile("/" + remotePath, os);
            logger.info("下载完成");
            if (flag) {
                iMyFileService.updateFile(
                        MyFile.builder().myFileId(myFile.getMyFileId()).downloadTime(myFile.getDownloadTime() + 1).build());
                os.flush();
                os.close();
                logger.info("文件下载成功!  ----->>>>>  " + myFile.getMyFileName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @RequestMapping("/audio")
    public void mp3Show(@RequestParam(value = "fId") Integer fId, HttpServletResponse response) {

        //获取文件信息
        MyFile myFile = iMyFileService.getFileByFileId(fId);

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
            boolean flag = FtpUtil.downloadFile("/" + remotePath, os);
            if (flag) {
                iMyFileService.updateFile(
                        MyFile.builder().myFileId(myFile.getMyFileId()).downloadTime(myFile.getDownloadTime() + 1).build());
                os.flush();
                os.close();
                logger.info("文件下载成功!  ----->>>>>  " + myFile.getMyFileName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @GetMapping("/code/preview")
    public String preview(@RequestParam(value = "fId") Integer id, Model model) {
        Map<String, String> supportPreviewLang = new HashMap<>();
        supportPreviewLang.put("cpp", "cpp");
        supportPreviewLang.put("java", "java");
        supportPreviewLang.put("html", "html");
        supportPreviewLang.put("py", "python");


        MyFile file = iMyFileService.getFileByFileId(id);
        String fileName = file.getMyFileName();
        String suffix = StringUtils.substringAfterLast(fileName, ".");
        if (suffix.equals("md"))
            return "redirect:/ebook/getbook/" + id;
        if (supportPreviewLang.containsKey(suffix)) {
            if (suffix.equals("java")) {
                StringBuilder fileContentByMyFile = iFileStoreService.getFileContentByMyFile(file);
                String code = iCodeService.addQuotationMarks(supportPreviewLang.get(suffix), fileContentByMyFile);
                String htmlContent = MarkdownToHtmlUtil.markdownToHtmlExtensions(code);

                StringBuilder addJavaCompileHtml = iCodeService.addHtmlCompileModule(new StringBuilder(htmlContent), "java");

                StringBuilder newCode = iCodeService.addHtmlShowStyle(addJavaCompileHtml, Arrays.asList("java"));
                model.addAttribute("code", newCode.toString());
                return "clouddisk/show-code";
            } else {
                StringBuilder fileContentByMyFile = iFileStoreService.getFileContentByMyFile(file);
                String code = iCodeService.addQuotationMarks(supportPreviewLang.get(suffix), fileContentByMyFile);
                // 其他语言 启动mardown显示
                String htmlContent = MarkdownToHtmlUtil.markdownToHtmlExtensions(code);
                StringBuilder newCode = iCodeService.addHtmlShowStyle(new StringBuilder(htmlContent), new ArrayList<>(supportPreviewLang.values()));
                model.addAttribute("code", newCode.toString());
                return "clouddisk/show-code";
            }
        }
        return "redirect:/files";
    }



    @RequestMapping(value = "/img_preview",method = RequestMethod.GET)
    public void downloadFile(@RequestParam(value = "fId") Integer fId) {

        //获取文件信息
        MyFile myFile = iMyFileService.getFileByFileId(fId);
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
            boolean flag = FtpUtil.downloadFile("/" + remotePath, os);

            if (flag) {
                logger.info("文件下载成功!  ----->>>>>  " + myFile.getMyFileName());
                iMyFileService.updateFile(
                        MyFile.builder().myFileId(myFile.getMyFileId()).downloadTime(myFile.getDownloadTime() + 1).build());
                os.flush();
                os.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

