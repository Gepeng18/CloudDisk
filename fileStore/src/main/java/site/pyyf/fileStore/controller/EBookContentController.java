package site.pyyf.fileStore.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.pyyf.fileStore.entity.Header;
import site.pyyf.fileStore.entity.Ebook;
import site.pyyf.fileStore.service.impl.EbooksServiceImpl;
import site.pyyf.fileStore.utils.CloudDiskUtil;
import site.pyyf.fileStore.utils.MarkdownToHtmlUtil;

import java.util.Arrays;

@Controller
public class EBookContentController extends BaseController {

    @Autowired
    private EbooksServiceImpl iLibraryService;

    @RequestMapping(path = "/ebook/getbook/{fileId}")
    public String getEbook(@PathVariable(value = "fileId") int fileId, Model model) {

        Ebook ebook = iLibraryService.selectByFileId(fileId);
        //Feature.OrderedField表示解析时按照顺序解析，不要打乱List中元素相对顺序
        Header directory = JSONObject.parseObject(ebook.getHeader(), Header.class, Feature.OrderedField);
        model.addAttribute("headers", directory);
        return "ebook/ebook";
    }


    @ResponseBody
    @RequestMapping(value = "/ebook/getcontent", method = RequestMethod.POST)
    public String getcontent(@RequestParam("contentId") String contentId) {
        String content = iEbookContentService.selectContentByContentId(contentId);
        String htmlContent = MarkdownToHtmlUtil.markdownToHtmlExtensions(content);
        StringBuilder processedContent = new StringBuilder();
        int i = 0;
        char buf;
        while (i < htmlContent.length()) {
            if ((buf = htmlContent.charAt(i)) == '\n' && i != 0) {
                if (CloudDiskUtil.isChineseNumberAlpha(htmlContent.charAt(i - 1)))
                    processedContent.append("<br>");
            }
            if (htmlContent.charAt(i) == '~')
                processedContent.append("<br>");
            else
                processedContent.append(buf);
            i++;
        }

        StringBuilder newCode = iCodeService.addHtmlShowStyle(iCodeService.addHtmlCompileModule(processedContent, "java"), Arrays.asList("java", "cpp", "python", "html"));
        return CloudDiskUtil.getJSONString(200, newCode.toString());
    }

    @RequestMapping(value = "/ebook/goupdate", method = RequestMethod.POST)
    public String goUpdatePage(@RequestParam("contentId") String contentId,
                               Model model) {
        String content = iEbookContentService.selectContentByContentId(contentId);
        model.addAttribute("content", content);
        model.addAttribute("contentId", contentId);
        return "ebook/update";
    }

    @RequestMapping(value = "/ebook/update", method = RequestMethod.POST)
    public String updateContent(@RequestParam("content") String content,
                                @RequestParam("contentId") String contentId,
                                Model model) {
        iEbookContentService.updateContentByContentId(contentId, content);
        int ebookId = iEbookContentService.selectEbookIdByContentId(contentId);
        return "redirect:/ebook/getbook/" + ebookId;
    }


}
