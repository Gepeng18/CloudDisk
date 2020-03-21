package site.pyyf.fileStore.service;


import site.pyyf.fileStore.entity.FileFolder;
import site.pyyf.fileStore.entity.MyFile;

import java.util.List;

/**
 * @Description 文件夹业务层接口
 * @Author xw
 * @Date 15:21 2020/2/26
 * @Param  * @param null
 * @return
 **/
public interface IFileFolderService {


    /**
     * @Description 根据文件夹的id删除文件夹
     * @Author xw
     * @Date 2020/2/9 16:38
     * @Param [fileFolderId] 文件夹的id
     * @Return java.lang.Integer
     */
    Integer deleteFileFolderById(Integer fileFolderId);

    /**
     * @Description 增加文件夹
     * @Author xw
     * @Date 2020/2/9 16:37
     * @Param [fileFolder] 文件夹对象
     * @Return java.lang.Integer
     */
    Integer addFileFolder(FileFolder fileFolder);


    /**
     * Created by "gepeng" on 2020-03-81 08:50:27.
     * @Description 根据父文件夹和用户ID获得获取文件夹下的文件夹 带redis缓存的
     * @param [puserId, arentFolderId]
     * @return java.util.List<site.pyyf.fileStore.entity.FileFolder>
     */
    List<FileFolder> getFileFoldersByUserIdAndParentFolderId(Integer userId,Integer parentFolderId);

    /**
     * @Description 根据文件夹的id获取文件夹
     * @Author xw
     * @Date 2020/2/9 22:23
     * @Param [fileFolderId]
     * @Return com.molihub.entity.FileFolder
     */
    FileFolder getFileFolderByFileFolderId(Integer fileFolderId);

    /**
<<<<<<< HEAD
=======
     * @Description 根据仓库Id获得仓库根目录下的所有文件夹
     * @Author xw
     * @Date 23:46 2020/2/9
     * @Param [userId]
     * @return java.util.List<com.molihub.entity.FileFolder>
     **/
    List<FileFolder> getRootFoldersByUserId(Integer userId);

    /**
>>>>>>> 92c711a204ae08a936e2073da2b82198610d3895
     * @Description 根据文件夹的id修改文件夹信息
     * @Author xw
     * @Date 2020/2/9 16:39
     * @Param [fileFolder] 文件夹对象
     * @Return java.lang.Integer
     */
    Integer updateFileFolderById(FileFolder fileFolder);



}
