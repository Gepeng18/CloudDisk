package site.pyyf.cloudpan.无用的代码;
import java.io.File;

class TreeNode
{
    String data;
    TreeNode firstChild;
    TreeNode nextSibling;
    public TreeNode(String data, TreeNode  firstChild,
                    TreeNode nextSibling) {
        super();
        this.data = data;
        this.firstChild = firstChild;
        this.nextSibling = nextSibling;
    }
}


/*
 *
 *
 */
public class DirectorySystem//存储指定文件夹所有文件名的 树类
{
    private TreeNode root=new TreeNode(null, null, null);//树根（相当于链表的头指针）
    public TreeNode getRoot()//获取树根
    {
        return root;
    }
    /*
     * 函数名：getFile
     * 作用：实现将指定文件夹的所有文件存入树中
     */
    public void getFile(String path,TreeNode treeNode)
    {
        File file=new File(path);
        File[] array=file.listFiles();
        TreeNode newNode=null;
        for(int i=0;i<array.length;i++)
        {
            newNode=new TreeNode(array[i].getName(),null, null);
            //判断当前节点有没有firstChild，没有的话为其添加一个
            if (treeNode.firstChild==null)
            {
                if (array[i].isFile())
                    treeNode.firstChild=newNode;
                if (array[i].isDirectory())
                {
                    treeNode.firstChild=newNode;
                    getFile(array[i].getPath(), newNode);
                }
            }
            //当前节点已经存在firstChild，所以后面的都是firstChild节点的兄弟
            else
            {
                TreeNode p=treeNode.firstChild;
                while(p.nextSibling!=null)
                    p=p.nextSibling;
                if (array[i].isFile())
                    p.nextSibling=newNode;
                if (array[i].isDirectory())
                {
                    p.nextSibling=newNode;
                    getFile(array[i].getPath(), newNode);
                }
            }
        }
    }


    /*
     * 函数名：printTree
     * 作用：输出树中的内容
     */
    public void printTree(TreeNode root,int deep)
    {
        if (root.data!=null) //这个if是为了区分root节点，因为root节点data=null
        {
            for (int i = 0; i < deep; i++)//输出前置空格
                System.out.print("  ");
            System.out.println(root.data);
        }
        //画一下图，就能理解这个两个if
        if (root.firstChild!=null)
            printTree(root.firstChild, deep+1);
        if (root.nextSibling!=null)
            printTree(root.nextSibling, deep);

    }


    public static void main(String[] args)
    {
        DirectorySystem test=new DirectorySystem();
        test.getFile("/Users/XXY/Desktop/test",test.getRoot());
        test.printTree(test.root, 0);
    }
}

