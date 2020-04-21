package site.pyyf.fileStore.utils;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_SHOWFILES = "show:files";
    private static final String PREFIX_SHOWFOLDERS = "show:folders";
    private static final String PREFIX_SHARE = "share";

    // 某个分享
    // share：pwd -> "folder-folderID" or "file-fileID"
    public static String getShareKey(String pwd) {
        return PREFIX_SHARE + SPLIT + pwd;
    }

    // 某个用户的某个文件夹下的文件
    // show:files:folderID: -> list(Myfile)
    public static String getFilesKey(String fileID) {
        return PREFIX_SHOWFILES + SPLIT  + SPLIT + fileID;
    }

    // 某个用户的某个文件夹下的文件夹
    // show:files:folderID: -> list(Folder)
    public static String getFoldersKey( String folderID) {
         return PREFIX_SHOWFOLDERS + SPLIT + SPLIT + folderID;

    }
}
