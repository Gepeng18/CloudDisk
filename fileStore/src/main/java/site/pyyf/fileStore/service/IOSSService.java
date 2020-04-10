package site.pyyf.fileStore.service;


import site.pyyf.fileStore.entity.UploadResult;

import java.io.*;

public interface IOSSService {


    public UploadResult upload( String suffix,File file);

    public UploadResult upload(InputStream inputStream, String fileName, String suffix);


    public void download(String absolutePath, OutputStream out);

    public UploadResult transfer(String srcPath, String dstSuffix);


    public boolean delete(String srcPath);

}





