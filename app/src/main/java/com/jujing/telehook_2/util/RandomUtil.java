package com.jujing.telehook_2.util;

import java.util.Random;

/**
 * 取随机数工具类
 * Created by Vampire on 2017/6/13.
 */

public class RandomUtil {
    public static int randomNumber(int min, int max) {//前闭后闭
        int s = 0;
        try {
            Random random = new Random();
            s = random.nextInt(max) % (max - min + 1) + min;
            return s;
        } catch (Exception e) {

        }
        return min;
    }
}
