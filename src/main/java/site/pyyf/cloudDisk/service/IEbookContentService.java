package site.pyyf.cloudDisk.service;

import site.pyyf.cloudDisk.entity.EbookConent;

public interface IEbookContentService {
    public String selectContentByContentId(String contentId);

    public void insertEbookContent(EbookConent ebookConent);


    public void updateContentByContentId(String contentId,String content);

    public EbookConent selectByContentId(String contentId);

    public void deleteByBookId(int bookId);
}
