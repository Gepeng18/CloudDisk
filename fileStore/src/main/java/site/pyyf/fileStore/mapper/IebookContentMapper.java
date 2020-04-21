package site.pyyf.fileStore.mapper;

import site.pyyf.fileStore.entity.EbookContent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Description (EbookContent)表数据库访问层
 *
 * @author "Gepeng"
 * @since 2020-04-21 19:52:58
 */
@Mapper
public interface IEbookContentMapper {

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:58
     * @param id 主键
     * @return 实例对象
     */
    EbookContent queryById(Integer id);

    /**
     * @Description 查询指定行数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:58
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EbookContent> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * @Description 查询全部数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:58
     * @return 对象列表
     */
    List<EbookContent> queryAll();

    /**
     * @Description 通过实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:58
     * @param ebookContent 实例对象
     * @return 对象列表
     */
    List<EbookContent> queryAll(EbookContent ebookContent);

    /**
     * @Description 通过实体数量
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:58
     * @return 数量
     */
    int queryCount();
    
     /**
     * @Description 通过实体作为筛选条件查询数量
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:58
     * @param ebookContent 实例对象
     * @return 数量
     */
    int queryCount(EbookContent ebookContent);

    /**
     * @Description 通过实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:58
     * @param ebookContent 实例对象
     * @return 对象列表
     */
    List<EbookContent> querySomeByLimit(@Param("ebookContent") EbookContent ebookContent, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * @Description 新增数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:58
     * @param ebookContent 实例对象
     * @return 影响行数
     */
    int insert(EbookContent ebookContent);

    /**
     * @Description 修改数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:58
     * @param ebookContent 实例对象
     * @return 影响行数
     */
    int update(EbookContent ebookContent);

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:58
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    int insertAllEbookContent(List<EbookContent> allContent);
}