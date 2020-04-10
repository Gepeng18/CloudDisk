package site.pyyf.fileStore.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class CloudDiskUtil {

    static SimpleDateFormat dstDateFormat = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
    static SimpleDateFormat srcDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.UK);

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密
    // hello -> abc123def456
    // hello + 3e4a8 -> abc123def456abc
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }


    public static boolean isChineseNumberAlpha(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        if(Character.isDigit(c))
            return true;
        if(Character.isDigit(c))
            return true;
        return false;
    }

    public static int countStringNumber1(String str,String s) {
        int count = 0;
        while(str.indexOf(s) != -1) {
            str = str.substring(str.indexOf(s) + 1);
            count++;
        }
        return count;
    }
    public static int countStringNumber2(String str,String s) {
        String str1 = str.replaceAll(s, "");
        int len1 = str.length(),len2 = str1.length(),len3 = s.length();
        int count = (len1 - len2) / len3;
        return count;
    }

    public static String formatBaseDataString(String s) throws ParseException {
        return dstDateFormat.format(srcDateFormat.parse(s));
    }
}
