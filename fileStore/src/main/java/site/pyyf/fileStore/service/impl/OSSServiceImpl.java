package site.pyyf.fileStore.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.GenericRequest;
import com.aliyun.oss.model.OSSObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pyyf.fileStore.config.AliyunConfig;
import site.pyyf.fileStore.entity.UploadResult;
import site.pyyf.fileStore.service.IOSSService;

import java.io.*;
import java.util.UUID;


@Service
public class OSSServiceImpl implements IOSSService {
    private static final Logger logger = LoggerFactory.getLogger(OSSServiceImpl.class);
    @Autowired
    private OSS ossClient;

    @Autowired
    private AliyunConfig aliyunConfig;

    // 允许上传的格式
    private static String[] SUP_TYPE = new String[]{
            "md", "java", "css", "cpp", "py", "php", "html",
            "bmp", "jpg", "jpeg", "gif", "png",
            "mp4", "wmv", "flv",
            "mp3", "wma","flac"};

    private String getFilePath( String suffix,String fileName) {

        return suffix + "/" + fileName;
    }

    public UploadResult upload(File file, String suffix) {
        String fileName = file.getName();
        // 校验文件格式
        boolean isLegal = false;
        for (String type : SUP_TYPE) {
            if (StringUtils.substringAfterLast(fileName, ".").equals(type)) {

                isLegal = true;
                break;
            }
        }
        // 封装Result对象，并且将文件的byte数组放置到result对象中
        UploadResult fileUploadResult = new UploadResult();
        if (!isLegal) {
            fileUploadResult.setStatus("error");
            logger.error("上传OSS时后缀名不匹配");
            return fileUploadResult;
        }

        String remotePath = getFilePath(suffix, fileName);
        // 上传到阿里云
        try {
            ossClient.putObject(aliyunConfig.getBucketName(), remotePath, new FileInputStream(file));
            logger.info("上传OSS成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传OSS失败");

            //上传失败
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }
        fileUploadResult.setStatus("done");
        fileUploadResult.setUrl(this.aliyunConfig.getUrlPrefix() + remotePath);
        return fileUploadResult;
    }

    public UploadResult upload(InputStream inputStream, String fileName, String suffix) {
        // 校验文件格式
        boolean isLegal = false;
        for (String type : SUP_TYPE) {
            if (StringUtils.substringAfterLast(fileName, ".").equals(type)) {
                isLegal = true;
                break;
            }
        }
        // 封装Result对象，并且将文件的byte数组放置到result对象中
        UploadResult fileUploadResult = new UploadResult();
        if (!isLegal) {
            logger.error("上传OSS时后缀名不匹配");
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }

        String remotePath = getFilePath(suffix, fileName);
        // 上传到阿里云
        try {
            ossClient.putObject(aliyunConfig.getBucketName(), remotePath, inputStream);
            logger.info("上传OSS成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("上传OSS失败");
            //上传失败
            fileUploadResult.setStatus("error");
            return fileUploadResult;
        }
        fileUploadResult.setStatus("done");
        fileUploadResult.setUrl(this.aliyunConfig.getUrlPrefix() + remotePath);
        return fileUploadResult;
    }


    public void download(String absolutePath, OutputStream out) {
        final String bucketName = aliyunConfig.getBucketName();
        final OSS oss = aliyunConfig.oSSClient();
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = oss.getObject(bucketName, absolutePath);
        BufferedInputStream in = null;
        try {
            // 读取文件内容。
            in = new BufferedInputStream(ossObject.getObjectContent());
            byte[] buffer = new byte[1024];
            int len = 0;
            int i = 0;
            while ((len = in.read(buffer)) > 0) {
                i = i + len;
                out.write(buffer, 0, len);
            }
            logger.info("下载OSS成功");
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            in.close();
        } catch (Exception e) {
            logger.info("下载OSS失败");
            e.printStackTrace();
        }

    }

    public UploadResult transfer(String srcPath, String dstSuffix) {
        if (!new File("data/temp").exists())
            new File("data/temp").mkdirs();
        File tmpFile = new File("data/temp/" + UUID.randomUUID().toString() + "." + StringUtils.substringAfterLast(srcPath, "."));

        try {
            download(srcPath, new FileOutputStream(tmpFile));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("OSS转存过程中下载失败");
            return new UploadResult("", "error");
        }
        return upload(tmpFile, dstSuffix);


    }


    public boolean delete(String srcPath) {
        String bucketName = aliyunConfig.getBucketName();
        String fileName = StringUtils.substringAfterLast(srcPath, "/");

        if (bucketName == null || fileName == null) {
            logger.error("OSS所删除的文件不存在！");
            return false;
        }
        try {
            GenericRequest request = new DeleteObjectsRequest(bucketName).withKey(srcPath); //srcPath = "cloudDisk/Audio/test.html"
            ossClient.deleteObject(request);
        } catch (Exception oe) {
            oe.printStackTrace();
            return false;
        }
        return true;
    }

}

//    public static void main(String[] args) throws FileNotFoundException {
//        String a = "cloudDisk/imgs/img/14df6b3f040a47abb957e650cde4d028tibet-9.jpg";
//        OutputStream outputStream = new FileOutputStream("F:\\Projects\\Java\\Community\\community\\src\\main\\java\\com\\nowcoder\\community\\service\\1.jpg");
//        final AliyunConfigg aliyunConfig = new AliyunConfigg();
//        final OSSService iossService = new OSSService();
//        iossService.aliyunConfig = aliyunConfig;
//        iossService.download(a,outputStream,iossService.aliyunConfig.getBucketName());
//    }




