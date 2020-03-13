package site.pyyf.cloudpan.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class processMd2Ebook {
    private static OssUpload upload ;
    static Path2Oss md2oss;

    static {
        upload = OssUpload.getInstance();
        md2oss = new Path2Oss();
    }
    public static void main(String[] args) throws Exception {

        String filePath = "F:\\OneDrive\\笔记\\项目\\community.md";
        String imgsPath = StringUtils.substringBeforeLast(filePath, "\\") + "\\imgs\\" +
                StringUtils.substringAfterLast(StringUtils.substringBeforeLast(filePath, "."), "\\") ;
        replacePath(filePath);
        uploadDir(imgsPath);
    }

    public static void uploadDir(String imgsPath){

        File file = new File(imgsPath);
        for (File listFile : file.listFiles()) {
            upload.upload("ebook",listFile);
        }
    }

    public static void replacePath(String filePath) throws Exception {

        String fileName = filePath.split("\\\\")[filePath.split("\\\\").length - 1];
        String fileNameWithoutSuffix = fileName.split("\\.")[0];
        String src = ".\\imgs\\" + fileNameWithoutSuffix + "\\";
        String dst = "https://pyyf.oss-cn-hangzhou.aliyuncs.com/ebook/" + fileNameWithoutSuffix + "\\";
        md2oss.replace(filePath, src, dst);
    }
}
