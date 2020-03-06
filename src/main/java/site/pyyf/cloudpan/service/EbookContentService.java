package site.pyyf.cloudpan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pyyf.cloudpan.mapper.IebookContentMapper;
import site.pyyf.cloudpan.entity.EbookConent;

//import site.pyyf.dao.IebookMapper;

@Service
public class EbookContentService {

    @Autowired
    public IebookContentMapper iebookContentMapper;
    public String selectContentByContentId(String contentId){
        return iebookContentMapper.selectContentByContentId(contentId);
    }

    public void insertEbookContent(EbookConent ebookConent){
        iebookContentMapper.insertEbookContent(ebookConent);
    }


    public void updateContentByContentId(String contentId,String content){
        iebookContentMapper.updateContentByContentId(contentId,content);
    }

    public EbookConent selectByContentId(String contentId) {
        return iebookContentMapper.selectByContentId(contentId);
    }


    public void deleteByBookId(int bookId) {
       iebookContentMapper.deleteByBookId(bookId);
    }
}


