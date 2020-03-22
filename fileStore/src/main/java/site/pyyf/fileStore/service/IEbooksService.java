package site.pyyf.fileStore.service;

import site.pyyf.fileStore.entity.Ebook;

public interface ILibraryService {
    public void insertEbook(Ebook eBook);

    public Ebook selectByBookId(int ebookId);


    public void deleteByBookId(int ebookId);

    public void updateEbookNameByBookId(int ebookId, String ebookName);

}
