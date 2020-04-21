package site.pyyf.fileStore.service.impl;

import org.springframework.stereotype.Service;
import site.pyyf.fileStore.entity.FileFolder;
import site.pyyf.fileStore.entity.MyFile;
import site.pyyf.fileStore.entity.UserStatistics;
import site.pyyf.fileStore.mapper.IFileFolderMapper;
import site.pyyf.fileStore.mapper.IMyFileMapper;
import site.pyyf.fileStore.service.IMyFileService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description (MyFile)表服务实现类
 *
 * @author "Gepeng"
 * @since 2020-04-21 15:53:16
 */
@Service
public class MyFileServiceImpl implements IMyFileService {
    @Resource
    private IMyFileMapper iMyFileMapper;

    @Resource
    private IFileFolderMapper iFileFolderMapper;

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFileId 主键
     * @return 实例对象
     */
    @Override
    public MyFile queryById(Integer myFileId) {
        return iMyFileMapper.queryById(myFileId);
    }

    /**
     * @Description 查询多条数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<MyFile> queryAllByLimit(int offset, int limit) {
        return iMyFileMapper.queryAllByLimit(offset, limit);
    }
    
    /**
     * @Description 查询全部数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @return 对象列表
     */
    @Override
    public List<MyFile> queryAll() {
        return iMyFileMapper.queryAll();
    }
    
     /**
     * @Description 实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @return 对象列表
     */
    @Override
    public List<MyFile> queryAll(MyFile myFile) {
        return iMyFileMapper.queryAll(myFile);
    }
    
    /**
     * @Description 查询实体数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @return 数量
     */
    @Override
    public int queryCount() {
        return iMyFileMapper.queryCount();
    }
    
    /**
     * @Description 实体作为筛选条件查询数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @return 数量
     */
    @Override
    public int queryCount(MyFile myFile) {
        return iMyFileMapper.queryCount(myFile);
    }

    /**
     * @Description 实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<MyFile> queryAllByLimit(MyFile myFile,int offset, int limit) {
        return iMyFileMapper.querySomeByLimit(myFile,offset, limit);
    }
    
    /**
     * @Description 新增数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @return 影响行数
     */
    @Override
    public int insert(MyFile myFile) {
        return this.iMyFileMapper.insert(myFile);
    }

    /**
     * @Description 修改数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFile 实例对象
     * @return 实例对象
     */
    @Override
    public MyFile update(MyFile myFile) {
        this.iMyFileMapper.update(myFile);
        return queryById(myFile.getId());
    }

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:16
     * @param myFileId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer myFileId) {
        return iMyFileMapper.deleteById(myFileId) > 0;
    }

    @Override
    public UserStatistics getCountStatistics(Integer userId) {
        UserStatistics statistics = iMyFileMapper.getCountStatistics(userId);
        //因为根文件夹也是个文件夹，所以-1
        statistics.setFolderCount(iFileFolderMapper.queryCount(FileFolder.builder().userId(userId).build())-1);
        return statistics;
    }
}