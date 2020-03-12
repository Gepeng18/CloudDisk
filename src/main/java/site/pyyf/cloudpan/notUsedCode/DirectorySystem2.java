package site.pyyf.cloudpan.notUsedCode;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
class FileNode
{
    boolean isFile;
    private Map<String,FileNode> subNodes = new HashMap<>();

    // 添加子节点
    public void addSubNode(String fileName, FileNode node) {
        subNodes.put(fileName, node);
    }

    public FileNode(boolean isFile) {
        this.isFile = isFile;
    }

    // 获取子节点
    public FileNode getSubNode(String fileName) {
        return subNodes.get(fileName);
    }

    // 获取子节点
    public Map<String,FileNode> getAllNode() {
        return subNodes;
    }
}


/*
 *
 *
 */
public class DirectorySystem2//存储指定文件夹所有文件名的 树类
{
    private FileNode root=new FileNode(false);//树根（相当于链表的头指针）
    public FileNode getRoot()//获取树根
    {
        return root;
    }
    /*
     * 函数名：getFile
     * 作用：实现将指定文件夹的所有文件存入树中
     */
    public void getFile(String path,FileNode fileNode)
    {
        File file=new File(path);
        File[] array=file.listFiles();
        FileNode newNode=null;
        for(int i=0;i<array.length;i++)
        {
            newNode = new FileNode(array[i].isFile());
            fileNode.addSubNode(array[i].getName(),newNode);

            if (array[i].isDirectory())
            {
                getFile(array[i].getPath(), newNode);
            }
        }
    }


    /*
     * 函数名：printTree
     * 作用：输出树中的内容
     */
    public void printTree(FileNode node,int deep)
    {
        for (Map.Entry<String, FileNode> stringFileNodeEntry : node.getAllNode().entrySet()) {
            for (int j = 0; j < deep; j++)//输出前置空格
                System.out.print("--");
            System.out.println(stringFileNodeEntry.getKey());

            //画一下图，就能理解这个两个if
            if (!stringFileNodeEntry.getValue().isFile)
                printTree(stringFileNodeEntry.getValue(), deep+1);

        }



    }


    public static void main(String[] args)
    {
        DirectorySystem2 test=new DirectorySystem2();
        test.getFile("F:\\Projects\\Java\\java300\\target\\classes",test.getRoot());
        test.printTree(test.root, 0);
    }
}

