package site.pyyf.fileStore.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.pyyf.fileStore.entity.FileFolder;
import site.pyyf.fileStore.entity.MyFile;
import site.pyyf.fileStore.service.IFileFolderService;
import org.springframework.stereotype.Service;
import site.pyyf.fileStore.utils.RedisKeyUtil;

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
private static final Logger logger= LoggerFactory.getLogger(FileFolderServiceImpl.class);
    @Override
    public Integer deleteFileFolderById(Integer fileFolderId) {
        return fileFolderMapper.deleteFileFolderById(fileFolderId);
    }

    @Override
    public Integer addFileFolder(FileFolder fileFolder) {
        return fileFolderMapper.addFileFolder(fileFolder);
    }



    @Override
    public List<FileFolder> getFileFoldersByUserIdAndParentFolderId(Integer userId,Integer parentFolderId) {
        String userFoldersKey = RedisKeyUtil.getUserFoldersKey(String.valueOf(userId), String.valueOf(parentFolderId));
        List<FileFolder> folders = (List<FileFolder>)redisTemplate.opsForList().range(userFoldersKey,0,-1);
        if(folders==null||folders.size()==0){
            logger.info("查询文件夹时缓存未击中或文件夹数量为空，查询数据库");
            folders = fileFolderMapper.getFileFoldersByParentFolderIdAndUserId(userId,parentFolderId);
            //redis直接传collection，只能转化为array,并且value不能为空
            if(folders.size()!=0)
                redisTemplate.opsForList().rightPushAll(userFoldersKey,folders.toArray());
        }else{
            logger.info("查询文件夹时缓存击中，查询缓存");
        }
        return folders;
    }

<<<<<<< HEAD
=======

>>>>>>> 92c711a204ae08a936e2073da2b82198610d3895
    @Override
    public Integer updateFileFolderById(FileFolder fileFolder) {
        return fileFolderMapper.updateFileFolderById(fileFolder);
    }

<<<<<<< HEAD
=======



>>>>>>> 92c711a204ae08a936e2073da2b82198610d3895
    @Override
    public FileFolder getFileFolderByFileFolderId(Integer fileFolderId) {
        if(fileFolderId==0)
            return FileFolder.builder().fileFolderId(0).build();
        return fileFolderMapper.getFileFolderById(fileFolderId);
    }

<<<<<<< HEAD
=======
    @Override
    public List<FileFolder> getRootFoldersByUserId(Integer userId) {
        return fileFolderMapper.getRootFoldersByUserId(userId);
    }
>>>>>>> 92c711a204ae08a936e2073da2b82198610d3895
}
