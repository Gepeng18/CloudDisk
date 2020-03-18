package site.pyyf.cloudDisk.service;

import org.springframework.web.multipart.MultipartFile;
import site.pyyf.cloudDisk.entity.MyFile;
import site.pyyf.cloudDisk.entity.User;

import java.util.List;

/**
 * @InterfaceName: MyFileService
 * @Description: 用户业务层接口
 * @author: xw
 * @date 2020/1/26 22:15
 * @Version: 1.0
 **/
public interface IFileStoreService {

    StringBuilder getFileContentByMyFile(MyFile file);

    void deleteFile(MyFile myFile);

    int getType(String type) ;

    void uploadTAudioFile(MultipartFile originalFile,MyFile fileItem) throws Exception;

    void uploadTVideoFile(MultipartFile originalFile,MyFile fileItem) throws Exception;

    void uploadImgFile(MultipartFile originalFile,MyFile fileItem) throws Exception;

    void uploadAudioAndVideoFile(MultipartFile originalFile,MyFile fileItem) throws Exception;

    void uploadOtherFile(MultipartFile originalFile,MyFile fileItem) throws Exception;

    void transeferFile(MyFile shareFile,MyFile insertFile);
}