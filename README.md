
本项目基于[莫提网盘](https://github.com/373675032/moti-cloud) 进行二次开发
- 增加在线预览功能，主要针对代码文件进行在线预览
- java代码提供在线编译系统，在线编译后台已完成单还未整合，稍后进行整合
- 提供markdown自动分页功能，使其易于在浏览器上进行浏览（需要后台上传文件，此处由于前端不会写，没办法）


使用：
请修改所有sample文件后进行部署

2020.3.8
- markdown的内容分解后批量进行上传，同时云OSS存储采用多线程进行上传。
- 前端进行了改进，预览代码支持更多的语言且显示括号和行号

2020.3.7
- 引入QQ登录模块   输入任意页面（包含项目前缀）被LoginHandlerInterceptor重定向到/路径，MvcConfig将/映射为index登录页面，然后点击/login后，重定向new Oauth().getAuthorizeURL(request)，这里即根据qqconnectconfig中的信息请求QQ互联，QQ互联将QQ信息回调给redirect_URI（即QQ互联回调域及qqconnectconfig中的redirect_URI），所以redirect_URI不能被拦截，且这里使用controller进行接收，至此登录完成。

2020.3.7
- 将jquery.contextMenu.min.js中的 top: t.clientY + 2改为 top: t.clientY + $(window).scrollTop()+2以修复当页面下拉时出现菜单显示位置错误

2020.3.6
- 修改前端ebook界面，使得界面左右可以分别滑动，侧边栏上下滑动，左右超出区域分行显示，且侧边栏以contentid作为id(之前是header)
- 后端重新改了数据结构，改为普通的树结构

2020.3.6
- 增加在线编译模块（仅支持java）

2020.3.6
- 将所有的css、js改为cdn加速，加速网站访问速度

2020.3.5
- 修改下载逻辑，不利用应用服务器进行转存，节省应用服务器流量并且提高大文件下载响应度


