package site.pyyf.fileStore.utils.OSS;
import lombok.AllArgsConstructor;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
class Uploadthread implements Runnable {

    private List<File> files;
    private OssUpload ossUpload;

    @Override
    public void run() {
        for (File file : files) {
            ossUpload.upload("cloudDisk/ebook",file);
        }
    }
}

public class MultiThreadUploadUtil {

    private List<List<File>> splitImages(String folderPath, int partNum) {
        List<List<File>> lists = new ArrayList<>();
        File[] files = new File(folderPath).listFiles();
        int everyPartNum = (int) Math.ceil(1.0d * files.length / partNum);
        for (int i = 0; i < partNum - 1; i++) {
            ArrayList<File> list = new ArrayList<>();
            for (int j = 0; j < everyPartNum; j++) {
                list.add(files[i * everyPartNum + j]);
            }
            lists.add(list);
        }

        ArrayList<File> list = new ArrayList<>();
        for (int j = (partNum-1) * everyPartNum; j < files.length; j++) {
            list.add(files[j]);
        }
        lists.add(list);

        return lists;
    }


    private void multiThread(String folderPath,int threadNum) throws InterruptedException {
        List<Thread> threads = Collections.synchronizedList(new ArrayList<>());
        long startTime = new Date().getTime();
        List<List<File>> lists = new MultiThreadUploadUtil().splitImages(folderPath, threadNum);
        OssUpload instance = OssUpload.getInstance();

        for (int i = 0; i < threadNum; i++) {
            Thread thread = new Thread(new Uploadthread(lists.get(i), instance));
            thread.start();
            threads.add(thread);
        }

        for(Thread thread:threads)
            thread.join();
        long endTime = new Date().getTime();
        System.out.println("一共用了： " + (endTime - startTime) / 1000 + "秒"); // 一共用了： 49秒
    }


    public void singleThread(String folderPath){
        long startTime = new Date().getTime();
        File[] files = new File(folderPath).listFiles();
        OssUpload instance = OssUpload.getInstance();
        for(File file:files){
            instance.upload("cloudDisk/ebook",file);
        }
        long endTime = new Date().getTime();
        System.out.println("一共用了： " + (endTime - startTime) / 1000 + "秒"); //一共用了： 129秒
    }

    public static void main(String[] args) throws Exception {
//        new multiThreadUploadUtil().singleThread("F:\\OneDrive\\笔记\\项目\\imgs\\community");
        new MultiThreadUploadUtil().multiThread("F:\\OneDrive\\笔记\\项目\\imgs\\community",10);
    }

}
