package site.pyyf.cloudDisk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import site.pyyf.cloudDisk.mapper.FileFolderMapper;
import site.pyyf.cloudDisk.mapper.FileStoreMapper;
import site.pyyf.cloudDisk.mapper.MyFileMapper;
import site.pyyf.cloudDisk.mapper.UserMapper;

/**
 * @ClassName: BaseService
 * @Description: TODO
 * @author: xw
 * @date 2020/2/25 17:19
 * @Version: 1.0
 **/
public class BaseService {

    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected MyFileMapper myFileMapper;
    @Autowired
    protected FileFolderMapper fileFolderMapper;
    @Autowired
    protected FileStoreMapper fileStoreMapper;
}
