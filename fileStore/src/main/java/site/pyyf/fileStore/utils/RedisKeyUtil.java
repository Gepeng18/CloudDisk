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
    // show:files:userID:fileID: -> list(Myfile)
    public static String getUserFilesKey(String userID, String fileID) {
        return PREFIX_SHOWFILES + SPLIT + userID + SPLIT + fileID;
    }

    // 某个用户的某个文件夹下的文件夹
    // show:files:userID:folderID: -> list(Folder)
    public static String getUserFoldersKey(String userID, String folderID) {
         return PREFIX_SHOWFOLDERS + SPLIT + userID + SPLIT + folderID;

    }
}
