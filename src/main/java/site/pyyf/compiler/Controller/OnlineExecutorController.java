package site.pyyf.compiler.Controller;


import jdk.nashorn.internal.ir.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pyyf.cloudpan.service.MyFileService;
import site.pyyf.cloudpan.utils.CommunityUtil;
import site.pyyf.compiler.service.IExecuteStringSourceService;
import site.pyyf.compiler.service.Impl.SiteSettingService;


@Controller
public class OnlineExecutorController {
    private Logger logger = LoggerFactory.getLogger(OnlineExecutorController.class);

    @Autowired
    private SiteSettingService siteSettingService;

    @Autowired
    private IExecuteStringSourceService executeStringSourceService;

    @RequestMapping(path = "/compile", method = RequestMethod.POST)
    @ResponseBody
    public String commpile(@RequestParam(value = "code") String code,
                           @RequestParam(value = "input",defaultValue = "") String input) {
        if (siteSettingService.allowOnlineExecutor() == 0)
            return CommunityUtil.getJSONString(200, "非常抱歉，该功能正在测试中，请谅解");
        //因为前端传来的代码不光是代码，还有前端加上的如：java copy这些字样，这是前端的显示格式，也一并传到了后端，所以这里把它都去掉
        String pureCode = code.substring(0, code.lastIndexOf("}")+1);
        String runResult = executeStringSourceService.execute(pureCode, input);
        runResult = runResult.replaceAll(System.lineSeparator(), "\n"); // 处理html中换行的问题
        return CommunityUtil.getJSONString(200, runResult);
    }

}
