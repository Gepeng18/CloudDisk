package site.pyyf.utils;

import java.io.*;

public class Path2Oss {
    public static void main(String[] args) throws Exception {
        String filePath = "F:\\Projects\\Java\\tianti\\ebook\\src\\main\\resources\\容器.md";
        String fileName = filePath.split("\\\\")[filePath.split("\\\\").length - 1];
        String fileNameWithoutSuffix = fileName.split("\\.")[0];
        String src = ".\\imgs\\" + fileNameWithoutSuffix + "\\";
        String dst = "https://pyyf.oss-cn-hangzhou.aliyuncs.com/ebook/" + fileNameWithoutSuffix + "\\";
        replace(filePath,src,dst);
    }


    public static void replace(String filePath, String src, String dst) throws Exception {

        String fileName = filePath.split("\\\\")[filePath.split("\\\\").length - 1];
        String fileNameWithoutSuffix = fileName.split("\\.")[0];
        String suffix = fileName.split("\\.")[1];


        BufferedReader bfr = new BufferedReader(new FileReader(filePath));
        StringBuilder content = new StringBuilder();
        String buffer = null;
        while ((buffer = bfr.readLine()) != null)
            content.append(buffer).append("\n");
        StringBuilder result = new StringBuilder();
        int p1 = 0;
        int p2 = 0;
        while (p1 < content.length() && p2 < content.length()) {
            int start = 0;
            //当第一个字母满足条件时
            while (p2 < content.length() && start < src.length()) {
                if (content.charAt(p2) == src.charAt(start)) {
                    p2++;
                    start++;
                } else {
                    break;
                }
            }
            if (start == src.length()) {

                result.append(dst);
                p1 = p1 + start;
                boolean findPng = false;
                int p3 = p1;
                while (p3 < content.length()) {
                    if (p3 == p1 + 40)
                        break;
                    if (content.charAt(p3) == '.' && p3 + 3 < content.length()) {

                        if (content.charAt(p3 + 1) == 'p' && content.charAt(p3 + 2) == 'n' && content.charAt(p3 + 3) == 'g') {
                            p3 = p3 + 3;
                            findPng = true;
                            break;
                        }
                    }
                    p3++;

                }
                if (findPng) {
                    StringBuilder imgName = new StringBuilder();
                    for (int i = p1; i <= p3; i++)
                        imgName.append(content.charAt(i));
                }

            } else {
                result.append(content.charAt(p1));
                p1++;
            }
            p2 = p1;
        }
        final File file = new File(new File(filePath).getParent() + "//" + fileNameWithoutSuffix + "_ossmd" + "." + suffix);

        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(result.toString());
        bw.flush();
        bw.close();
    }


}
