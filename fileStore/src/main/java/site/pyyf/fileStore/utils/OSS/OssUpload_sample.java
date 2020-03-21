package site.pyyf.fileStore.utils.OSS;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.pyyf.fileStore.entity.UploadResult;

import java.io.File;
import java.io.FileInputStream;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
class AliyunConfi_sample {

    private String endpoint = "";
    private String accessKeyId ="";
    private String accessKeySecret="";
    private String bucketName="";
    private String urlPrefix="";

    public OSS oSSClient() {
        return new OSSClient(endpoint, accessKeyId, accessKeySecret);
    }

}

public class OssUpload_sample {

    private static final Logger logger= LoggerFactory.getLogger(OssUpload.class);
    private OSS ossClient;
    private AliyunConfigg aliyunConfig;
    //创建 SingleObject 的一个对象
    private static OssUpload_sample instance = new OssUpload_sample();

    //让构造函数为 private，这样该类就不会被实例化
    private OssUpload_sample(){
        aliyunConfig = new AliyunConfigg();
        ossClient = aliyunConfig.oSSClient();
    }

    //获取唯一可用的对象
    public static OssUpload_sample getInstance(){
        return instance;
    }

    // 允许上传的格式
    private static String[] SUP_TYPE = new String[]{
            "md", "java", "css", "cpp", "py", "php", "html",
            "bmp", "jpg", "jpeg", "gif", "png",
            "mp4", "wmv", "flv",
            "mp3", "wma"};

    private String getFilePath(String fileName, String suffix) {

        return suffix + "/" + fileName;
    }


    public UploadResult upload(String suffix,File file) {
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

        String remotePath = getFilePath(fileName, suffix);
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


}
