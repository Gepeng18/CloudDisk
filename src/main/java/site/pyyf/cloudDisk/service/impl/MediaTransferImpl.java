package site.pyyf.cloudDisk.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import site.pyyf.cloudDisk.service.IMediaTranfer;
import site.pyyf.cloudDisk.utils.Audio.AudioTransfer;
import site.pyyf.cloudDisk.utils.Video.VideoTransfer;

import java.io.File;

@Service
public class MediaTransferImpl implements IMediaTranfer {

    private static final Logger logger = LoggerFactory.getLogger(MediaTransferImpl.class);

    @Override
    public boolean tranferAudio(File src, File path) {
        try {
            boolean isSuccess = AudioTransfer.audioToMp3(src, path);
            if(isSuccess){
                logger.info("音乐 " + StringUtils.substringAfterLast(src.getAbsolutePath(),"\\") + " 转化成功");
                return true;
            }else{
                logger.info("音乐 " + StringUtils.substringAfterLast(src.getAbsolutePath(),"\\") + " 转化失败");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("音乐 " + StringUtils.substringAfterLast(src.getAbsolutePath(),"\\") + " 转化错误");
            return false;
        }

    }

    @Override
    public boolean tranferVideo(File src, File path) {
        try {
            boolean isSuccess = VideoTransfer.videoToMp4(src, path);
            if(isSuccess){
            logger.info("视频 " + StringUtils.substringAfterLast(src.getAbsolutePath(),"\\") + " 转化成功");
            return true;}
            else {
                logger.error("视频 " + StringUtils.substringAfterLast(src.getAbsolutePath(),"\\") + " 转化错误");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("视频 " + StringUtils.substringAfterLast(src.getAbsolutePath(),"\\") + " 转化错误");
            return false;
        }
    }


}
