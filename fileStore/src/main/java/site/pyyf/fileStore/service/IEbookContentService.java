package site.pyyf.fileStore.service;

import site.pyyf.fileStore.entity.EbookConent;

public interface IEbookContentService {
    public String selectContentByContentId(String contentId);

<<<<<<< HEAD
=======
    public void insertEbookContent(EbookConent ebookConent);


>>>>>>> 92c711a204ae08a936e2073da2b82198610d3895
    public void updateContentByContentId(String contentId,String content);

    public int selectEbookIdByContentId(String contentId);

    public void deleteByBookId(int bookId);
}
