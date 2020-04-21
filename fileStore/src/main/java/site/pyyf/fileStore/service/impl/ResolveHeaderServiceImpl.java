package site.pyyf.fileStore.service.impl;

/**
 * Created by "gepeng" on 2020-02-58 03:42:10.
 * 从markdown文件中提取标题
 * <p>
 * 检测本行以#开头时，下一行至下一个#截止中间的内容保存起来。
 * 保存过程中，当检测到```时，不检测，直至出现```则重新开始检测#
 */

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import site.pyyf.fileStore.entity.Ebook;
import site.pyyf.fileStore.entity.EbookContent;
import site.pyyf.fileStore.entity.Header;
import site.pyyf.fileStore.mapper.IEbookContentMapper;
import site.pyyf.fileStore.service.IEbookService;
import site.pyyf.fileStore.service.IResolveHeaderService;
import site.pyyf.fileStore.utils.FtpUtil;

import java.io.*;
import java.util.*;


@Service
@Scope("prototype")
public class ResolveHeaderServiceImpl implements IResolveHeaderService//存储指定文件夹所有文件名的 树类
{
    private static Logger logger = LoggerFactory.getLogger(ResolveHeaderServiceImpl.class);

    @Autowired
    private IEbookContentMapper iebookContentMapper;

    @Autowired
    private IEbookService iEbooksService;

    private List<EbookContent> allContent = new ArrayList<>();
    private boolean jugleFirstLevelHeader = false;
    private int fileId;
    private int firstLevelHeader;
    private String preContentId;
    private StringBuilder preContent = new StringBuilder();
    private Header root;//树根（相当于链表的头指针）
    private boolean detect = true;
    private Map<String, Header> records = new HashMap<>();


    private void insertHeader(Header head, int level) {
        if (level == firstLevelHeader) {
            if (records.containsKey("" + level))
                records.remove("" + level);
            /* ------------------- 更新记录和插入节点 ----------------- */
            root.addSubNode(head);
            root.setHasSub(true);
            records.put("" + level, head);
            return;
        }

        /* ------------------- 找到要插入的节点 ----------------- */
        int preLevel = 0;
        for (preLevel = level - 1; preLevel > 0; preLevel--) {
            if (records.containsKey("" + preLevel))
                break;
        }

        /* ------------------- 后面节点全部失效 ----------------- */
        for (int i = preLevel + 1; i < 7; i++) {
            if (records.containsKey("" + i))
                records.remove("" + i);
        }

        /* ------------------- 更新记录和插入节点 ----------------- */
        Header preList = records.get("" + preLevel);
        preList.addSubNode(head);
        preList.setHasSub(true);
        records.put("" + level, head);
    }

    public ResolveHeaderServiceImpl() {
        root = new Header();//树根（相当于链表的头指针）
    }

    public boolean isNHeader(String buffer, int n) {


        String trimed = buffer.trim();
        try {
            return (trimed.charAt(n - 1) == '#') && (trimed.charAt(n) != '#');
        } catch (IndexOutOfBoundsException e) {
            //超过范围了，表明第n个数没有，则肯定不是n级标题
            return false;
        }

    }

