package site.pyyf.cloudpan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import site.pyyf.cloudpan.mapper.FileFolderMapper;
import site.pyyf.cloudpan.mapper.FileStoreMapper;
import site.pyyf.cloudpan.mapper.MyFileMapper;
import site.pyyf.cloudpan.mapper.UserMapper;

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
