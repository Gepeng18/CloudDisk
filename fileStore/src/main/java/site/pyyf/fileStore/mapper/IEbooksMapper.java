package site.pyyf.fileStore.mapper;

import org.apache.ibatis.annotations.Mapper;
import site.pyyf.fileStore.entity.Ebook;

@Mapper
public interface IlibraryMapper {

    Ebook selectByBookId(int bookId);

    void insertEbook(Ebook eBook);

    String selectHeaderByBookId(int ebookId);

    void deleteByBookId(int ebookId);

    void updateEbookNameByBookId(int ebookId, String ebookName);
}
