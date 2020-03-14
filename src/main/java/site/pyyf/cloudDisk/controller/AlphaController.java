package site.pyyf.cloudDisk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import site.pyyf.cloudDisk.entity.MyFile;
import site.pyyf.cloudDisk.utils.FtpUtil;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@Controller
public class AlphaController extends BaseController{
    private static final Logger logger= LoggerFactory.getLogger(AlphaController.class);

    @RequestMapping("/alpha")
    public String getImg() {
        return "ebook/test";
    }

}
