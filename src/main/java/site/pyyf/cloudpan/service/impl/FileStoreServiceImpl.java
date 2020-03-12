package site.pyyf.cloudpan.service.impl;

import site.pyyf.cloudpan.entity.FileStore;
import site.pyyf.cloudpan.entity.MyFile;
import site.pyyf.cloudpan.service.FileStoreService;
import site.pyyf.cloudpan.utils.CommunityUtil;
import site.pyyf.cloudpan.utils.FtpUtil;
import site.pyyf.cloudpan.utils.LogUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName: FileStoreServiceImpl
 * @Description: 文件仓库业务层接口实现类
 * @author: xw
 * @date 2020/1/26 22:16
 * @Version: 1.0
 **/
@Service
public class FileStoreServiceImpl extends BaseService implements FileStoreService {

    Logger logger = LogUtils.getInstance(UserServiceImpl.class);

    @Override
    public Integer addFileStore(FileStore fileStore) {
        return fileStoreMapper.addFileStore(fileStore);
    }

    @Override
    public FileStore getFileStoreByUserId(Integer userId) {
        return fileStoreMapper.getFileStoreByUserId(userId);
    }

    @Override
    public FileStore getFileStoreById(Integer fileStoreId) {
        return fileStoreMapper.getFileStoreById(fileStoreId);
    }

    @Override
    public Integer addSize(Integer id, Integer size) {
        return fileStoreMapper.addSize(id,size);
    }

    @Override
    public Integer subSize(Integer id, Integer size) {
        return fileStoreMapper.subSize(id,size);
    }

    @Override
    public StringBuilder getFileContentByMyFile(MyFile file) {
        StringBuilder code = new StringBuilder();
        //获取文件信息
        String remotePath = file.getMyFilePath();

        try {
            File temp = new File("temp");
            if (!temp.exists()) {
                temp.mkdirs();
            }
            String tempStr = "temp/" + UUID.randomUUID().toString();
            //去FTP上拉取
            OutputStream tmpFileStream = new FileOutputStream(new File(tempStr));
            boolean flag = FtpUtil.downloadFile("/" + remotePath, file.getMyFileName(), tmpFileStream);
            if (flag) {

                //获得服务器本地的文件，并使用IO流写出到浏览器
                InputStream is = new BufferedInputStream(new FileInputStream(tempStr));
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String buffer = null;
                while ((buffer = br.readLine()) != null) {
                    buffer += '\n';
                    code.append(buffer);
                }
                is.close();
                tmpFileStream.close();
                logger.info("文件下载成功!" + file);

                new File(tempStr).delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

}
