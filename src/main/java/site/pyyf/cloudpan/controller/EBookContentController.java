package site.pyyf.cloudpan.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.pyyf.cloudpan.entity.Directory;
import site.pyyf.cloudpan.entity.Ebook;
import site.pyyf.cloudpan.entity.EbookConent;
import site.pyyf.cloudpan.service.EbookContentService;
import site.pyyf.cloudpan.service.IfilePreviewService;
import site.pyyf.cloudpan.service.LibraryService;
import site.pyyf.cloudpan.utils.CommunityUtil;
import site.pyyf.cloudpan.utils.MarkdownUtils;

import java.util.Arrays;

@Controller
public class EBookContentController extends BaseController {

    @Autowired
    public EbookContentService ebookContentService;

    @Autowired
    private LibraryService libraryService;

    @RequestMapping(path = "/ebook/getbook/{bookId}")
    public String getEbook(@PathVariable(value = "bookId") int bookId, Model model) {

        Ebook ebook = libraryService.selectByBookId(bookId);
        final Directory directory = JSONObject.parseObject(ebook.getHeader(), Directory.class, Feature.OrderedField);
        model.addAttribute("headers", directory);
        return "ebook/ebook";
    }


    @ResponseBody
    @RequestMapping(value = "/ebook/getcontent", method = RequestMethod.POST)
    public String getcontent(@RequestParam("contentId") String contentId) {
        final String content = ebookContentService.selectContentByContentId(contentId);
        final String htmlContent = MarkdownUtils.markdownToHtmlExtensions(content);
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
        final String content = ebookContentService.selectContentByContentId(contentId);
        model.addAttribute("content", content);
        model.addAttribute("contentId", contentId);
        return "ebook/update";
    }

    @RequestMapping(value = "/ebook/update", method = RequestMethod.POST)
    public String updateContent(@RequestParam("content") String content,
                                @RequestParam("contentId") String contentId,
                                Model model) {
        ebookContentService.updateContentByContentId(contentId, content);
        EbookConent ebookConent = ebookContentService.selectByContentId(contentId);
        return "redirect:/ebook/getbook/" + ebookConent.getEbookId();
    }


}
