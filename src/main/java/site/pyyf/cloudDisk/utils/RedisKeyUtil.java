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

    // 某用户的某个文件下的文件内容
    // userFile: files: userId: folderId -> list( file )
    public static String getFilesInUserFolder(int userId, int folderId) {
        return PREFIX_USERFILES + SPLIT + userId + SPLIT + folderId;
    }

    // 某用户的某个文件下的文件夹内容
    // userFile: folders: userId: folderId -> list( foler )
    public static String getFoldersInUserFolder(int userId, int folderId) {
        return PREFIX_USERFOLDERS + SPLIT + userId + SPLIT + folderId;
    }
}
