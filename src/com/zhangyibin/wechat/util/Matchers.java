package com.zhangyibin.wechat.util;


import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matchers {

    /**
     * 正则匹配器
     *
     * @param regex
     * @param str
     * @return
     * @author zhangyibin
     */
    public static @Nullable
    String match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);

        }
        return null;
    }

}