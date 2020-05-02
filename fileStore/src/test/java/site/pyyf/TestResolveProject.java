package site.pyyf;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import site.pyyf.fileStore.FileStoreApplication;
import site.pyyf.fileStore.entity.Ebook;
import site.pyyf.fileStore.entity.EbookContent;
import site.pyyf.fileStore.entity.Header;
import site.pyyf.fileStore.entity.MyFile;
import site.pyyf.fileStore.mapper.IEbookContentMapper;
import site.pyyf.fileStore.service.IEbookService;
import site.pyyf.fileStore.service.IFileStoreService;
import site.pyyf.fileStore.service.IMyFileService;
import site.pyyf.fileStore.service.impl.ResolveHeaderServiceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = FileStoreApplication.class)
public class TestResolveProject {

    private static Logger logger = LoggerFactory.getLogger(ResolveHeaderServiceImpl.class);

    @Autowired
    private IEbookContentMapper iebookContentMapper;

    @Autowired
    private IEbookService iEbooksService;

    @Autowired
    private IMyFileService iMyFileService;

    @Test
    public void testResolveProj() throws Exception {
        String projectPath = "F:\\Projects\\Java\\CloudDisk\\compiler\\src\\main\\java\\site\\pyyf\\compiler";
        String projectName = new File(projectPath).getName()+".proj";

        MyFile file = MyFile.builder()
                .myFileName(projectName)
                .type(1)
                .parentFolderId(17)
                .size((int)(new File(projectPath).length()/1024))
                .postfix("proj")
                .uploadTime(new Date())
                .build();
        iMyFileService.insert(file);

        List<EbookContent> allContent = new ArrayList<>();
        Header root = new Header();
        resolveDir(projectPath,allContent,root,file.getId());
        iebookContentMapper.insertAllEbookContent(allContent);
        iEbooksService.insert(Ebook.builder().ebookName(projectName).fileId(file.getId()).header(JSON.toJSONString(root)).build());
        System.out.println();
    }


    /***
     * Created by "gepeng" on 2020-05-02 19:37:22.
     * @Description 读取文件夹，将这个文件夹里的文件和文件夹都挂到header下
     * @param [dirPath 文件夹路径,
     *        allContent 存储容器,
     *        header 存储容器,
     *        projectId 工程ID]
     * @return
     */

    public void resolveDir(String dirPath, List allContent, Header header,int projectId){
        File[] files = new File(dirPath).listFiles();
        if(files.length>0)
            header.setHasSub(true);

        for(File file:files){
            /* ------------------- 这个文件或者文件夹进入head ----------------- */
            String contentId = UUID.randomUUID().toString().replaceAll("-", "");
            Header thisFile = new Header();
            thisFile.setHeader(file.getName());
            thisFile.setContentId(contentId);
            header.addSubNode(thisFile);

            if(file.isFile()){
                /* ------------------- 这个文件进入content ----------------- */
                EbookContent ebookConent = new EbookContent();
                ebookConent.setContentId(contentId);
                ebookConent.setFileId(projectId);
                ebookConent.setContent(readFile(file.getAbsolutePath()));
                allContent.add(ebookConent);
                thisFile.setHasSub(false);
            }else{
                //是文件夹
                resolveDir(file.getAbsolutePath(),allContent,thisFile,projectId);
            }
        }
    }

    public String readFile(String filePath){
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(filePath));
            StringBuilder content = new StringBuilder();
            String buffer = null;
            while((buffer=bfr.readLine())!=null)
                content.append(buffer).append("\n");
            return content.toString();
        }catch (Exception e){
            logger.error("读取工程项目时，读取文件出错");
            return "";
        }
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

}