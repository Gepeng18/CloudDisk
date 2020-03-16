package site.pyyf.cloudDisk.controller;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.pyyf.cloudDisk.entity.FileFolder;
import site.pyyf.cloudDisk.entity.FileStoreStatistics;
import site.pyyf.cloudDisk.entity.MyFile;
import site.pyyf.cloudDisk.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SystemController
 * @Description: 系统页面跳转控制器
 * @author: xw
 * @date 2020/2/25 22:02
 * @Version: 1.0
 **/
@Controller
public class SystemController extends BaseController {

    Logger logger = LogUtils.getInstance(SystemController.class);

    /**
     * @return java.lang.String
     * @Description 前往我的网盘
     * @Author xw
     * @Date 23:28 2020/2/10
     * @Param [fId, fName, error, map]
     **/
    @GetMapping("/files")
    public String toFileStorePage(@RequestParam(value = "fId", defaultValue = "0") Integer fId,
                                  Integer error,
                                  Map<String, Object> map) {
        //判断是否包含错误信息
        if (error != null) {
            if (error == 1) {
                map.put("error", "添加失败！当前已存在同名文件夹");
            }
            if (error == 2) {
                map.put("error", "重命名失败！文件夹已存在");
            }
        }

        //包含的子文件夹
        List<FileFolder> folders = null;
        //包含的文件
        List<MyFile> files = null;
        //当前文件夹信息
        FileFolder nowFolder = null;
        //当前文件夹的相对路径
        List<FileFolder> location = new ArrayList<>();

        //当前为具体目录
        folders = iFileFolderService.getFileFolderByParentFolderId(fId);
        files = iMyFileService.getFilesByParentFolderId(fId);
        nowFolder = iFileFolderService.getFileFolderByFileFolderId(fId);
        if (fId == 0) {
            location.add(nowFolder);

        } else { //遍历查询当前目录
            FileFolder temp = nowFolder;
            while (temp.getParentFolderId() != 0) {
                temp = iFileFolderService.getFileFolderByFileFolderId(temp.getParentFolderId());
                location.add(temp);
            }
        }

        Collections.reverse(location);
        //获得统计信息
        FileStoreStatistics statistics = iMyFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("folders", folders);
        map.put("files", files);
        map.put("nowFolder", nowFolder);
        map.put("location", location);
        logger.info("网盘页面域中的数据:" + map);
        return "u-admin/files";
//
    }


    /**
     * @return java.lang.String
     * @Description 前往所有文档页面
     * @Author xw
     * @Date 10:26 2020/2/26
     * @Param [map]
     **/
    @GetMapping("/doc-files")
    public String toDocFilePage(Map<String, Object> map) {
        List<MyFile> files = iMyFileService.getFilesByType(loginUser.getFileStoreId(), 1);
        //获得统计信息
        FileStoreStatistics statistics = iMyFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("currUri", "doc");
        return "u-admin/subFiles";
    }

    /**
     * @return java.lang.String
     * @Description 前往所有图像页面
     * @Author xw
     * @Date 10:26 2020/2/26
     * @Param [map]
     **/
    @GetMapping("/image-files")
    public String toImageFilePage(Map<String, Object> map) {
        List<MyFile> files = iMyFileService.getFilesByType(loginUser.getFileStoreId(), 2);
        //获得统计信息
        FileStoreStatistics statistics = iMyFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("currUri", "image");
        return "u-admin/subFiles";
    }

    /**
     * @return java.lang.String
     * @Description 前往所有视频页面
     * @Author xw
     * @Date 10:26 2020/2/26
     * @Param [map]
     **/
    @GetMapping("/video-files")
    public String toVideoFilePage(Map<String, Object> map) {
        List<MyFile> files = iMyFileService.getFilesByType(loginUser.getFileStoreId(), 3);
        //获得统计信息
        FileStoreStatistics statistics = iMyFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("currUri", "video");
        return "u-admin/subFiles";
    }

    /**
     * @return java.lang.String
     * @Description 前往所有音频页面
     * @Author xw
     * @Date 10:26 2020/2/26
     * @Param [map]
     **/
    @GetMapping("/music-files")
    public String toMusicFilePage(Map<String, Object> map) {
        List<MyFile> files = iMyFileService.getFilesByType(loginUser.getFileStoreId(), 4);
        //获得统计信息
        FileStoreStatistics statistics = iMyFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("currUri", "music");
        return "u-admin/subFiles";
    }

    /**
     * @return java.lang.String
     * @Description 前往其他文件页面
     * @Author xw
     * @Date 10:26 2020/2/26
     * @Param [map]
     **/
    @GetMapping("/other-files")
    public String toOtherFilePage(Map<String, Object> map) {
        List<MyFile> files = iMyFileService.getFilesByType(loginUser.getFileStoreId(), 5);
        //获得统计信息
        FileStoreStatistics statistics = iMyFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("currUri", "other");
        return "u-admin/subFiles";
    }

    /**
     * @return java.lang.String
     * @Description 登录之后的用户主页
     * @Author xw
     * @Date 10:28 2020/2/26
     * @Param [map]
     **/
    @GetMapping("/index")
    public String index(Map<String, Object> map) {
        //获得统计信息
        FileStoreStatistics statistics = iMyFileService.getCountStatistics(loginUser.getFileStoreId());
        statistics.setFileStore(iFileStoreService.getFileStoreById(loginUser.getFileStoreId()));
        map.put("statistics", statistics);
        return "u-admin/index";
    }

    /**
     * @return java.lang.String
     * @Description 前往帮助页面
     * @Author xw
     * @Date 15:17 2020/2/26
     * @Param [map]
     **/
    @GetMapping("/help")
    public String helpPage(Map<String, Object> map) {
        //获得统计信息
        FileStoreStatistics statistics = iMyFileService.getCountStatistics(loginUser.getFileStoreId());
        map.put("statistics", statistics);
        return "u-admin/help";
    }
}
