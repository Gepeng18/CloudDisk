package site.pyyf.fileStore.service;

import site.pyyf.fileStore.entity.Ebook;

public interface IEbooksService {
    public void insertEbook(Ebook eBook);

    public Ebook selectByFileId(int fileId);


    public void deleteByFileId(int fileId);

    public void updateEbookNameByFileId(int fileId, String ebookName);

}
