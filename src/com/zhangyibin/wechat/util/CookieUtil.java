package com.zhangyibin.wechat.util;

import com.blade.kit.http.HttpRequest;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class CookieUtil {

    /**
     * 网页版微信登陆所使用的Cookie工具
     * 作用：获取浏到登陆使用的cookie
     *
     * @param request(Http请求)
     * @return 返回cookie代码
     * @author zhangyibin
     */
    public static String getCookie(HttpRequest request) {
        HttpURLConnection httpURLConnection = request.getConnection();
        Map<String, List<String>> resHeaders = httpURLConnection.getHeaderFields();
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
            String name = entry.getKey();
            if (name == null)
                continue; // http/1.1 line
            List<String> values = entry.getValue();
            if (name.equalsIgnoreCase("Set-Cookie")) {
                for (String value : values) {
                    if (value == null) {
                        continue;
                    }
                    String cookie = value.substring(0, value.indexOf(";") + 1);
                    stringBuffer.append(cookie);
                }
            }
        }
        return stringBuffer.toString();

    }
}
