package com.jujing.telehook_2.model.operate;

import android.text.TextUtils;

import com.jujing.telehook_2.Global;
import com.jujing.telehook_2.util.CrashHandler;
import com.jujing.telehook_2.util.LoggerUtil;
import com.jujing.telehook_2.util.WriteFileUtil;

public class JudgeCountryAndLangAction {

    private static final String TAG = "JudgeCountryAndLangAction";

    public static String judgeCountry(long from_id, String messageContent) {
        String country = "";
        try {
            country = WriteFileUtil.read(Global.COUNTRY_JUDGE + from_id);
            LoggerUtil.logI(TAG + from_id, "country 15----->" + country);
            if (!TextUtils.isEmpty(country)) {
                if (!country.equals("fail")) {
                    return country;
                }
            }
            String result = TranslateAction.post(from_id + "", messageContent, "en");//都翻译成英语
            LoggerUtil.logI(TAG + from_id, "result 20----->" + result + "---->" + messageContent);
            String[] s = result.split(" ");
            if (s.length < 3) {
                country = "fail";
            }
            for (int i = 0; i < s.length; i++) {
                String s1 = s[i].toLowerCase();
                if (s1.contains("china") || s1.contains("chinese") || s1.contains("beijing") || s1.contains("\uD83C\uDDE8\uD83C\uDDF3")) {//中国
                    country = "zh";
                    break;
                }
                if (s1.contains("america") || s1.contains("english")
                        || s1.contains("washionton")
                        || s1.contains("u.s") || s1.contains("usa")
                        || s1.contains("united sta") || s1.contains("\uD83C\uDDFA\uD83C\uDDF8")) {//美国
                    country = "en";
                    break;
                }
                if (s1.contains("uk") || s1.contains("u.k") || s1.contains("kingdom") || s1.contains("london") || s1.contains("\uD83C\uDDEC\uD83C\uDDE7")) {
                    country = "en_uk";
                    break;
                }
                if (s1.contains("japan") || s1.contains("japanese") || s1.contains("tokyo") || s1.contains("\uD83C\uDDEF\uD83C\uDDF5")) {
                    country = "jp";
                    break;
                }
                if (s1.contains("spain") || s1.contains("spaniard") || s1.contains("spanish") || s1.contains("madrid") || s1.contains("\uD83C\uDDEA\uD83C\uDDF8")) {
                    country = "xiba";
                    break;
                }
                if (s1.contains("brazil") || s1.contains("\uD83C\uDDE7\uD83C\uDDF7")) {
                    country = "baxi";
                    break;
                }
                if (s1.contains("portugal") || s1.contains("portu") || s1.contains("portuguese") || s1.contains("lisbon") || s1.contains("\uD83C\uDDF5\uD83C\uDDF9")) {
                    country = "puto";
                    break;
                }
                if (s1.contains("denmark") || s1.contains("danish") || s1.contains("copenhagen") || s1.contains("\uD83C\uDDE9\uD83C\uDDF0")) {
                    country = "danm";
                    break;
                }
                if (s1.contains("netherlands") || s1.contains("dutch") || s1.contains("amsterdam") || s1.contains("\uD83C\uDDF3\uD83C\uDDF1")) {
                    country = "helan";
                    break;
                }
                if (s1.contains("australia") || s1.contains("australian") || s1.contains("canberra") || s1.contains("\uD83C\uDDE6\uD83C\uDDFA")) {
                    country = "aod";
                    break;
                }
                if (s1.contains("thailand") || s1.contains("thailands") || s1.contains("thai") || s1.contains("bangkok") || s1.contains("\uD83C\uDDF9\uD83C\uDDED")) {
                    country = "tailan";
                    break;
                }
                if (s1.contains("zealand") || s1.contains("zealanders") || s1.contains("wellington") || s1.contains("\uD83C\uDDF3\uD83C\uDDFF")) {
                    country = "newel";
                    break;
                }
                if (s1.contains("india") || s1.contains("indians") || s1.contains("hindi") || s1.contains("delhi") || s1.contains("\uD83C\uDDEE\uD83C\uDDF3")) {
                    country = "yind";
                    break;
                }
                if (s1.contains("indonesia") || s1.contains("indones") || s1.contains("indonesian") || s1.contains("jakarta") || s1.contains("\uD83C\uDDEE\uD83C\uDDE9")) {
                    country = "yini";
                    break;
                }
                if (s1.contains("philippines") || s1.contains("filipino") || s1.contains("manila") || s1.contains("philippin") || s1.contains("\uD83C\uDDF5\uD83C\uDDED")) {
                    country = "pipli";
                    break;
                }

                if (s1.contains("iraq")||s1.contains("\uD83C\uDDEE\uD83C\uDDF6")){//伊拉克
                    country = "elak";
                }
                if (s1.contains("iran")||s1.contains("\uD83C\uDDEE\uD83C\uDDF7")){//伊朗
                    country = "elang";
                }
                if (s1.contains("bangladesh")||s1.contains("\uD83C\uDDE7\uD83C\uDDE9")){//孟加拉国
                    country = "mangj";
                }
                if (s1.contains("saudi")||s1.contains("arabia")||s1.contains("\uD83C\uDDF8\uD83C\uDDE6")){//沙特阿拉伯
                    country = "shate";
                }
                if (s1.contains("georgia")||s1.contains("\uD83C\uDDEC\uD83C\uDDEA")){//格鲁吉亚
                    country = "gelu";
                }
                if (s1.contains("russian")||s1.contains("\uD83C\uDDF7\uD83C\uDDFA")){//俄罗斯
                    country = "eluos";
                }
                if (s1.contains("italy") || s1.contains("italian") || s1.contains("rome") || s1.contains("\uD83C\uDDEE\uD83C\uDDF9")) {
                    country = "eteli";
                    break;
                }
                if (s1.contains("türkiye")||s1.contains("turkey")||s1.contains("turkiye")||s1.contains("\uD83C\uDDF9\uD83C\uDDF7")){//土耳其
                    country = "eluos";
                }
                if (s1.contains("chad")||s1.contains("\uD83C\uDDF9\uD83C\uDDE9")){//乍得
                    country = "zhade";
                }
                if (s1.contains("uzbekistan")||s1.contains("\uD83C\uDDFA\uD83C\uDDFF")){//乌兹别克斯坦
                    country = "wuzi";
                }
                if (s1.contains("canada")||s1.contains("\uD83C\uDDE8\uD83C\uDDE6")){//加拿大
                    country = "jianada";
                }
                if (s1.contains("ukraine")||s1.contains("\uD83C\uDDFA\uD83C\uDDE6")){//乌克兰
                    country = "wukelan";
                }

                if (s1.contains("france") || s1.contains("frenchman") || s1.contains("french") || s1.contains("paris") || s1.contains("\uD83C\uDDEB\uD83C\uDDF7")) {
                    country = "fr";
                    break;
                }


                if (s1.contains("germany") || s1.contains("german") || s1.contains("germen") || s1.contains("berlin") || s1.contains("\uD83C\uDDE9\uD83C\uDDEA")) {
                    country = "de";
                    break;
                }


                if (s1.contains("malaysia") || s1.contains("malaysian") || s1.contains("malay") || s1.contains("kuala") || s1.contains("\uD83C\uDDF2\uD83C\uDDFE")) {
                    country = "ms";
                    break;
                }
                if (s1.contains("singapore") || s1.contains("singapores") || s1.contains("\uD83C\uDDF8\uD83C\uDDEC")) {
                    country = "en_si";
                    break;
                }
            }


            if (!country.equals("")) {

            } else {
                country = "fail";
            }
        } catch (Exception e) {
            LoggerUtil.logI(TAG + from_id, "eee  122---->" + CrashHandler.getInstance().printCrash(e));
        }
        LoggerUtil.logI(TAG + from_id, "country 128----->" + country);
        WriteFileUtil.write(country, Global.COUNTRY_JUDGE + from_id);
        return country;
    }
}
