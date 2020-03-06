package site.pyyf.cloudpan.mapper;


import org.apache.ibatis.annotations.Mapper;
import site.pyyf.cloudpan.entity.EbookConent;

@Mapper
public interface IebookContentMapper {

    String selectContentByContentId(String content_id);

    void insertEbookContent(EbookConent ebookConent);

    EbookConent selectByContentId(String contentId);

    void updateContentByContentId(String contentId, String content);


    void deleteByBookId(int bookId);
}