    public void resolveHeader(String buffer) {
        boolean isHeaderLine = false;
        if (detect) {

            /* ------------------- 判断第一次出现的标题是几级标题 ----------------- */
            if (!jugleFirstLevelHeader) {
                for (int i = 1; i < 7; i++) {
                    if (isNHeader(buffer, i)) {
                        firstLevelHeader = i;
                        jugleFirstLevelHeader = true;
                        break;
                    }
                }
            }

            for (int i = 1; i < firstLevelHeader; i++) {
                if (isNHeader(buffer, i)) {
                    throw new RuntimeException("出现了更高的标题");
                }
            }


            //一级目录直接加到根上去
            if (isNHeader(buffer, firstLevelHeader)) {
                isHeaderLine = true;

                /* ------------------- 第2次检测到标题时对其上面所有的内容进行封存 ----------------- */
                if (root.getSubNodes().size() > 0) {
                    /* ------------------- 插入内容表(插入上一次的） ----------------- */
                    String content = preContent.toString();
                    EbookContent ebookConent = new EbookContent();
                    ebookConent.setContentId(preContentId);
                    ebookConent.setFileId(fileId);
                    ebookConent.setContent(content);
                    allContent.add(ebookConent);


                    preContent = new StringBuilder();
                }

                /* ------------------- 标题加入root队伍 ----------------- */
                Header newDir = new Header();
                String contentId = UUID.randomUUID().toString().replaceAll("-", "");
                newDir.setHeader(buffer.substring(firstLevelHeader));
                newDir.setContentId(contentId);

                insertHeader(newDir, firstLevelHeader);
                preContentId = contentId;
            }

            /* ------------------- 处理除firstLevel级标题外的其他标题 ----------------- */
            for (int i = firstLevelHeader + 1; i < 7; i++) {
                if (isNHeader(buffer, i)) {
                    isHeaderLine = true;

                    /* -------------------  插入内容表(插入上一次的）----------------- */
                    String content = preContent.toString();
                    EbookContent ebookConent = new EbookContent();
                    ebookConent.setContentId(preContentId);
                    ebookConent.setContent(content);
                    ebookConent.setFileId(fileId);
                    allContent.add(ebookConent);
                    preContent = new StringBuilder();

                    /* ------------------- 标题加入root队伍 ----------------- */
                    String contentId = UUID.randomUUID().toString().replaceAll("-", "");
                    Header newDir = new Header();
                    newDir.setHeader(buffer.substring(i));
                    newDir.setContentId(contentId);
                    preContentId = contentId;
                    insertHeader(newDir, i);
                    break;
                }
            }
        }

        if (buffer.contains("```"))
            detect = !detect;
        if (!isHeaderLine)
            preContent.append(buffer).append("\n");
    }


    /*
     * 函数名：getFile
     * 作用：实现将指定文件夹的所有文件存入树中
     */
    public void readFile(InputStream in, String ebookName, int fileId) throws Exception {
        this.fileId = fileId;
        logger.info("开始处理markdown文件");
        Ebook eBook = new Ebook();
        eBook.setFileId(this.fileId);
        eBook.setEbookName(ebookName);

        BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
        String buffer = null;
        while ((buffer = bfr.readLine()) != null) {
            resolveHeader(buffer);
        }


        /* ------------------- 插入内容表 ----------------- */
        String content = preContent.toString();
        EbookContent ebookConent = new EbookContent();
        ebookConent.setContentId(preContentId);
        ebookConent.setContent(content);
        ebookConent.setFileId(this.fileId);
        allContent.add(ebookConent);
        iebookContentMapper.insertAllEbookContent(allContent);

        /* ------------------- 将标题的所有内容插入标题表中 ----------------- */
        eBook.setHeader(JSON.toJSONString(root));
        iEbooksService.insert(eBook);

        logger.info("markdown文件处理完毕");

    }


    public void readFile(String remotePath, String fileName, int fileId) throws Exception {
        if(!new File("data/temp").exists())
            new File("data/temp").mkdirs();
        String tmpFilePath = "data/temp/"+UUID.randomUUID().toString().replaceAll("-", "");
        FileOutputStream fileOutputStream = new FileOutputStream(tmpFilePath);
        FtpUtil.downloadFile("/"+remotePath, fileOutputStream);
        FileInputStream fileInputStream = new FileInputStream(tmpFilePath);
        readFile(fileInputStream, fileName, fileId);
        new File(tmpFilePath).delete();
        fileInputStream.close();
        fileOutputStream.close();
    }

    /*
     * 函数名：printTree
     * 作用：输出树中的内容
     */
    public void printTree(Header node, int deep) {
        for (Header directory : node.getSubNodes()) {
            for (int j = 0; j < deep; j++)//输出前置空格
                System.out.print("       ");
            System.out.println(directory.getContentId());
            System.out.println(directory.getHeader());
            printTree(directory, deep + 1);
        }
    }

    public void printTree(Header node) {
        printTree(node, 0);
    }

    public void printTree() {
        printTree(root, 0);
    }


}

