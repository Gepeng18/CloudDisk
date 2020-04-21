package site.pyyf.fileStore.service.impl;

import org.springframework.stereotype.Service;
import site.pyyf.fileStore.entity.FileFolder;
import site.pyyf.fileStore.mapper.IFileFolderMapper;
import site.pyyf.fileStore.service.IFileFolderService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description (FileFolder)表服务实现类
 *
 * @author "Gepeng"
 * @since 2020-04-21 15:45:29
 */
@Service
public class FileFolderServiceImpl implements IFileFolderService {
    @Resource
    private IFileFolderMapper iFileFolderMapper;

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolderId 主键
     * @return 实例对象
     */
    @Override
    public FileFolder queryById(Integer fileFolderId) {
        return iFileFolderMapper.queryById(fileFolderId);
    }

    /**
     * @Description 查询多条数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<FileFolder> queryAllByLimit(int offset, int limit) {
        return iFileFolderMapper.queryAllByLimit(offset, limit);
    }
    
    /**
     * @Description 查询全部数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @return 对象列表
     */
    @Override
    public List<FileFolder> queryAll() {
        return iFileFolderMapper.queryAll();
    }
    
     /**
     * @Description 实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolder 实例对象
     * @return 对象列表
     */
    @Override
    public List<FileFolder> queryAll(FileFolder fileFolder) {
        return iFileFolderMapper.queryAll(fileFolder);
    }
    
    /**
     * @Description 查询实体数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @return 数量
     */
    @Override
    public int queryCount() {
        return iFileFolderMapper.queryCount();
    }
    
    /**
     * @Description 实体作为筛选条件查询数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolder 实例对象
     * @return 数量
     */
    @Override
    public int queryCount(FileFolder fileFolder) {
        return iFileFolderMapper.queryCount(fileFolder);
    }

    /**
     * @Description 实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolder 实例对象
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<FileFolder> queryAllByLimit(FileFolder fileFolder,int offset, int limit) {
        return iFileFolderMapper.querySomeByLimit(fileFolder,offset, limit);
    }
    
    /**
     * @Description 新增数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolder 实例对象
     * @return 影响行数
     */
    @Override
    public int insert(FileFolder fileFolder) {
        return this.iFileFolderMapper.insert(fileFolder);
    }

    /**
     * @Description 修改数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolder 实例对象
     * @return 实例对象
     */
    @Override
    public FileFolder update(FileFolder fileFolder) {
        this.iFileFolderMapper.update(fileFolder);
        return queryById(fileFolder.getId());
    }

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:45:29
     * @param fileFolderId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer fileFolderId) {
        return iFileFolderMapper.deleteById(fileFolderId) > 0;
    }
}