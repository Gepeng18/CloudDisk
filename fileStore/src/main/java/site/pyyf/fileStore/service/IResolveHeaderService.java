package site.pyyf.fileStore.service;

import site.pyyf.fileStore.entity.Header;

import java.io.*;

public interface IResolveHeaderService {

    boolean isNHeader(String buffer, int n);


    /*
     * 函数名：getFile
     * 作用：实现将指定文件夹的所有文件存入树中
     */
    void readFile(InputStream in, String ebookName, int id) throws Exception;

    void readFile(String remotePath, String fileName, int fileId) throws Exception;

    /*
     * 函数名：printTree
     * 作用：输出树中的内容
     */
    void printTree(Header node, int deep);

    void printTree(Header node);

    void printTree();


}
