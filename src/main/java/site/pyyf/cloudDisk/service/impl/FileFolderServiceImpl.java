package site.pyyf.cloudDisk.service.impl;

import site.pyyf.cloudDisk.entity.FileFolder;
import site.pyyf.cloudDisk.entity.MyFile;
import site.pyyf.cloudDisk.service.IFileFolderService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @ClassName: FileFolderServiceImpl
* @Description: TODO
* @author: xw
* @date 2020/2/8 23:00
* @Version: 1.0
**/

@Service
public class FileFolderServiceImpl extends BaseService implements IFileFolderService {

    @Override
    public Integer deleteFileFolderById(Integer fileFolderId) {
        return fileFolderMapper.deleteFileFolderById(fileFolderId);
    }

    @Override
    public Integer addFileFolder(FileFolder fileFolder) {
        return fileFolderMapper.addFileFolder(fileFolder);
    }

    @Override
    public List<MyFile> getFileFolderById(Integer fileFolderId) {
        return fileFolderMapper.getFileByFileFolder(fileFolderId);
    }

    @Override
    public Integer updateFileFolderById(FileFolder fileFolder) {
        return fileFolderMapper.updateFileFolderById(fileFolder);
    }

    @Override
    public List<FileFolder> getFileFolderByParentFolderId(Integer parentFolderId) {
        return fileFolderMapper.getFileFolderByParentFolderId(parentFolderId);
    }

    @Override
    public FileFolder getFileFolderByFileFolderId(Integer fileFolderId) {
        if(fileFolderId==0)
            return FileFolder.builder().fileFolderId(0).build();
        return fileFolderMapper.getFileFolderById(fileFolderId);
    }

    @Override
    public List<FileFolder> getRootFoldersByUserId(Integer userId) {
        return fileFolderMapper.getRootFoldersByUserId(userId);
    }
}
