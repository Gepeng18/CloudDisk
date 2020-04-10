package site.pyyf.fileStore.IndependentRunning;

import org.apache.commons.lang3.StringUtils;

import javax.xml.crypto.Data;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProcessMdToEbook {
    private static OssUpload upload =OssUpload.getInstance();
    private static ReplacePathInMardown pathToOSS = new ReplacePathInMardown();

    private static void replacePath( String ossPath, String ossSuffix,String formatDate,String filePath,boolean delete) throws Exception {
        String fileName = filePath.split("\\\\")[filePath.split("\\\\").length - 1];
        String fileNameWithoutSuffix = fileName.split("\\.")[0];
        String dst = ossPath + "/"+ossSuffix +  "/" + fileNameWithoutSuffix  +  "/"+ formatDate;
        pathToOSS.replace(filePath, dst,delete);
    }

    private static void uploadDir(String ossSuffix,String formatDate,String filePath){
        String dstImgsPath = StringUtils.substringBeforeLast(filePath, "\\") + "\\img\\" +
                StringUtils.substringAfterLast(StringUtils.substringBeforeLast(filePath, "."), "\\") ;
        String fileName = filePath.split("\\\\")[filePath.split("\\\\").length - 1];
        String fileNameWithoutSuffix = fileName.split("\\.")[0];
        File file = new File(dstImgsPath);
        if(file.listFiles()==null){
            System.out.println("没有产生图片文件");
            return;
        }
        for (File listFile : file.listFiles()) {
            upload.upload(ossSuffix+"/"+ fileNameWithoutSuffix+"/"+formatDate,listFile);
        }
    }


    public static void main(String[] args) throws Exception {

        String filePath = "G:\\OneDrive\\笔记\\JAVA\\JVM--内存分区.md";

        String ossSuffix = "post/img";
        String formatDate = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss").format(new Date());
        String ossPath = "https://pyyf.oss-cn-hangzhou.aliyuncs.com";

        replacePath(ossPath,ossSuffix,formatDate,filePath,false);
        uploadDir(ossSuffix,formatDate,filePath);

    }
}
