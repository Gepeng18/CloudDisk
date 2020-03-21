package site.pyyf.fileStore.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.pyyf.cloudDiskInterface.service.IExecuteStringSourceService;
import site.pyyf.fileStore.service.ISiteSettingService;
import site.pyyf.fileStore.utils.CloudDiskUtil;

import java.io.IOException;


@Controller
public class OnlineExecutorController {
    private Logger logger = LoggerFactory.getLogger(OnlineExecutorController.class);

    @Autowired
    private ISiteSettingService iSiteSettingService;

    @Reference(loadbalance = "random", timeout = 5000, check = false) //dubbo直连
    private IExecuteStringSourceService iExecuteStringSourceService;

    public String errorExec(@RequestParam(value = "code") String code,
                            @RequestParam(value = "input",defaultValue = "") String input) {

        String runResult = "服务器发生了错误，请稍后再试";
        return CloudDiskUtil.getJSONString(200, runResult);
    }

    @HystrixCommand(fallbackMethod="errorExec")
    @RequestMapping(path = "/java/compile", method = RequestMethod.POST)
    @ResponseBody
    public String commpile(@RequestParam(value = "code") String code,
                           @RequestParam(value = "input",defaultValue = "") String input) throws IOException {
        if (iSiteSettingService.allowOnlineExecutor() == 0)
            return CloudDiskUtil.getJSONString(200, "非常抱歉，该功能正在测试中，请谅解");
        //因为前端传来的代码不光是代码，还有前端加上的如：java copy这些字样，这是前端的显示格式，也一并传到了后端，所以这里把它都去掉
        String pureCode = code.substring(0, code.lastIndexOf("}")+1);
        String runResult = iExecuteStringSourceService.execute(pureCode, input);
        runResult = runResult.replaceAll(System.lineSeparator(), "\n"); // 处理html中换行的问题
        return CloudDiskUtil.getJSONString(200, runResult);
    }

}
