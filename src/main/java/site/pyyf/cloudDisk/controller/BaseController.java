package site.pyyf.cloudDisk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.ModelAttribute;
import site.pyyf.cloudDisk.config.CloudDiskConfig;
import site.pyyf.cloudDisk.entity.User;
import site.pyyf.cloudDisk.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @ClassName: BaseController
 * @Description: 控制器的基类，所有控制器必须继承此类
 * @author: xw
 * @date 2020/2/25 18:19
 * @Version: 1.0
 **/
public class BaseController {
    @Autowired
    protected IMediaTranfer iMediaTranfer;
    @Autowired
    protected IFilePreviewService ifilePreviewService;

    @Autowired
    protected IUserService iUserService;
    @Autowired
    protected IMyFileService iMyFileService;
    @Autowired
    protected IFileFolderService iFileFolderService;

//    @Autowired
//    protected UserService userService;

    @Autowired
    protected ILibraryService iLibraryService;
    @Autowired
    protected CloudDiskConfig cloudDiskConfig;
    @Autowired
    protected IOSSService iossService;
    @Autowired
    protected IEbookContentService iEbookContentService;
    @Autowired
    protected RedisTemplate redisTemplate;

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected HttpSession session;
    protected User loginUser;

    /**
     * 在每个子类方法调用之前先调用
     * 设置request,response,session这三个对象
     *
     * @param request
     * @param response
     */
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.session = request.getSession(true);
        loginUser = (User) session.getAttribute("loginUser");
    }
}
