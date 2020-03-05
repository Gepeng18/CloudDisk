package site.pyyf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pyyf.mapper.IlibraryMapper;
import site.pyyf.entity.Ebook;

import java.util.List;


@Service
public class LibraryService {


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
