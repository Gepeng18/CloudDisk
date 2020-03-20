package site.pyyf.fileStore.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.pyyf.fileStore.entity.Directory;
import site.pyyf.fileStore.entity.Ebook;
import site.pyyf.fileStore.entity.EbookConent;
import site.pyyf.fileStore.service.impl.LibraryServiceImpl;
import site.pyyf.fileStore.utils.CommunityUtil;
import site.pyyf.fileStore.utils.markdown.MarkdownToHtmlUtils;

import java.util.Arrays;

@Controller
public class EBookContentController extends BaseController {

    @Autowired
    private LibraryServiceImpl iLibraryService;

    @RequestMapping(path = "/ebook/getbook/{bookId}")
    public String getEbook(@PathVariable(value = "bookId") int bookId, Model model) {

        Ebook ebook = iLibraryService.selectByBookId(bookId);
        Directory directory = JSONObject.parseObject(ebook.getHeader(), Directory.class, Feature.OrderedField);
        model.addAttribute("headers", directory);
        return "ebook/ebook";
    }


    @ResponseBody
    @RequestMapping(value = "/ebook/getcontent", method = RequestMethod.POST)
    public String getcontent(@RequestParam("contentId") String contentId) {
        String content = iEbookContentService.selectContentByContentId(contentId);
        String htmlContent = MarkdownToHtmlUtils.markdownToHtmlExtensions(content);
        StringBuilder processedContent = new StringBuilder();
        int i = 0;
        char buf;
        while (i < htmlContent.length()) {
            if ((buf = htmlContent.charAt(i)) == '\n' && i != 0) {
                if (CommunityUtil.isChineseNumberAlpha(htmlContent.charAt(i - 1)))
                    processedContent.append("<br>");
            }
            if (htmlContent.charAt(i) == '~')
                processedContent.append("<br>");
            else
                processedContent.append(buf);
            i++;
        }

        StringBuilder newCode = ifilePreviewService.addHtmlShowStyle(ifilePreviewService.addHtmlCompileModule(processedContent, "java"), Arrays.asList("java","cpp","python","html"));
        return CommunityUtil.getJSONString(200, newCode.toString());
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
        EbookConent ebookConent = iEbookContentService.selectByContentId(contentId);
        return "redirect:/ebook/getbook/" + ebookConent.getEbookId();
    }


}
