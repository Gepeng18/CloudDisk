package site.pyyf;

import site.pyyf.cloudDisk.utils.Video.VideoTransfer;

import java.io.File;

public class test {
    public static void main(String[] args) throws Exception {
        final File file = new File("G:\\FFOutput\\7.1 Spring Security.wmv");
        final File file2 = new File("G:\\FFOutput\\7.1 Spring Security.mp4");


        VideoTransfer.videoToMp4(file,file2);
    }
}
