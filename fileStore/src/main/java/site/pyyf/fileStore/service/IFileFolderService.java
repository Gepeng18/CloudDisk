package site.pyyf.fileStore.service;

import site.pyyf.fileStore.entity.FileFolder;
import java.util.List;

/**
 * @Description (FileFolder)表服务接口
 *
 * @author "Gepeng"
 * @since 2020-04-21 15:45:29
 */
public interface IFileFolderService {

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolderId 主键
     * @return 实例对象
     */
    FileFolder queryById(Integer fileFolderId);
    
     /**
     * @Description 查询全部数据
     * @author makejava
     * @date 2020-04-21 15:45:29
     * @return 对象列表
     */
    List<FileFolder> queryAll();
    
    /**
     * @Description 实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolder 实例对象
     * @return 对象列表
     */
    List<FileFolder> queryAll(FileFolder fileFolder);
   
    /**
     * @Description 查询实体数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @return 数量
     */
    int queryCount();
    
    /**
     * @Description 实体作为筛选条件查询数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolder 实例对象
     * @return 数量
     */
    int queryCount(FileFolder fileFolder);
   
   
    /**
     * @Description 查询多条数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<FileFolder> queryAllByLimit(int offset, int limit);

    /**
     * @Description 实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolder 实例对象
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<FileFolder> queryAllByLimit(FileFolder fileFolder,int offset, int limit);

    
    /**
     * @Description 新增数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolder 实例对象
     * @return 影响行数
     */
    int insert(FileFolder fileFolder);

    /**
     * @Description 修改数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolder 实例对象
     * @return 实例对象
     */
    FileFolder update(FileFolder fileFolder);

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolderId 主键
     * @return 是否成功
     */
    boolean deleteById(Integer fileFolderId);

}