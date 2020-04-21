package site.pyyf.fileStore.service.impl;

import site.pyyf.fileStore.entity.User;
import site.pyyf.fileStore.mapper.IUserMapper;
import site.pyyf.fileStore.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description (User)表服务实现类
 *
 * @author "Gepeng"
 * @since 2020-04-21 15:53:29
 */
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private IUserMapper iUserMapper;

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:29
     * @param userId 主键
     * @return 实例对象
     */
    @Override
    public User queryById(Integer userId) {
        return iUserMapper.queryById(userId);
    }

    /**
     * @Description 查询多条数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:29
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<User> queryAllByLimit(int offset, int limit) {
        return iUserMapper.queryAllByLimit(offset, limit);
    }
    
    /**
     * @Description 查询全部数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:29
     * @return 对象列表
     */
    @Override
    public List<User> queryAll() {
        return iUserMapper.queryAll();
    }
    
     /**
     * @Description 实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:29
     * @param user 实例对象
     * @return 对象列表
     */
    @Override
    public List<User> queryAll(User user) {
        return iUserMapper.queryAll(user);
    }
    
    /**
     * @Description 查询实体数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:29
     * @return 数量
     */
    @Override
    public int queryCount() {
        return iUserMapper.queryCount();
    }
    
    /**
     * @Description 实体作为筛选条件查询数量
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:29
     * @param user 实例对象
     * @return 数量
     */
    @Override
    public int queryCount(User user) {
        return iUserMapper.queryCount(user);
    }

    /**
     * @Description 实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:29
     * @param user 实例对象
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<User> queryAllByLimit(User user,int offset, int limit) {
        return iUserMapper.querySomeByLimit(user,offset, limit);
    }
    
    /**
     * @Description 新增数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:29
     * @param user 实例对象
     * @return 影响行数
     */
    @Override
    public int insert(User user) {
        return this.iUserMapper.insert(user);
    }

    /**
     * @Description 修改数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:29
     * @param user 实例对象
     * @return 实例对象
     */
    @Override
    public User update(User user) {
        this.iUserMapper.update(user);
        return queryById(user.getId());
    }

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 15:53:29
     * @param userId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer userId) {
        return iUserMapper.deleteById(userId) > 0;
    }
}