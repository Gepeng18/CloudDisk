package site.pyyf.fileStore.service;

import site.pyyf.fileStore.entity.Header;

import java.io.*;

public interface IResolveHeaderService {

    public boolean isNHeader(String buffer, int n);


    /*
     * 函数名：getFile
     * 作用：实现将指定文件夹的所有文件存入树中
     */
    public void readFile(InputStream in, String ebookName, int id) throws Exception;

    public void readFile(String remotePath, String fileName, int fileId) throws Exception;

    /*
     * 函数名：printTree
     * 作用：输出树中的内容
     */
    public void printTree(Header node, int deep);

    public void printTree(Header node);

    public void printTree();


}
