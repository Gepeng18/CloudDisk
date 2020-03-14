package site.pyyf.cloudDisk.mapper;


import org.apache.ibatis.annotations.Mapper;
import site.pyyf.cloudDisk.entity.EbookConent;

import java.util.List;

@Mapper
public interface IebookContentMapper {

    String selectContentByContentId(String content_id);

    void insertEbookContent(EbookConent ebookConent);

    EbookConent selectByContentId(String contentId);

    void updateContentByContentId(String contentId, String content);


    void deleteByBookId(int bookId);

    int insertAllEbookContent(List<EbookConent> allContent);

}
