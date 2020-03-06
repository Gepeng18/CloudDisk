package site.pyyf.cloudpan.service.impl;

import site.pyyf.cloudpan.entity.FileStoreStatistics;
import site.pyyf.cloudpan.entity.MyFile;
import site.pyyf.cloudpan.service.MyFileService;
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
public class MyFileServiceImpl extends BaseService implements MyFileService {

    @Override
    public Integer deleteByFileId(Integer myFileId) {
        return myFileMapper.deleteByFileId(myFileId);
    }

    @Override
    public Integer deleteByParentFolderId(Integer id) {
        return myFileMapper.deleteByParentFolderId(id);
    }

    @Override
    public Integer addFileByFileStoreId(MyFile myFile) {
        return myFileMapper.addFileByFileStoreId(myFile);
    }

    @Override
    public Integer updateFile(MyFile myFile) {
        return myFileMapper.updateFileByFileId(myFile);
    }

    @Override
    public List<MyFile> getRootFilesByFileStoreId(Integer fileStoreId) {
        return myFileMapper.getRootFilesByFileStoreId(fileStoreId);
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
    public FileStoreStatistics getCountStatistics(Integer id) {
        FileStoreStatistics statistics = myFileMapper.getCountStatistics(id);
        statistics.setFolderCount(fileFolderMapper.getFileFolderCountByFileStoreId(id));
        return statistics;
    }

    @Override
    public MyFile getFileByFileId(Integer myFileId) {
        return myFileMapper.getFileByFileId(myFileId);
    }
}
