package site.pyyf.fileStore.mapper;


import org.apache.ibatis.annotations.Mapper;
import site.pyyf.fileStore.entity.EbookConent;

import java.util.List;

@Mapper
public interface IEbookContentMapper {

    String selectContentByContentId(String content_id);

    void insertEbookContent(EbookConent ebookConent);

    int selectEbookIdByContentId(String contentId);

    void updateContentByContentId(String contentId, String content);


    void deleteByFileId(int fileId);

    int insertAllEbookContent(List<EbookConent> allContent);

}
