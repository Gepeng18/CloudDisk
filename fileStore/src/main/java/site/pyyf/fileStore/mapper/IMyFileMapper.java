package site.pyyf.fileStore.mapper;

import site.pyyf.fileStore.entity.MyFile;
import org.apache.ibatis.annotations.Mapper;
import site.pyyf.fileStore.entity.UserStatistics;

import java.util.List;

/**
 * @InterfaceName: MyFileMapper
 * @Description: 与文件相关的数据库操作
 * @author: xw
 * @date 2020/1/26 21:55
 * @Version: 1.0
 **/
@Mapper
public interface IMyFileMapper {

    /**
     * @Description 添加文件
     * @Author xw
     * @Date 15:20 2020/2/26
     * @Param [myFile]
     * @return java.lang.Integer
     **/
    Integer addFileByUserId(MyFile myFile);

    /**
     * @Description 根据文件id修改文件
     * @Author xw
     * @Date 15:20 2020/2/26
     * @Param [myFile]
     * @return java.lang.Integer
     **/
    Integer updateFileByFileId(MyFile myFile);

    /**
     * @Description 根据文件的id删除文件
     * @Author xw
     * @Date 15:20 2020/2/26
     * @Param [myFileId]
     * @return java.lang.Integer
     **/
    Integer deleteByFileId(Integer myFileId);

    /**
     * @Description 根据父文件夹的id删除文件
     * @Author xw
     * @Date 15:20 2020/2/26
     * @Param [id]
     * @return java.lang.Integer
     **/
    Integer deleteByParentFolderId(Integer id);

    /**
     * @Description 根据文件的id获取文件
     * @Author xw
     * @Date 15:20 2020/2/26
     * @Param [myFileId]
     * @return com.moti.entity.MyFile
     **/
    MyFile getFileByFileId(Integer myFileId);

    /**
     * @Description 获得仓库根目录下的所有文件
     * @Author xw
     * @Date 23:53 2020/2/9
     * @Param [userId]
     * @return java.util.List<com.molihub.entity.MyFile>
     **/
    List<MyFile> getRootFilesByUserId(Integer userId);

   /**
    * Created by "gepeng" on 2020-03-81 11:47:35.
    * @Description 根据文件夹的ID和用户ID获取文件夹下面的文件
    * @param [userId, parentFolderId]
    * @return java.util.List<site.pyyf.fileStore.entity.MyFile>
    */
    List<MyFile> getFilesByUserIdAndParentFolderId(Integer userId,Integer parentFolderId);
    
    /**
     * @Description 根据类别获取文件
     * @Author xw
     * @Date 10:13 2020/2/26
     * @Param [userId, type]
     * @return java.util.List<com.moti.entity.MyFile>
     **/
    List<MyFile> getFilesByType(Integer userId,Integer type);

    /**
     * @Description 获取仓库的统计信息
     * @Author xw
     * @Date 21:47 2020/2/10
     * @Param [id]
     * @return com.molihub.entity.UserStatistics
     **/
    UserStatistics getCountStatistics(Integer userId);
}