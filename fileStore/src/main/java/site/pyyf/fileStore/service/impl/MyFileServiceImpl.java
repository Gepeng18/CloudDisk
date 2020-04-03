package site.pyyf.fileStore.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.pyyf.fileStore.entity.MyFile;
import site.pyyf.fileStore.entity.UserStatistics;
import site.pyyf.fileStore.service.IMyFileService;
import org.springframework.stereotype.Service;
import site.pyyf.fileStore.utils.RedisKeyUtil;

import java.util.List;

/**
 * @ClassName: MyFileServiceImpl
 * @author: xw
 * @date 2020/2/8 22:49
 * @Version: 1.0
 **/

@Service
public class MyFileServiceImpl extends BaseService implements IMyFileService {
    private static final Logger logger= LoggerFactory.getLogger(MyFileServiceImpl.class);
    @Override
    public Integer deleteByFileId(Integer myFileId) {
        return myFileMapper.deleteByFileId(myFileId);
    }


    public Integer addFileByUserId(MyFile myFile) {
        return myFileMapper.addFileByUserId(myFile);
    }

    @Override
    public Integer updateFile(MyFile myFile) {
        return myFileMapper.updateFileByFileId(myFile);
    }


    @Override
    public List<MyFile> getFilesByUserIdAndParentFolderId(Integer userId, Integer parentFolderId) {
        String userFilesKey = RedisKeyUtil.getUserFilesKey(String.valueOf(userId), String.valueOf(parentFolderId));
        List<MyFile> files = (List<MyFile>) redisTemplate.opsForList().range(userFilesKey, 0, -1);
        if (files == null || files.size() == 0) {
            logger.info("查询文件时缓存未击中或文件数量为空，查询数据库");
            files = myFileMapper.getFilesByUserIdAndParentFolderId(userId, parentFolderId);
            //redis不能直接传collection，只能转化为array,并且value不能为空
            if (files.size() != 0)
                redisTemplate.opsForList().rightPushAll(userFilesKey, files.toArray());
        } else {
            logger.info("查询文件时缓存击中，查询Redis缓存");

        }
        return files;
    }

    @Override
    public List<MyFile> getFilesByType(Integer userId, Integer type) {
        return myFileMapper.getFilesByType(userId, type);
    }

    @Override
    public UserStatistics getCountStatistics(Integer userId) {
        UserStatistics statistics = myFileMapper.getCountStatistics(userId);
        statistics.setFolderCount(fileFolderMapper.getFileFolderCountByUserId(userId));
        return statistics;
    }

    @Override
    public MyFile getFileByFileId(Integer myFileId) {
        return myFileMapper.getFileByFileId(myFileId);
    }
}
