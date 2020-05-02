package site.pyyf.fileStore.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.pyyf.fileStore.entity.EbookContent;
import site.pyyf.fileStore.entity.Header;
import site.pyyf.fileStore.entity.Ebook;
import site.pyyf.fileStore.service.ICodePreviewService;
import site.pyyf.fileStore.utils.CloudDiskUtil;
import site.pyyf.fileStore.utils.MarkdownToHtmlUtil;

import java.util.Arrays;

@Controller
public class EBookContentController extends BaseController {

    @RequestMapping(path = "/ebook/getbook/{fileId}")
    public String getEbook(@PathVariable(value = "fileId") int fileId, Model model) {

        //搜到只会有一个
        Ebook ebook = iEbookService.queryAll(Ebook.builder().fileId(fileId).build()).get(0);
        //Feature.OrderedField表示解析时按照顺序解析，不要打乱List中元素相对顺序
        Header directory = JSONObject.parseObject(ebook.getHeader(), Header.class, Feature.OrderedField);
        model.addAttribute("headers", directory);

        //前端根据这里判断是否在点击有子节点的目录的item时向后端请求，
        //如果是md则需要请求，是proj则不需要请求
        if ("md".equals(StringUtils.substringAfterLast(ebook.getEbookName(), ".")))
            model.addAttribute("mode", 0);
        else if ("proj".equals(StringUtils.substringAfterLast(ebook.getEbookName(), ".")))
            model.addAttribute("mode", 1);
        return "ebook/ebook";
    }


    @ResponseBody
    @RequestMapping(value = "/ebook/getcontent", method = RequestMethod.POST)
    public String getcontent(@RequestParam("contentId") String contentId) {
        //搜到只会有一个
        String content = iEbookContentService.queryAll(EbookContent.builder().contentId(contentId).build()).get(0).getContent();
        content = iCodePreviewService.addQuotationMarks("java", new StringBuilder(content));
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
        String content = iEbookContentService.queryAll(EbookContent.builder().contentId(contentId).build()).get(0).getContent();
        model.addAttribute("content", content);
        model.addAttribute("contentId", contentId);
        return "ebook/update";
    }

    @RequestMapping(value = "/ebook/update", method = RequestMethod.POST)
    public String updateContent(@RequestParam("content") String content,
                                @RequestParam("contentId") String contentId,
                                Model model) {
        iEbookContentService.update(EbookContent.builder().contentId(contentId).content(content).build());
        int ebookId = iEbookContentService.queryAll(EbookContent.builder().contentId(contentId).build()).get(0).getFileId();
        return "redirect:/ebook/getbook/" + ebookId;
    }


}
