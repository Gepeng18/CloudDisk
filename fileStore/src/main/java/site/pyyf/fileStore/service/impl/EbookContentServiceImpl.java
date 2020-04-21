package site.pyyf.fileStore.service.impl;

import site.pyyf.fileStore.entity.EbookContent;
import site.pyyf.fileStore.mapper.IEbookContentMapper;
import site.pyyf.fileStore.service.IEbookContentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description (EbookContent)表服务实现类
 *
 * @author "Gepeng"
 * @since 2020-04-21 16:02:11
 */
@Service
public class EbookContentServiceImpl implements IEbookContentService {
    @Resource
    private IEbookContentMapper iEbookContentMapper;

    /**
     * @Description 通过ID查询单条数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EbookContent queryById(Integer id) {
        return iEbookContentMapper.queryById(id);
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
    public List<EbookContent> queryAllByLimit(int offset, int limit) {
        return iEbookContentMapper.queryAllByLimit(offset, limit);
    }
    
    /**
     * @Description 查询全部数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @return 对象列表
     */
    @Override
    public List<EbookContent> queryAll() {
        return iEbookContentMapper.queryAll();
    }
    
     /**
     * @Description 实体作为筛选条件查询数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookContent 实例对象
     * @return 对象列表
     */
    @Override
    public List<EbookContent> queryAll(EbookContent ebookContent) {
        return iEbookContentMapper.queryAll(ebookContent);
    }
    
    /**
     * @Description 查询实体数量
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @return 数量
     */
    @Override
    public int queryCount() {
        return iEbookContentMapper.queryCount();
    }
    
    /**
     * @Description 实体作为筛选条件查询数量
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookContent 实例对象
     * @return 数量
     */
    @Override
    public int queryCount(EbookContent ebookContent) {
        return iEbookContentMapper.queryCount(ebookContent);
    }

    /**
     * @Description 实体作为筛选条件查询指定行
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookContent 实例对象
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<EbookContent> queryAllByLimit(EbookContent ebookContent,int offset, int limit) {
        return iEbookContentMapper.querySomeByLimit(ebookContent,offset, limit);
    }
    
    /**
     * @Description 新增数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookContent 实例对象
     * @return 影响行数
     */
    @Override
    public int insert(EbookContent ebookContent) {
        return this.iEbookContentMapper.insert(ebookContent);
    }

    /**
     * @Description 修改数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param ebookContent 实例对象
     * @return 实例对象
     */
    @Override
    public EbookContent update(EbookContent ebookContent) {
        this.iEbookContentMapper.update(ebookContent);
        return queryById(ebookContent.getId());
    }

    /**
     * @Description 通过主键删除数据
     * @author "Gepeng18"
     * @date 2020-04-21 16:02:11
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return iEbookContentMapper.deleteById(id) > 0;
    }
}