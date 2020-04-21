package site.pyyf.fileStore.service;


import site.pyyf.fileStore.entity.UploadResult;

import java.io.*;

public interface IOSSService {

    UploadResult upload( String suffix,File file);

    UploadResult upload(InputStream inputStream, String fileName, String suffix);

    void download(String absolutePath, OutputStream out);

    UploadResult transfer(String srcPath, String dstSuffix);

    boolean delete(String srcPath);

}





