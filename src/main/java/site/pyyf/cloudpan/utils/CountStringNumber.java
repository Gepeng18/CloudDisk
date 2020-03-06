package site.pyyf.cloudpan.utils;

public class CountStringNumber {
    public static int countString1(String str,String s) {
        int count = 0;
        while(str.indexOf(s) != -1) {
            str = str.substring(str.indexOf(s) + 1);
            count++;
        }
        return count;
    }
    public static int countString2(String str,String s) {
        String str1 = str.replaceAll(s, "");
        int len1 = str.length(),len2 = str1.length(),len3 = s.length();
        int count = (len1 - len2) / len3;
        return count;
    }

}
