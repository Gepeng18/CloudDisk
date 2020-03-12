package site.pyyf;

import org.apache.commons.lang3.StringUtils;
import site.pyyf.cloudpan.utils.Audio.AudioTransfer;
import site.pyyf.cloudpan.utils.Video.VideoTransfer;

import java.io.File;

public class test {
    public static void main(String[] args) throws Exception {
        final File file = new File("G:\\FFOutput\\7.1 Spring Security.wmv");
        final File file2 = new File("G:\\FFOutput\\7.1 Spring Security.mp4");


        VideoTransfer.videoToMp4(file,file2);
    }
}
