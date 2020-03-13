package site.pyyf.cloudpan.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.pyyf.cloudpan.entity.MyFile;
import site.pyyf.cloudpan.utils.FtpUtil;
import site.pyyf.cloudpan.utils.MarkdownUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
}

