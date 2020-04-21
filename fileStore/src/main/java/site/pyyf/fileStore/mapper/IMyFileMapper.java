package site.pyyf.fileStore.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.pyyf.fileStore.entity.MyFile;
import site.pyyf.fileStore.entity.UserStatistics;

import java.util.List;

/**
 * @Description (MyFile)表数据库访问层
 *
 * @author "Gepeng"
 * @since 2020-04-21 15:53:16
 */
@Mapper
public interface IMyFileMapper {

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFileId 主键
     * @return 实例对象
     */
    MyFile queryById(Integer myFileId);

    /**
     * @Description 查询指定行数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<MyFile> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * @Description 查询全部数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @return 对象列表
     */
    List<MyFile> queryAll();

    /**
     * @Description 通过实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @return 对象列表
     */
    List<MyFile> queryAll(MyFile myFile);

    /**
     * @Description 通过实体数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @return 数量
     */
    int queryCount();
    
     /**
     * @Description 通过实体作为筛选条件查询数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @return 数量
     */
    int queryCount(MyFile myFile);

    /**
     * @Description 通过实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @return 对象列表
     */
    List<MyFile> querySomeByLimit(@Param("myFile") MyFile myFile, @Param("offset") int offset, @Param("limit") int limit);
    
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
     * @return 影响行数
     */
    int update(MyFile myFile);

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFileId 主键
     * @return 影响行数
     */
    int deleteById(Integer myFileId);

    /**
     * @Description 获取仓库的统计信息
     * @Author xw
     * @Date 21:47 2020/2/10
     * @Param [id]
     * @return com.molihub.entity.UserStatistics
     **/
    UserStatistics getCountStatistics(Integer userId);
}