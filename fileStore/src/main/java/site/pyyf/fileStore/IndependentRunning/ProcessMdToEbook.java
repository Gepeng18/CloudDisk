package site.pyyf.fileStore.IndependentRunning;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class ProcessMdToEbook {
    private static OssUpload upload =OssUpload.getInstance();
    private static ReplacePathInMardown pathToOSS = new ReplacePathInMardown();

    private static void replacePath(String ossPath,String suffix, String filePath) throws Exception {
        String fileName = filePath.split("\\\\")[filePath.split("\\\\").length - 1];
        String fileNameWithoutSuffix = fileName.split("\\.")[0];
        String dst = ossPath + suffix +  "/"+fileNameWithoutSuffix ;
        pathToOSS.replace(filePath, dst);
    }

    private static void uploadDir(String suffix,String filePath){
        String dstImgsPath = StringUtils.substringBeforeLast(filePath, "\\") + "\\imgs\\" +
                StringUtils.substringAfterLast(StringUtils.substringBeforeLast(filePath, "."), "\\") ;
        String fileName = filePath.split("\\\\")[filePath.split("\\\\").length - 1];
        String fileNameWithoutSuffix = fileName.split("\\.")[0];
        File file = new File(dstImgsPath);
        for (File listFile : file.listFiles()) {
            upload.upload(suffix+"/"+fileNameWithoutSuffix,listFile);
        }
    }


    public static void main(String[] args) throws Exception {
        String filePath = "F:\\Projects\\Java\\CloudDisk\\docs\\部署.md";
        String suffix = "cloudDisk/ebook";
        String ossPath = "https://pyyf.oss-cn-hangzhou.aliyuncs.com/";

        replacePath(ossPath,suffix,filePath);
        uploadDir(suffix,filePath);
    }
}
