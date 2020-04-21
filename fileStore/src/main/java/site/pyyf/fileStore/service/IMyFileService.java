package site.pyyf.fileStore.service;

import site.pyyf.fileStore.entity.MyFile;
import site.pyyf.fileStore.entity.UserStatistics;

import java.util.List;

/**
 * @Description (MyFile)表服务接口
 *
 * @author "Gepeng"
 * @since 2020-04-21 15:53:16
 */
public interface IMyFileService {

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFileId 主键
     * @return 实例对象
     */
    MyFile queryById(Integer myFileId);
    
     /**
     * @Description 查询全部数据
     * @author makejava
     * @date 2020-04-21 15:53:16
     * @return 对象列表
     */
    List<MyFile> queryAll();
    
    /**
     * @Description 实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @return 对象列表
     */
    List<MyFile> queryAll(MyFile myFile);
   
    /**
     * @Description 查询实体数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @return 数量
     */
    int queryCount();
    
    /**
     * @Description 实体作为筛选条件查询数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @return 数量
     */
    int queryCount(MyFile myFile);
   
   
    /**
     * @Description 查询多条数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<MyFile> queryAllByLimit(int offset, int limit);

    /**
     * @Description 实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<MyFile> queryAllByLimit(MyFile myFile,int offset, int limit);

    
    /**
     * @Description 新增数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @return 影响行数
     */
    int insert(MyFile myFile);

    /**
     * @Description 修改数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @return 实例对象
     */
    MyFile update(MyFile myFile);

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFileId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer myFileId);

    /**
     * @Description 获取仓库的统计信息
     * @Author xw
     * @Date 21:47 2020/2/10
     * @Param [id]
     * @return com.molihub.entity.UserStatistics
     **/
    UserStatistics getCountStatistics(Integer userId);
}