package site.pyyf.fileStore.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.pyyf.fileStore.mapper.IebookContentMapper;
import site.pyyf.fileStore.entity.EbookConent;
import site.pyyf.fileStore.service.IEbookContentService;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

//import site.pyyf.dao.IebookMapper;

@Service
public class EbookContentService implements IEbookContentService {
private static final Logger logger= LoggerFactory.getLogger(EbookContentService.class);
    // 代码缓存
    private Cache<String, Object> markdownCache;

    @Value("${caffeine.markdown.max-size}")
    private int maxSize;

    @Value("${caffeine.markdown.expire-seconds}")
    private int expireSeconds;

    @PostConstruct
    public void init() {
        // 初始化帖子列表缓存
        markdownCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build();
    }

<<<<<<< HEAD
=======

>>>>>>> 92c711a204ae08a936e2073da2b82198610d3895
    @Autowired
    public IebookContentMapper iebookContentMapper;

    public String selectContentByContentId(String contentId){
        //这里引入caffeine在内存中存储文件内容
        String caffeineKey = "markdown:"+contentId;
        // 根据key查询一个缓存，如果没有返回NULL
        String cacheContent = (String)markdownCache.getIfPresent(caffeineKey);
        if(cacheContent!=null){
            logger.info("查询markdown内容时,caffeine缓存击中");
            return cacheContent;
        }
        logger.info("查询markdown内容时,caffeine缓存未击中，从数据库读取");
        cacheContent = iebookContentMapper.selectContentByContentId(contentId);
        markdownCache.put(caffeineKey, cacheContent);
        return cacheContent;
    }

<<<<<<< HEAD
=======
    public void insertEbookContent(EbookConent ebookConent){
        iebookContentMapper.insertEbookContent(ebookConent);
    }

>>>>>>> 92c711a204ae08a936e2073da2b82198610d3895
    public void updateContentByContentId(String contentId,String content){
        logger.info("内容改变，修改缓存内容");
        String caffeineKey = "markdown:"+contentId;
        markdownCache.put(caffeineKey,content);
        iebookContentMapper.updateContentByContentId(contentId,content);
    }

    public int selectEbookIdByContentId(String contentId) {
        return iebookContentMapper.selectEbookIdByContentId(contentId);
    }

    public void deleteByBookId(int bookId) {
       iebookContentMapper.deleteByBookId(bookId);
    }
}


