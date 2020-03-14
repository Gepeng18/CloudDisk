package site.pyyf.cloudDisk.service;

import com.alibaba.fastjson.JSON;
import site.pyyf.cloudDisk.entity.Directory;
import site.pyyf.cloudDisk.entity.Ebook;
import site.pyyf.cloudDisk.entity.EbookConent;
import site.pyyf.cloudDisk.utils.FtpUtil;

import java.io.*;
import java.util.UUID;

public interface IResolveHeaderService {

    public boolean isNHeader(String buffer, int n);


    /*
     * 函数名：getFile
     * 作用：实现将指定文件夹的所有文件存入树中
     */
    public void readFile(InputStream in, String ebookName, int id) throws Exception;

    public void readFile(String remotePath, String fileName, int id) throws Exception;

    /*
     * 函数名：printTree
     * 作用：输出树中的内容
     */
    public void printTree(Directory node, int deep);

    public void printTree(Directory node);

    public void printTree();


}
