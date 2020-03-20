
本项目基于[莫提网盘](https://github.com/373675032/moti-cloud) 进行二次开发
- 增加在线预览功能，主要针对代码文件进行在线预览
- java代码提供在线编译系统，在线编译后台已完成，并且支持多个类相互调用
- 提供markdown自动分页功能，使其易于在浏览器上进行浏览（需要后台上传文件，此处由于前端不会写，没办法）
- 支持视频在线播放
- 支持音乐在线播放
- 支持图片在线预览



使用：
请修改所有sample文件后进行部署



2020.3.20
- 使用dubbo，因为在线编译模块会受到lombok的影响导致编译失败，所以cloudDisk主模块作为consumer依旧使用lombok，而compiler作为provider提供服务，
此模块中不允许使用lombok，否则多个类编译时会出错

2020.3.19 
- 优化在线编译器，使在线编译模块可以实现多个类的相互调用
2020.3.18
- 增加在线编译器

2020.3.17
- 修改前端样式和结构
- 修改前端bug,使得视频可以切换

2020.3.16
- 支持FTP和OSS保存数据，其中考虑到OSS价格问题，大文件都采用FTP存储，小文件采用OSS存储，可以在application.yml中进行配置
- 注意：本项目所有的图片皆采用OSS，大的文件和普通文件皆采用FTP存储

2020.3.12
- 支持各种图片在线预览
- 重新构建后端代码，将代码预览和视频播放，音乐播放，图片预览的controller合而为一

2020.3.12
- 支持多种音乐和视频在线播放
- 其实还是只支持mp3和MP4格式的文件，然而当用户每上传一个音乐或者视频文件后，后端会转为mp3格式和mp4格式（转化过程为了加快速度和节省带宽
和服务器压力，采取压缩策略，如果想不压缩可修改videoTransfer和audioTransfer中的相关参数。当用户上传一个音乐或视频文件后，在文件列表看
到了这个文件则表明转换完成，不显示则表示正在转换

2020.3.12
- 修改前端，音乐可以切换

2020.3.11
- 支持音乐和视频在线播放
- 目前只支持mp3和mp4格式的文件，其他格式文件不支持

2020.3.9
- 增加分享功能，支持文件夹分享和文件分享，但是是文件转储，所以比较耗费时间
- 前端进行了改进，将上传文件模块和主文件模块进行整合


2020.3.8
- markdown的内容分解后批量进行上传，同时云OSS存储采用多线程进行上传
- 前端进行了改进，预览代码支持更多的语言且显示括号和行号

2020.3.7
- 引入QQ登录模块   输入任意页面（包含项目前缀）被LoginHandlerInterceptor重定向到/路径，MvcConfig将/映射为index登录页面，然后点击/login后，重定向new Oauth().getAuthorizeURL(request)，这里即根据qqconnectconfig中的信息请求QQ互联，QQ互联将QQ信息回调给redirect_URI（即QQ互联回调域及qqconnectconfig中的redirect_URI），所以redirect_URI不能被拦截，且这里使用controller进行接收，至此登录完成

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







## 界面展示

##### 完整页面

![](https://github.com/Gepeng18/CloudDisk/blob/master/imgs/总界面.png)



##### 在线预览

![](https://github.com/Gepeng18/CloudDisk/blob/master/imgs/音乐播放1.png)

##### 音乐播放

![](https://github.com/Gepeng18/CloudDisk/blob/master/imgs/音乐播放2.png)

##### 代码预览

![](https://github.com/Gepeng18/CloudDisk/blob/master/imgs/代码展示.png)

##### 图片预览

![](https://github.com/Gepeng18/CloudDisk/blob/master/imgs/图片预览.png)

##### 图片播放

![](https://github.com/Gepeng18/CloudDisk/blob/master/imgs/图片浏览.png)

##### 视频播放

![](https://github.com/Gepeng18/CloudDisk/blob/master/imgs/视频播放.png)