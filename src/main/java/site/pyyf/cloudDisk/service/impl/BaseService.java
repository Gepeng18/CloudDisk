package site.pyyf.cloudDisk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import site.pyyf.cloudDisk.config.AliyunConfig;
import site.pyyf.cloudDisk.config.CloudDiskConfig;
import site.pyyf.cloudDisk.mapper.FileFolderMapper;
import site.pyyf.cloudDisk.mapper.MyFileMapper;
import site.pyyf.cloudDisk.mapper.UserMapper;
import site.pyyf.cloudDisk.service.IEbookContentService;
import site.pyyf.cloudDisk.service.IMediaTranfer;
import site.pyyf.cloudDisk.service.IOSSService;

/**
 * @ClassName: BaseService
 * @Description: TODO
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
