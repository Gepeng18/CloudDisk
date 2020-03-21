package site.pyyf.fileStore.service;

import site.pyyf.fileStore.entity.EbookConent;

public interface IEbookContentService {
    public String selectContentByContentId(String contentId);

    public void updateContentByContentId(String contentId,String content);

    public int selectEbookIdByContentId(String contentId);

    public void deleteByBookId(int bookId);
}
