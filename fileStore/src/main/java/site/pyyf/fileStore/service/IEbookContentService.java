package site.pyyf.fileStore.service;

import site.pyyf.fileStore.entity.EbookConent;

public interface IEbookContentService {
    public String selectContentByContentId(String contentId);

    public void insertEbookContent(EbookConent ebookConent);


    public void updateContentByContentId(String contentId,String content);

    public EbookConent selectByContentId(String contentId);

    public void deleteByBookId(int bookId);
}
