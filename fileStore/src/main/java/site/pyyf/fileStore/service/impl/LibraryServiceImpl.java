package site.pyyf.fileStore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pyyf.fileStore.mapper.IlibraryMapper;
import site.pyyf.fileStore.entity.Ebook;
import site.pyyf.fileStore.service.ILibraryService;


@Service
public class LibraryServiceImpl implements ILibraryService {


    @Autowired
    private IlibraryMapper ilibraryMapper;

    public void insertEbook(Ebook eBook) {
        ilibraryMapper.insertEbook(eBook);
    }

    public Ebook selectByBookId(int ebookId) {
        return ilibraryMapper.selectByBookId(ebookId);
    }


    public void deleteByBookId(int ebookId) {
        ilibraryMapper.deleteByBookId(ebookId);
    }

    public void updateEbookNameByBookId(int ebookId, String ebookName) {
        ilibraryMapper.updateEbookNameByBookId(ebookId, ebookName);
    }
}
