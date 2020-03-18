package site.pyyf.cloudDisk.service.impl;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import site.pyyf.cloudDisk.entity.MyFile;
import site.pyyf.cloudDisk.entity.User;
import site.pyyf.cloudDisk.service.IUserService;
import site.pyyf.cloudDisk.utils.FtpUtil;
import site.pyyf.cloudDisk.utils.LogUtils;

import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * @ClassName UserServiceImpl
 * @Description (User)表服务实现类
 * @author 鹏圆
 * @date 2020-02-25 17:19:31
 * @Version 1.0
 **/
 @Service
public class UserServiceImpl extends BaseService implements IUserService {

    Logger logger = LogUtils.getInstance(UserServiceImpl.class);

    /**
     * @Description 添加User
     * @author 鹏圆
     * @date 2020-02-25 17:19:31
     * @param user 实例对象
     * @return 是否成功
     */
    @Override
    public boolean insert(User user) {
        if(userMapper.insert(user) == 1){
            return true;
        }
        return false;
    }

    /**
     * @Description 删除User
     * @author 鹏圆
     * @date 2020-02-25 17:19:31
     * @param userId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer userId) {
        if(userMapper.deleteById(userId) == 1){
            return true;
        }
        return false;
    }

    /**
     * @Description 查询单条数据
     * @author 鹏圆
     * @date 2020-02-25 17:19:31
     * @param userId 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Integer userId) {
        return userMapper.queryById(userId);
    }

     /**
      * @Description  通过openID查询单条数据
      * @Author xw
      * @Date 18:29 2020/2/25
      * @Param [userId]
      * @return com.moti.entity.User
      **/
     @Override
     public User getUserByOpenId(String openId) {
         return userMapper.getUserByOpenId(openId);
     }

     /**
     * @Description 查询全部数据
     * @author 鹏圆
     * @date 2020-02-25 17:19:31
     * 分页使用MyBatis的插件实现
     * @return 对象列表
     */
    @Override
    public List<User> queryAll() {
        return userMapper.queryAll();
    }

    /**
     * @Description 实体作为筛选条件查询数据
     * @author 鹏圆
     * @date 2020-02-25 17:19:31
     * @param user 实例对象
     * @return 对象列表
     */
    @Override
    public List<User> queryAll(User user) {
        return userMapper.queryAll(user);
    }

    /**
     * @Description 修改数据，哪个属性不为空就修改哪个属性
     * @author 鹏圆
     * @date 2020-02-25 17:19:31
     * @param user 实例对象
     * @return 是否成功
     */
    @Override
    public boolean update(User user) {
        if(userMapper.update(user) == 1){
            return true;
        }
        return false;
    }


    @Override
    public Integer addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public User getUserByUserId(Integer userId) {
        return userMapper.getUserByUserId(userId);
    }

    @Override
    public User getUserById(Integer userId) {
        return userMapper.getUserById(userId);
    }

    @Override
    public Integer addSize(Integer id, Integer size) {
        return userMapper.addSize(id,size);
    }

    @Override
    public Integer subSize(Integer id, Integer size) {
        return userMapper.subSize(id,size);
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
            boolean flag = FtpUtil.downloadFile("/" + remotePath, tmpFileStream);
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