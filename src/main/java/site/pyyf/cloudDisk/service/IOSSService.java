package site.pyyf.cloudDisk.service;


import org.springframework.stereotype.Service;
import site.pyyf.cloudDisk.entity.UploadResult;

import java.io.*;

public interface IOSSService {


    public UploadResult upload(File file, String suffix);

    public UploadResult upload(InputStream inputStream, String fileName, String suffix);


    public void download(String absolutePath, OutputStream out);

    public UploadResult transfer(String srcPath, String dstSuffix);


    public boolean delete(String srcPath);

}





