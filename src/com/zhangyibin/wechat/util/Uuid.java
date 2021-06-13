package com.zhangyibin.wechat.util;

import com.blade.kit.DateKit;
import com.blade.kit.StringKit;
import com.blade.kit.http.HttpRequest;

public class Uuid {

    private String uuid = "";

    public Uuid() {
        HttpRequest httpRequest =
                HttpRequest.get(LoginInterface.UUID_URL,
                        true,
                        "appid", "wx782c26e4c19acffb", "fun", "new", "lang", "zh_CN", "_", DateKit.getCurrentUnixTime());
        String request = httpRequest.body();
        httpRequest.disconnect();
        //如果返回的uuid信息不等于空
        if (StringKit.isNotBlank(request)) {
            //将登陆信息赋值给code
            String code = Matchers.match("window.QRLogin.code = (\\d+);", request);
            //如果登陆信息不等于空，那么微信登陆成功，否则反馈错误状态码
            if (null != code) {
                if (code.equals("200")) {
                    this.uuid = Matchers.match("window.QRLogin.uuid = \"(.*)\";", request);

                } else {
                    System.err.println("[张益斌提示]-错误的状态码:" + code);

                }
            }
        }
    }

    /**
     * 返回uuid(唯一的识别码)
     *
     * @return uuid
     * @author zhangyibin
     */

    public String getUuid() {
        return uuid;

    }
}
