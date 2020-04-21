package site.pyyf.fileStore.controller;

import org.slf4j.LoggerFactory;
import site.pyyf.fileStore.entity.FileFolder;
import site.pyyf.fileStore.entity.User;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Description 登录控制器
 * @Author xw
 * @Date 18:19 2020/2/25
 * @Param  * @param null
 * @return
 **/
@Controller
public class LoginController extends BaseController {

    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

    /**
     * @Description 免登陆用户入口
     * @Author xw
     * @Date 15:17 2020/2/26
     * @Param []
     * @return java.lang.String
     **/
    @GetMapping("/test")
    public String testLogin(){
        String testOpenID = "12345678";
        String testNickName = "测试用户";
        String testAvatar = "https://pyyf.oss-cn-hangzhou.aliyuncs.com/community/icons/右.png";
        //设置用户信息
        List<User> users = iUserService.queryAll(User.builder().openId(testOpenID).build());

        User user = null;
        if (users.size() == 0){
            user = User.builder()
                    .openId(testOpenID).userName(testNickName)
                    .imagePath(testAvatar).
                            registerTime(new Date()).build();
            if (iUserService.insert(user)>0){
                FileFolder rootFolder = FileFolder.builder().userId(user.getId()).createTime(new Date()).parentFolderId(0).fileFolderName("根文件夹").build();
                int result = iFileFolderService.insert(rootFolder);
                if(result>0){
                    logger.info("测试用户注册成功");
                    //创建用户时无法确定根文件夹ID，所以再创建根文件夹后更新用户
                    user.setRootFolder(rootFolder.getId());
                    iUserService.update(user);
                }

            } else {
                logger.error("注册用户失败！");
            }
        }else {
           user = users.get(0);
        }
        logger.info("测试用户登录成功");
        session.setAttribute("loginUser", user);
            return "redirect:/index";

    }

    /**
     * @Description 请求QQ登录
     * @Author xw
     * @Date 18:27 2020/2/25
     * @Param []
     * @return void
     **/
    @GetMapping("/login")
    public void login() {
        response.setContentType("text/html;charset=utf-8");
        try {
            response.sendRedirect(new Oauth().getAuthorizeURL(request));
            logger.info("请求QQ登录,开始跳转...");
        } catch (QQConnectException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @Description QQ登录回调地址
     * @Author xw
     * @Date 18:27 2020/2/25
     * @Param []
     * @return java.lang.String
     **/
    @GetMapping("/tencentqq")
    public String connection() {
        try {
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
            String accessToken = null, openID = null;
            long tokenExpireIn = 0L;
            if ("".equals(accessTokenObj.getAccessToken())) {
                logger.error("登录失败:没有获取到响应参数");
                return "accessTokenObj=>" + accessTokenObj + "; accessToken" + accessTokenObj.getAccessToken();
            } else {
                accessToken = accessTokenObj.getAccessToken();
                tokenExpireIn = accessTokenObj.getExpireIn();
                logger.info("accessToken" + accessToken);
                request.getSession().setAttribute("demo_access_token", accessToken);
                request.getSession().setAttribute("demo_token_expirein", String.valueOf(tokenExpireIn));
                // 利用获取到的accessToken 去获取当前用的openid -------- start
                OpenID openIDObj = new OpenID(accessToken);
                openID = openIDObj.getUserOpenID();
                UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                if (userInfoBean.getRet() == 0) {
                    logger.info("用户的OPEN_ID: " + openID);
                    logger.info("用户的昵称: " + removeNonBmpUnicode(userInfoBean.getNickname()));
                    logger.info("用户的头像URI: " + userInfoBean.getAvatar().getAvatarURL100());
                    //设置用户信息
                    List<User> users = iUserService.queryAll(User.builder().openId(openID).build());
                    User user = null;
                    if (users.size()==0){
                        user = User.builder()
                                .openId(openID).userName(removeNonBmpUnicode(userInfoBean.getNickname()))
                                .imagePath(userInfoBean.getAvatar().getAvatarURL100()).
                                registerTime(new Date()).build();
                        if (iUserService.insert(user)>0){
                            FileFolder rootFolder = FileFolder.builder().createTime(new Date()).userId(user.getId()).parentFolderId(0).fileFolderName("根文件夹").build();
                            int result = iFileFolderService.insert(rootFolder);
                            if(result>0){
                                logger.info("测试用户注册成功");
                                //创建用户时无法确定根文件夹ID，所以再创建根文件夹后更新用户
                                user.setRootFolder(rootFolder.getId());
                                iUserService.update(user);
                            }
                        } else {
                            logger.error("注册用户失败！");
                        }
                    }else {
                        user = users.get(0);
                        user.setUserName(removeNonBmpUnicode(userInfoBean.getNickname()));
                        user.setImagePath(userInfoBean.getAvatar().getAvatarURL100());
                        iUserService.update(user);
                    }
                    logger.info("QQ用户登录成功！"+user);
                    session.setAttribute("loginUser", user);
                    return "redirect:/index";
                } else {
                    logger.error("很抱歉，我们没能正确获取到您的信息，原因是： " + userInfoBean.getMsg());
                }
            }
        } catch (QQConnectException e) {
            e.printStackTrace();
        }
        return "登录失败!请查看日志信息...";
    }

    /**
     * @Description 处理掉QQ网名中的特殊表情
     * @Author xw
     * @Date 18:26 2020/2/25
     * @Param [str]
     * @return java.lang.String 返回处理之后的网名
     **/
    public String removeNonBmpUnicode(String str) {
        if (str == null) {
            return null;
        }
        str = str.replaceAll("[^\\u0000-\\uFFFF]", "");
        if ("".equals(str)) {
            str = "($ _ $)";
        }
        return str;
    }

    /**
     * @Description 退出登录，清空session
     * @Author xw
     * @Date 18:26 2020/2/25
     * @Param []
     * @return java.lang.String
     **/
    @GetMapping("/logout")
    public String logout() {
        logger.info("用户退出登录！");
        session.invalidate();
        return "redirect:/";
    }

}
