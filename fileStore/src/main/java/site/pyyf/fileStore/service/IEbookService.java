package site.pyyf.fileStore.service;

import site.pyyf.fileStore.entity.Ebook;
import java.util.List;

/**
 * @Description (Ebook)表服务接口
 *
 * @author "Gepeng"
 * @since 2020-04-21 16:02:11
 */
public interface IEbookService {

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookId 主键
     * @return 实例对象
     */
    Ebook queryById(Integer ebookId);
    
     /**
     * @Description 查询全部数据
     * @author makejava
     * @date 2020-04-21 16:02:11
     * @return 对象列表
     */
    List<Ebook> queryAll();
    
    /**
     * @Description 实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebook 实例对象
     * @return 对象列表
     */
    List<Ebook> queryAll(Ebook ebook);
   
    /**
     * @Description 查询实体数量
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @return 数量
     */
    int queryCount();
    
    /**
     * @Description 实体作为筛选条件查询数量
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebook 实例对象
     * @return 数量
     */
    int queryCount(Ebook ebook);
   
   
    /**
     * @Description 查询多条数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Ebook> queryAllByLimit(int offset, int limit);

    /**
     * @Description 实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebook 实例对象
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Ebook> queryAllByLimit(Ebook ebook,int offset, int limit);

    
    /**
     * @Description 新增数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebook 实例对象
     * @return 影响行数
     */
    int insert(Ebook ebook);

    /**
     * @Description 修改数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebook 实例对象
     * @return 实例对象
     */
    Ebook update(Ebook ebook);

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer ebookId);

}