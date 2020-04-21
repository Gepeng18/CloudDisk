## markdown转化代码
- 本模块对使用typora编写的markdown文件进行转化。
- 本模块是独立于web项目的，当web项目启动后，如想要上传markdown文件，必须先手动执行`ProcessMdToEbook.java`文件对md文件进行转化，然后在前端进行上传转化后的md文件。
    - 原因：当使用typora进行书写markdown代码时，插入的图片地址通常是```![image-20200322185439920](https://pyyf.oss-cn-hangzhou.aliyuncs.com/post/img/readme/2020/04/21/20/31/08/image-20200322185439920.png)```,
    所以我们需要先将图片进行上传到OSS，再将md中的图片地址改为OSS的地址
    - 这里由于作者水平有限，无法整合到web中，小伙伴们可以做成一个桌面软件或者整合到web中使markdown能在前端进行直接上传。

- OssUpload.java                                       将一个文件夹中的所有图片上传到OSS中，这里采用阿里云OSS
- MultiThreadUploadUtil.java                  同OssUpload.java ，只不过采用多线程进行上传，加快上传速度
- ReplacePathInMardown.java                将markdown中所有的路径进行替换
- ProcessMdToEbook.java                        整合路径修改模块和图片上传模块

