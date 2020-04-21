package site.pyyf.fileStore.service;

import site.pyyf.fileStore.entity.EbookContent;
import java.util.List;

/**
 * @Description (EbookContent)表服务接口
 *
 * @author "Gepeng"
 * @since 2020-04-21 16:02:11
 */
public interface IEbookContentService {

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param id 主键
     * @return 实例对象
     */
    EbookContent queryById(Integer id);
    
     /**
     * @Description 查询全部数据
     * @author makejava
     * @date 2020-04-21 16:02:11
     * @return 对象列表
     */
    List<EbookContent> queryAll();
    
    /**
     * @Description 实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookContent 实例对象
     * @return 对象列表
     */
    List<EbookContent> queryAll(EbookContent ebookContent);
   
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
     * @param ebookContent 实例对象
     * @return 数量
     */
    int queryCount(EbookContent ebookContent);
   
   
    /**
     * @Description 查询多条数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EbookContent> queryAllByLimit(int offset, int limit);

    /**
     * @Description 实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookContent 实例对象
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<EbookContent> queryAllByLimit(EbookContent ebookContent,int offset, int limit);

    
    /**
     * @Description 新增数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookContent 实例对象
     * @return 影响行数
     */
    int insert(EbookContent ebookContent);

    /**
     * @Description 修改数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookContent 实例对象
     * @return 实例对象
     */
    EbookContent update(EbookContent ebookContent);

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}