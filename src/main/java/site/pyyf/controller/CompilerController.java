package site.pyyf.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pyyf.utils.CommunityUtil;

@Controller
public class CompilerController {

    @ResponseBody
    @RequestMapping("/compile")
    public String commpile(@RequestParam(value = "code") String code
                           /*@RequestParam(value = "input") String input*/) {
        System.out.println(code);
        return CommunityUtil.getJSONString(200, code);
    }

}




