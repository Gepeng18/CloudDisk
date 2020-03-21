package site.pyyf.fileStore.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import site.pyyf.fileStore.entity.User;
import site.pyyf.fileStore.service.IUserService;

import java.util.List;

@Service
public class UserServiceImpl extends BaseService implements IUserService {

   private static final Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * @param user 实例对象
     * @return 是否成功
     * @Description 添加User
     * @author 鹏圆
     * @date 2020-02-25 17:19:31
     */
    @Override
    public boolean insert(User user) {
        if (userMapper.insert(user) == 1) {
            return true;
        }
        return false;
    }

    /**
<<<<<<< HEAD
=======
     * @param userId 主键
     * @return 是否成功
     * @Description 删除User
     * @author 鹏圆
     * @date 2020-02-25 17:19:31
     */
    @Override
    public boolean deleteById(Integer userId) {
        if (userMapper.deleteById(userId) == 1) {
            return true;
        }
        return false;
    }

    /**
     * @param userId 主键
     * @return 实例对象
     * @Description 查询单条数据
     * @author 鹏圆
     * @date 2020-02-25 17:19:31
     */
    @Override
    public User queryById(Integer userId) {
        return userMapper.queryById(userId);
    }

    /**
>>>>>>> 92c711a204ae08a936e2073da2b82198610d3895
     * @return com.moti.entity.User
     * @Description 通过openID查询单条数据
     * @Author xw
     * @Date 18:29 2020/2/25
     * @Param [userId]
     **/
    @Override
    public User getUserByOpenId(String openId) {
        return userMapper.getUserByOpenId(openId);
    }

    /**
<<<<<<< HEAD
=======
     * @return 对象列表
     * @Description 查询全部数据
     * @author 鹏圆
     * @date 2020-02-25 17:19:31
     * 分页使用MyBatis的插件实现
     */
    @Override
    public List<User> queryAll() {
        return userMapper.queryAll();
    }

    /**
     * @param user 实例对象
     * @return 对象列表
     * @Description 实体作为筛选条件查询数据
     * @author 鹏圆
     * @date 2020-02-25 17:19:31
     */
    @Override
    public List<User> queryAll(User user) {
        return userMapper.queryAll(user);
    }

    /**
>>>>>>> 92c711a204ae08a936e2073da2b82198610d3895
     * @param user 实例对象
     * @return 是否成功
     * @Description 修改数据，哪个属性不为空就修改哪个属性
     * @author 鹏圆
     * @date 2020-02-25 17:19:31
     */
    @Override
    public boolean update(User user) {
        if (userMapper.update(user) == 1) {
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
        return userMapper.addSize(id, size);
    }

    @Override
    public Integer subSize(Integer id, Integer size) {
        return userMapper.subSize(id, size);
    }

}
