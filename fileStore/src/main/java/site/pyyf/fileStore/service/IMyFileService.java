package site.pyyf.fileStore.service;



import site.pyyf.fileStore.entity.MyFile;
import site.pyyf.fileStore.entity.UserStatistics;

import java.util.List;

/**
 * @InterfaceName: MyFileService
 * @Description: 文件业务层接口
 * @author: xw
 * @date 2020/1/26 22:15
 * @Version: 1.0
 **/
public interface IMyFileService {

    /**
     * @Description 根据文件的id删除文件
     * @Author xw
     * @Date 2020/2/9 15:49
     * @Param [myFileId]
     * @Return Integer
     */
    Integer deleteByFileId(Integer myFileId);

    /**
<<<<<<< HEAD
=======
     * @Description 根据父文件夹的id删除文件
     * @Author xw
     * @Date 2020/2/9 15:49
     * @Param [id]
     * @Return Integer
     */
    Integer deleteByParentFolderId(Integer id);

    /**
>>>>>>> 92c711a204ae08a936e2073da2b82198610d3895
     * @Description 添加文件
     * @Author xw
     * @Date 2020/2/9 15:50
     * @Param [myFile]
     * @Return Integer
     */
    Integer addFileByUserId(MyFile myFile);

    /**
     * @Description 根据文件id获得文件
     * @Author xw
     * @Date 2020/2/9 22:00
     * @Param [myFileId]
     * @Return com.molihub.entity.MyFile
     */

    MyFile getFileByFileId(Integer myFileId);
    /**
     * @Description 根据文件id修改文件
     * @Author xw
     * @Date 2020/2/9 15:55
     * @Param [record]
     * @Return Integer
     */
    Integer updateFile(MyFile record);

    /**
<<<<<<< HEAD
=======
     * @Description 获得仓库根目录下的所有文件
     * @Author xw
     * @Date 23:53 2020/2/9
     * @Param [userId]
     * @return java.util.List<com.molihub.entity.MyFile>
     **/
    List<MyFile> getRootFilesByUserId(Integer userId);

    /**
>>>>>>> 92c711a204ae08a936e2073da2b82198610d3895
     * @Description 根据父文件夹和用户ID获取文件夹下的文件
     * @Author xw
     * @Date 2020/2/9 16:34
     * @Param [parentFolderId] 文件夹id
     * @Return com.molihub.entity.FileFolder
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
