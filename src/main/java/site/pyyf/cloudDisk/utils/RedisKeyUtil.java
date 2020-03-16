package site.pyyf.cloudDisk.utils;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_USERFILES = "user:files";
    private static final String PREFIX_USERFOLDERS = "user:folders";
    private static final String PREFIX_SHARE = "share";

    // 某个分享
    // share：pwd -> folder-folderID or file-fileID
    public static String getShareKey(String pwd) {
        return PREFIX_SHARE + SPLIT + pwd;
    }

}
