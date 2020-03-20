package site.pyyf.fileStore.service;

import java.io.File;

public interface IMediaTranfer {
    public boolean tranferAudio(File src, File path);
    public boolean tranferVideo(File src,File path);

}
