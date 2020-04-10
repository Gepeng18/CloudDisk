package site.pyyf.fileStore.mapper;

import org.apache.ibatis.annotations.Mapper;
import site.pyyf.fileStore.entity.Ebook;

@Mapper
public interface IEbookMapper {

    Ebook selectByFileId(int fileId);

    void insertEbook(Ebook eBook);

    String selectHeaderByFileId(int fileId);

    void deleteByFileId(int efileId);

    void updateEbookNameByFileId(int fileId, String ebookName);
}
