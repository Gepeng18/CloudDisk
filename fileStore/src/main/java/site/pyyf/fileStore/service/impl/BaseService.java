package site.pyyf.fileStore.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import site.pyyf.fileStore.config.AliyunConfig;
import site.pyyf.fileStore.config.CloudDiskConfig;
import site.pyyf.fileStore.mapper.FileFolderMapper;
import site.pyyf.fileStore.mapper.MyFileMapper;
import site.pyyf.fileStore.mapper.UserMapper;
import site.pyyf.fileStore.service.IEbookContentService;
import site.pyyf.fileStore.service.IMediaTranfer;
import site.pyyf.fileStore.service.IOSSService;

/**
 * @ClassName: BaseService
 * @author: xw
 * @date 2020/2/25 17:19
 * @Version: 1.0
 **/
public class BaseService {
    @Autowired
    protected AliyunConfig aliyunConfig;
    @Autowired
    protected CloudDiskConfig cloudDiskConfig;

    @Autowired
    protected IMediaTranfer iMediaTranfer;
    @Autowired
    protected IOSSService iossService;
    @Autowired
    protected IEbookContentService iEbookContentService;
    @Autowired
    protected RedisTemplate redisTemplate;

    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected MyFileMapper myFileMapper;
    @Autowired
    protected FileFolderMapper fileFolderMapper;

}
