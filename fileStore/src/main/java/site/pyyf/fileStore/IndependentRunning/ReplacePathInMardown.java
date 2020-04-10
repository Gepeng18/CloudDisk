package site.pyyf.fileStore.IndependentRunning;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReplacePathInMardown {

    /**
     * Created by "gepeng" on 2020-03-82 08:34:18.
     *
     * @param [filePath, dst,delete]
     * @return void
     * @Description 将filePath处的markdown文件中的本地路径替换为dst，同时本地路径处的图片将被复制到markdown同级目录的imgs文件夹下,delete表示是否删除原图片
     */

    public static void replace(String filePath, String dst,boolean delete) throws Exception {
        /* ------------------- 将文件名和后缀分离以便后面使用 ----------------- */
        String fileName = filePath.split("\\\\")[filePath.split("\\\\").length - 1];
        String fileNameWithoutSuffix = fileName.split("\\.")[0];
        String suffix = fileName.split("\\.")[1];

        /* ------------------- 读入文件中的内容 ----------------- */
        BufferedReader bfr = new BufferedReader(new FileReader(filePath));
        StringBuilder content = new StringBuilder();
        String buffer = null;
        while ((buffer = bfr.readLine()) != null)
            content.append(buffer).append("\n");

        /* ------------------- 开始替换 ----------------- */
        StringBuilder result = new StringBuilder();
        int p1 = 0;
        while (p1 < content.length()) {
            //是本地路径 p1处为]
            if (p1 + 3 < content.length() && content.charAt(p1) == ']' && (content.charAt(p1 + 1) == '(') && isLocalPrefix(content.charAt(p1 + 2), content.charAt(p1 + 3))) {
                //从本地路径开始找.和)的索引，但不能无限找下去，所以规定路径长度最大为100
                int start = p1 + 2;
                int pointIndex = 0;
                int rightBracketIndex = 0;
                while (start < content.length() && start - p1 - 2 < 100) {
                    if (content.charAt(start) == '.')
                        pointIndex = start;
                    if (content.charAt(start) == ')')
                        rightBracketIndex = start;
                    start++;
                    if (pointIndex != 0 && rightBracketIndex != 0)
                        break;
                }
                boolean replaced = false;
                //表明本地路径的方圆100字节里有.和),并且相差不能超过10(因为他们之间的距离就是文件扩展名，基本都小于10)
                if (pointIndex != 0 && rightBracketIndex != 0 && (rightBracketIndex - pointIndex < 10)) {
                    List<String> imgSufix = new ArrayList<>(Arrays.asList("png", "jpg", "jpeg"));
                    if (imgSufix.contains(content.substring(pointIndex + 1, rightBracketIndex))) {
                        //表明从p2+2开始到rightBracketIndex-1的字符串应该是个路径(也不一定，但这里就不判断了，直接看文件是否存在)
                        File imgFile = null;
                        System.out.println("content[p1] = " + content.charAt(p1));
                        if (content.charAt(p1 + 2) == '.')
                            imgFile = new File(new File(filePath).getParent() + "\\" + content.substring(p1 + 2, rightBracketIndex));
                        else
                            imgFile = new File(content.substring(p1 + 2, rightBracketIndex));
                        if (imgFile.exists()) {
                            replaced = true;
                            String imgName = imgFile.getName();
                            String insertOssPath = "](" + dst + "/" + imgName + ")";
                            result.append(insertOssPath);
                            if (!new File(new File(filePath).getParent() + "\\" + "img\\" + fileNameWithoutSuffix).exists())
                                new File(new File(filePath).getParent() + "\\" + "img\\" + fileNameWithoutSuffix).mkdirs();
                            /* ------------------- 如果图片已经在img目录下则不进行复制文件 ----------------- */
                            if (!content.substring(p1 + 2,p1+2+".\\img\\".length()).equals(".\\img\\"))
                                cutFile(imgFile.getAbsolutePath(), new File(filePath).getParent() + "\\" + "img\\" + fileNameWithoutSuffix + "\\" + imgName,delete);
                            p1 = rightBracketIndex + 1;
                        }
                    }
                }

                //如果没有替换，则表明由于某些原因没有替换成功，则p1++继续判断下一个字符
                if (!replaced)
                    p1++;
            } else if (p1 + 11 < content.length() && content.substring(p1,p1+10).equals("<img src=\"")&& isLocalPrefix(content.charAt(p1 + 10), content.charAt(p1 + 11))) {
                    //是本地路径 p1处为<
                    //从本地路径开始找"的索引，但不能无限找下去，所以规定路径长度最大为100
                    int start = p1 + 11;
                    int pointIndex = 0;
                    int quoteIndex = 0;
                    while (start < content.length() && start - p1 - 10 < 100) {
                        if (content.charAt(start) == '.')
                            pointIndex = start;
                        if (content.charAt(start) == '"')
                            quoteIndex = start;
                        start++;
                        if (pointIndex != 0 && quoteIndex != 0)
                            break;
                    }
                    boolean replaced = false;
                    //表明本地路径的方圆100字节里有.和),并且相差不能超过10(因为他们之间的距离就是文件扩展名，基本都小于10)
                    if (pointIndex != 0 && quoteIndex != 0 && (quoteIndex - pointIndex < 10)) {
                        List<String> imgSufix = new ArrayList<>(Arrays.asList("png", "jpg", "jpeg"));
                        if (imgSufix.contains(content.substring(pointIndex + 1, quoteIndex))) {
                            //表明从p2+2开始到rightBracketIndex-1的字符串应该是个路径(也不一定，但这里就不判断了，直接看文件是否存在)
                            File imgFile = null;
                            if (content.charAt(p1 + 10) == '.')
                                imgFile = new File(new File(filePath).getParent() + "\\" + content.substring(p1 + 10, quoteIndex));
                            else
                                imgFile = new File(content.substring(p1 + 10, quoteIndex));
                            if (imgFile.exists()) {
                                replaced = true;
                                String imgName = imgFile.getName();
                                String insertOssPath = "<img src=\"" + dst + "/" + imgName + "\"";
                                result.append(insertOssPath);
                                if (!new File(new File(filePath).getParent() + "\\" + "img\\" + fileNameWithoutSuffix).exists())
                                    new File(new File(filePath).getParent() + "\\" + "img\\" + fileNameWithoutSuffix).mkdirs();

                                /* ------------------- 如果图片已经在img目录下则不进行复制文件 ----------------- */
                                if (!content.substring(p1 + 2,p1+2+".\\img\\".length()).equals(".\\img\\"))
                                cutFile(imgFile.getAbsolutePath(), new File(filePath).getParent() + "\\" + "img\\" + fileNameWithoutSuffix + "\\" + imgName,delete);
                                p1 = quoteIndex + 1;
                            }
                        }
                    }

                    //如果没有替换，则表明由于某些原因没有替换成功，则p1++继续判断下一个字符
                    if (!replaced)
                        p1++;
            } else {
                result.append(content.charAt(p1));
                p1++;
            }
        }


    final File file = new File(new File(filePath).getParent() + "//" + fileNameWithoutSuffix + "_oss" + "." + suffix);
    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(result.toString());
        bw.flush();
        bw.close();
}

    private static void cutFile(String oriPath, String dstPath,boolean delete) {
        try (InputStream is = new FileInputStream(oriPath);
             OutputStream os = new FileOutputStream(dstPath)) {
            //3、操作 (分段读取)
            byte[] flush = new byte[1024]; //缓冲容器
            int len = -1; //接收长度
            while ((len = is.read(flush)) != -1) {
                os.write(flush, 0, len); //分段写出
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(delete)
            new File(oriPath).delete();
    }


    //判断是否是本地路径的开头，如D: 或者/ 或者 ./ 或者..
    private static boolean isLocalPrefix(char c, char d) {
        return (Character.isAlphabetic(c) && d == ':') || c == '\\' || (c == '.' && d == '\\') || (c == '.' && d == '.');
    }

}
