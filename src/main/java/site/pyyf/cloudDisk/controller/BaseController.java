package site.pyyf.cloudDisk.controller;

import site.pyyf.cloudDisk.entity.User;
import site.pyyf.cloudDisk.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import site.pyyf.cloudDisk.service.FileFolderService;
import site.pyyf.cloudDisk.service.FileStoreService;
import site.pyyf.cloudDisk.service.MyFileService;
import site.pyyf.cloudDisk.service.UserService;
import site.pyyf.cloudDisk.service.impl.EbookContentService;
import site.pyyf.cloudDisk.utils.UploadOss.OssUpload;

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

    protected OssUpload uploadInstance = OssUpload.getInstance();

    @Autowired
    protected  IMediaTranfer iMediaTranfer;
    @Autowired
    protected IfilePreviewService ifilePreviewService;

    @Autowired
    protected UserService userService;
    @Autowired
    protected MyFileService myFileService;
    @Autowired
    protected FileFolderService fileFolderService;
    @Autowired
    protected FileStoreService fileStoreService;
    @Autowired
    protected ILibraryService iLibraryService;

    @Autowired
    protected IEbookContentService iEbookContentService;
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
