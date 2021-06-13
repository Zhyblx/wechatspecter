package com.zhangyibin.wechat.util;

public interface LoginInterface {

    public static final String UUID_URL = "https://login.weixin.qq.com/jslogin";
    public static final String SHOWQRCODEURL = "https://login.weixin.qq.com/qrcode/";
    public static final String LOGIN_URL = "https://login.weixin.qq.com/cgi-bin/mmwebwx-bin/login";
    public static final String WXSTATUS_CODE = "/webwxstatusnotify?lang=zh_CN&pass_ticket=";
    // Ios地址: https://webpush2.weixin.qq.com/cgi-bin/mmwebwx-bin
    // Android 地址：https://webpush.weixin.qq.com/cgi-bin/mmwebwx-bin
    public static final String WEBPUSH = "https://webpush.weixin.qq.com/cgi-bin/mmwebwx-bin";


}
