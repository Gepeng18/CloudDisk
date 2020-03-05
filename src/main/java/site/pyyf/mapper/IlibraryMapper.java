package site.pyyf.mapper;

import org.apache.ibatis.annotations.Mapper;
import site.pyyf.entity.Ebook;

import java.util.List;

@Mapper
public interface IlibraryMapper {

    Ebook selectByBookId(int bookId);

    void insertEbook(Ebook eBook);

    String selectHeaderByBookId(int ebookId);

    void deleteByBookId(int ebookId);

    void updateEbookNameByBookId(int ebookId, String ebookName);
}
