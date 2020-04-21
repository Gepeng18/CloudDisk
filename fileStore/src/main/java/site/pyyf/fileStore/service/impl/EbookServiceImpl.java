package site.pyyf.fileStore.service.impl;

import site.pyyf.fileStore.entity.Ebook;
import site.pyyf.fileStore.mapper.IEbookMapper;
import site.pyyf.fileStore.service.IEbookService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description (Ebook)表服务实现类
 *
 * @author "Gepeng"
 * @since 2020-04-21 16:02:11
 */
@Service
public class EbookServiceImpl implements IEbookService {
    @Resource
    private IEbookMapper iEbookMapper;

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookId 主键
     * @return 实例对象
     */
    @Override
    public Ebook queryById(Integer ebookId) {
        return iEbookMapper.queryById(ebookId);
    }

    /**
     * @Description 查询多条数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Ebook> queryAllByLimit(int offset, int limit) {
        return iEbookMapper.queryAllByLimit(offset, limit);
    }
    
    /**
     * @Description 查询全部数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @return 对象列表
     */
    @Override
    public List<Ebook> queryAll() {
        return iEbookMapper.queryAll();
    }
    
     /**
     * @Description 实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebook 实例对象
     * @return 对象列表
     */
    @Override
    public List<Ebook> queryAll(Ebook ebook) {
        return iEbookMapper.queryAll(ebook);
    }
    
    /**
     * @Description 查询实体数量
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @return 数量
     */
    @Override
    public int queryCount() {
        return iEbookMapper.queryCount();
    }
    
    /**
     * @Description 实体作为筛选条件查询数量
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebook 实例对象
     * @return 数量
     */
    @Override
    public int queryCount(Ebook ebook) {
        return iEbookMapper.queryCount(ebook);
    }

    /**
     * @Description 实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebook 实例对象
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<Ebook> queryAllByLimit(Ebook ebook,int offset, int limit) {
        return iEbookMapper.querySomeByLimit(ebook,offset, limit);
    }
    
    /**
     * @Description 新增数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebook 实例对象
     * @return 影响行数
     */
    @Override
    public int insert(Ebook ebook) {
        return this.iEbookMapper.insert(ebook);
    }

    /**
     * @Description 修改数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebook 实例对象
     * @return 实例对象
     */
    @Override
    public Ebook update(Ebook ebook) {
        this.iEbookMapper.update(ebook);
        return queryById(ebook.getId());
    }

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer ebookId) {
        return iEbookMapper.deleteById(ebookId) > 0;
    }
}