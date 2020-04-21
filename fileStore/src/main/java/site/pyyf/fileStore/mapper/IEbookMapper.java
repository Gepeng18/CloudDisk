package site.pyyf.fileStore.mapper;

import site.pyyf.fileStore.entity.Ebook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @Description (Ebook)表数据库访问层
 *
 * @author "Gepeng"
 * @since 2020-04-21 19:52:59
 */
@Mapper
public interface IEbookMapper {

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:59
     * @param ebookId 主键
     * @return 实例对象
     */
    Ebook queryById(Integer ebookId);

    /**
     * @Description 查询指定行数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:59
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Ebook> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * @Description 查询全部数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:59
     * @return 对象列表
     */
    List<Ebook> queryAll();

    /**
     * @Description 通过实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:59
     * @param ebook 实例对象
     * @return 对象列表
     */
    List<Ebook> queryAll(Ebook ebook);

    /**
     * @Description 通过实体数量
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:59
     * @return 数量
     */
    int queryCount();
    
     /**
     * @Description 通过实体作为筛选条件查询数量
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:59
     * @param ebook 实例对象
     * @return 数量
     */
    int queryCount(Ebook ebook);

    /**
     * @Description 通过实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:59
     * @param ebook 实例对象
     * @return 对象列表
     */
    List<Ebook> querySomeByLimit(@Param("ebook") Ebook ebook, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * @Description 新增数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:59
     * @param ebook 实例对象
     * @return 影响行数
     */
    int insert(Ebook ebook);

    /**
     * @Description 修改数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:59
     * @param ebook 实例对象
     * @return 影响行数
     */
    int update(Ebook ebook);

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 19:52:59
     * @param ebookId 主键
     * @return 影响行数
     */
    int deleteById(Integer ebookId);

}