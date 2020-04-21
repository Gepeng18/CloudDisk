package site.pyyf.fileStore.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.pyyf.fileStore.entity.FileFolder;

import java.util.List;

/**
 * @Description (FileFolder)表数据库访问层
 *
 * @author "Gepeng"
 * @since 2020-04-21 15:45:28
 */
@Mapper
public interface IFileFolderMapper {

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:28
     * @param fileFolderId 主键
     * @return 实例对象
     */
    FileFolder queryById(Integer fileFolderId);

    /**
     * @Description 查询指定行数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:28
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<FileFolder> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * @Description 查询全部数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:28
     * @return 对象列表
     */
    List<FileFolder> queryAll();

    /**
     * @Description 通过实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:28
     * @param fileFolder 实例对象
     * @return 对象列表
     */
    List<FileFolder> queryAll(FileFolder fileFolder);

    /**
     * @Description 通过实体数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:28
     * @return 数量
     */
    int queryCount();
    
     /**
     * @Description 通过实体作为筛选条件查询数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:28
     * @param fileFolder 实例对象
     * @return 数量
     */
    int queryCount(FileFolder fileFolder);

    /**
     * @Description 通过实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:28
     * @param fileFolder 实例对象
     * @return 对象列表
     */
    List<FileFolder> querySomeByLimit(@Param("fileFolder") FileFolder fileFolder, @Param("offset") int offset, @Param("limit") int limit);
    
    /**
     * @Description 新增数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:28
     * @param fileFolder 实例对象
     * @return 影响行数
     */
    int insert(FileFolder fileFolder);

    /**
     * @Description 修改数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:28
     * @param fileFolder 实例对象
     * @return 影响行数
     */
    int update(FileFolder fileFolder);

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:28
     * @param fileFolderId 主键
     * @return 影响行数
     */
    int deleteById(Integer fileFolderId);

}