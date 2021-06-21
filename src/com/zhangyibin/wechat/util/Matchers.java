package com.zhangyibin.wechat.util;


import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matchers {
    public static @Nullable
    /**
     * 正则匹配器
     *
     * @param regex(正则表达式)
     * @param str(文本内容)
     * @return null
     * @author zhangyibin
     */
    String match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(1);

        }
        return null;
    }

}