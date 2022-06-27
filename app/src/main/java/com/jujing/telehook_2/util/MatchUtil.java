package com.jujing.telehook_2.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchUtil {

    private static final String TAG = "MatchUtil";

    public static boolean hasEmoji(String content) {
        Pattern pattern = Pattern.compile("[^\\u0000-\\uFFFF]");
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    public static boolean hasChinese(String content) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(content);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }


    public static List<String> getAtUsername0(String content) {
        List<String> list = new ArrayList<>();


        String[] split = content.split("@");
        for (int j = 1; j < split.length; j++) {
            String s = split[j];
            LoggerUtil.logAll(TAG, "sss  41:" + s + "---->" + content);
            char[] chars = s.toCharArray();
            int index = 0;
            for (int i = 0; i < chars.length; i++) {
//            LoggerUtil.logI(TAG, "chars  40:" + chars[i] + "---->" + i);
//            if (chars[i] == '\n'||chars[i] == ' '){//大小写字母，数字，下划线
                if (!((chars[i] >= 'a' && chars[i] <= 'z')
                        || (chars[i] >= 'A' && chars[i] <= 'Z')
                        || (chars[i] >= '0' && chars[i] <= '9')
                        || chars[i] == '_')) {//大小写字母，数字，下划线
//                LoggerUtil.logAll(TAG, "chars  84:" + chars[i] + "---->" + i);

                    index = i;
                    break;
                }
            }
            String substring = "";
            if (chars.length > 0 && index == 0) {
                substring = s;
            } else {
                substring = s.substring(0, index);
            }
            list.add(substring);
        }




//        LoggerUtil.logAll(TAG, "substring  92:" + substring + "---->" + index);


        return list;

    }

    public static String getAtUsername(String content) {


        String[] split = content.split("@");
        String s = split[1];
        LoggerUtil.logAll(TAG, "sss  36:" + s + "---->" + content);
        char[] chars = s.toCharArray();
        int index = 0;
        for (int i = 0; i < chars.length; i++) {
//            LoggerUtil.logI(TAG, "chars  40:" + chars[i] + "---->" + i);
//            if (chars[i] == '\n'||chars[i] == ' '){//大小写字母，数字，下划线
            if (!((chars[i] >= 'a' && chars[i] <= 'z')
                    || (chars[i] >= 'A' && chars[i] <= 'Z')
                    || (chars[i] >= '0' && chars[i] <= '9')
                    || chars[i] == '_')) {//大小写字母，数字，下划线
//                LoggerUtil.logAll(TAG, "chars  84:" + chars[i] + "---->" + i);

                index = i;
                break;
            }
        }
        String substring = "";
        if (chars.length > 0 && index == 0) {
            substring = s;
        } else {
            substring = s.substring(0, index);
        }

//        LoggerUtil.logAll(TAG, "substring  92:" + substring + "---->" + index);

//        UsersAndChats.isStart = true;
//        SearchContactAction searchContactAction = new SearchContactAction();
//        searchContactAction.seachUsers(substring);

        return substring;

    }

    public static boolean isEmoji(String content) {
        if (content.startsWith("[") && content.endsWith("]")) {
            return true;
        }
        if (content.length() >= 2) {
            String substringStart = content.substring(0, 2);
            LoggerUtil.logAll("messagea", "substringStart  20--->" + substringStart);
            String substringEnd = content.substring(content.length() - 2, content.length());
            LoggerUtil.logAll("messagea", "substringEnd  22--->" + substringEnd);

            if ((hasEmoji(substringStart) || substringStart.startsWith("[")) && (hasEmoji(substringEnd) || substringEnd.endsWith("]"))) {
                return true;
            }
        }


        return false;

//        if ((content.contains("[") && content.contains("]")) || hasEmoji(content)) {
//            return true;
//        }
//        return false;
    }


    public static String splitFileName(String content) {
        String[] split = content.split("/");
        String s = split[split.length - 1];
        return s;
    }

}
