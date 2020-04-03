package site.pyyf.fileStore.service.impl;

import org.springframework.stereotype.Service;
import site.pyyf.fileStore.entity.Ebook;
import site.pyyf.fileStore.service.IEbooksService;


@Service
public class EbooksServiceImpl extends  BaseService implements IEbooksService {


    public void insertEbook(Ebook eBook) {
        ilibraryMapper.insertEbook(eBook);
    }

    public Ebook selectByFileId(int file_id) {
        return ilibraryMapper.selectByFileId(file_id);
    }


    public void deleteByFileId(int file_id) {
        ilibraryMapper.deleteByFileId(file_id);
    }

    public void updateEbookNameByFileId(int file_id, String ebookName) {
        ilibraryMapper.updateEbookNameByFileId(file_id, ebookName);
    }
}
