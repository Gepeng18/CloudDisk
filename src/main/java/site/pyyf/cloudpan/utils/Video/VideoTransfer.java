package site.pyyf.cloudpan.utils.Video;

import java.io.File;

import ws.schild.jave.*;

public class VideoTransfer {
    public static boolean videoToMp4(File source, File target) throws Exception {

        //原参数
//        AudioAttributes audio = new AudioAttributes();
//        audio.setCodec("libmp3lame"); //音频编码格式
//        audio.setBitRate(new Integer(800000));
//        audio.setChannels(new Integer(1));
//        //audio.setSamplingRate(new Integer(22050));
//        VideoAttributes video = new VideoAttributes();
//        video.setCodec("libx264");//视频编码格式
//        video.setBitRate(new Integer(3200000));
//        video.setFrameRate(new Integer(15));//数字设置小了，视频会卡顿
//        EncodingAttributes attrs = new EncodingAttributes();
//        attrs.setFormat("mp4");
//        attrs.setAudioAttributes(audio);
//        attrs.setVideoAttributes(video);
//        Encoder encoder = new Encoder();
//        MultimediaObject multimediaObject = new MultimediaObject(source);

        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame"); //音频编码格式
        audio.setBitRate(new Integer(80000));
        audio.setChannels(new Integer(1));
        //audio.setSamplingRate(new Integer(22050));
        VideoAttributes video = new VideoAttributes();
        video.setCodec("libx264");//视频编码格式
        video.setBitRate(new Integer(160000));
        video.setFrameRate(new Integer(9));//数字设置小了，视频会卡顿
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp4");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        MultimediaObject multimediaObject = new MultimediaObject(source);
        try {
//                logger.info("avi转MP4 --- 转换开始:"+new Date());
            encoder.encode(multimediaObject, target, attrs);
//                logger.info("avi转MP4 --- 转换结束:"+new Date());
            return true;
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (InputFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;

        } catch (EncoderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;

        }
    }
}
