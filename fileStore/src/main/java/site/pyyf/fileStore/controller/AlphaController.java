package site.pyyf.fileStore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AlphaController extends BaseController{
    private static final Logger logger= LoggerFactory.getLogger(AlphaController.class);

    @RequestMapping("/alpha")
    public String getImg() {
        return "ebook/test";
    }

}
