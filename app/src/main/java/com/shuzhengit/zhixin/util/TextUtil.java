package com.shuzhengit.zhixin.util;

/**
 * Created by 江苏镇江树正科技 .
 * Author：袁从斌 on 2017/11/1 09:32
 * E-mail:yuancongbin@gmail.com
 * <p>
 * 功能描述:
 */

public class TextUtil {
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }
}
