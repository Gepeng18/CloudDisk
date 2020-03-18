package site.pyyf.cloudDisk.service.impl;

import site.pyyf.cloudDisk.entity.MyFile;
import site.pyyf.cloudDisk.entity.UserStatistics;
import site.pyyf.cloudDisk.service.IMyFileService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @ClassName: MyFileServiceImpl
* @Description: TODO
* @author: xw
* @date 2020/2/8 22:49
* @Version: 1.0
**/

@Service
public class MyFileServiceImpl extends BaseService implements IMyFileService {

    @Override
    public Integer deleteByFileId(Integer myFileId) {
        return myFileMapper.deleteByFileId(myFileId);
    }

    @Override
    public Integer deleteByParentFolderId(Integer id) {
        return myFileMapper.deleteByParentFolderId(id);
    }

    @Override
    public Integer addFileByUserId(MyFile myFile) {
        return myFileMapper.addFileByUserId(myFile);
    }

    @Override
    public Integer updateFile(MyFile myFile) {
        return myFileMapper.updateFileByFileId(myFile);
    }

    @Override
    public List<MyFile> getRootFilesByUserId(Integer userId) {
        return myFileMapper.getRootFilesByUserId(userId);
    }

    @Override
    public List<MyFile> getFilesByParentFolderId(Integer parentFolderId) {
        return myFileMapper.getFilesByParentFolderId(parentFolderId);
    }

    @Override
    public List<MyFile> getFilesByType(Integer storeId, Integer type) {
        return myFileMapper.getFilesByType(storeId,type);
    }

    @Override
    public UserStatistics getCountStatistics(Integer id) {
        UserStatistics statistics = myFileMapper.getCountStatistics(id);
        statistics.setFolderCount(fileFolderMapper.getFileFolderCountByUserId(id));
        return statistics;
    }

    @Override
    public MyFile getFileByFileId(Integer myFileId) {
        return myFileMapper.getFileByFileId(myFileId);
    }
}
