package site.pyyf.fileStore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.pyyf.fileStore.entity.FileFolder;
import site.pyyf.fileStore.entity.MyFile;
import site.pyyf.fileStore.entity.UserStatistics;

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

    private static final Logger logger = LoggerFactory.getLogger(SystemController.class);

    /**
     * @return java.lang.String
     * @Description 前往我的云盘
     * @Author xw
     * @Date 23:28 2020/2/10
     * @Param [fId, fName, error, map]
     **/
    @GetMapping("/files")
    public String toUserPage(@RequestParam(value = "fId", defaultValue = "0") Integer fId,
                             Integer error, Map<String, Object> map) {
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
        folders = iFileFolderService.getFileFoldersByUserIdAndParentFolderId(loginUser.getUserId(), fId);
        files = iMyFileService.getFilesByUserIdAndParentFolderId(loginUser.getUserId(), fId);
        nowFolder = iFileFolderService.getFileFolderByFileFolderId(fId);
        if (fId == 0) {
            location.add(nowFolder);
        } else {
            //遍历查询当前目录
            FileFolder temp = nowFolder;
            while (temp.getParentFolderId() != 0) {
                temp = iFileFolderService.getFileFolderByFileFolderId(temp.getParentFolderId());
                location.add(temp);
            }
        }
        Collections.reverse(location);
        //获得统计信息
        UserStatistics statistics = iMyFileService.getCountStatistics(loginUser.getUserId());
        map.put("statistics", statistics);
        map.put("folders", folders);
        map.put("files", files);
        map.put("nowFolder", nowFolder);
        map.put("location", location);
        logger.info("云盘页面域中的数据显示成功");
        return "clouddisk/files";
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
        List<MyFile> files = iMyFileService.getFilesByType(loginUser.getUserId(), 1);
        //获得统计信息
        UserStatistics statistics = iMyFileService.getCountStatistics(loginUser.getUserId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("currUri", "doc");
        return "clouddisk/subFiles";
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
        List<MyFile> files = iMyFileService.getFilesByType(loginUser.getUserId(), 2);
        //获得统计信息
        UserStatistics statistics = iMyFileService.getCountStatistics(loginUser.getUserId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("currUri", "image");
        return "clouddisk/subFiles";
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
        List<MyFile> files = iMyFileService.getFilesByType(loginUser.getUserId(), 3);
        //获得统计信息
        UserStatistics statistics = iMyFileService.getCountStatistics(loginUser.getUserId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("currUri", "video");
        return "clouddisk/subFiles";
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
        List<MyFile> files = iMyFileService.getFilesByType(loginUser.getUserId(), 4);
        //获得统计信息
        UserStatistics statistics = iMyFileService.getCountStatistics(loginUser.getUserId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("currUri", "music");
        return "clouddisk/subFiles";
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
        List<MyFile> files = iMyFileService.getFilesByType(loginUser.getUserId(), 5);
        //获得统计信息
        UserStatistics statistics = iMyFileService.getCountStatistics(loginUser.getUserId());
        map.put("statistics", statistics);
        map.put("files", files);
        map.put("currUri", "other");
        return "clouddisk/subFiles";
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
        UserStatistics statistics = iMyFileService.getCountStatistics(loginUser.getUserId());
        statistics.setUser(iUserService.getUserById(loginUser.getUserId()));
        map.put("statistics", statistics);
        return "clouddisk/index";
    }

}
