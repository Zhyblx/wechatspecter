package com.zhangyibin.wechat.wechatapp;

import com.blade.kit.DateKit;
import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONObject;
import com.zhangyibin.wechat.util.QRCodeFrame;

public class WechatConfiguration {

    protected String BASE_URL = "https://webpush.weixin.qq.com/cgi-bin/mmwebwx-bin";
    protected String REDIRECT_URL = "https://webpush.weixin.qq.com/cgi-bin/mmwebwx-bin";
    protected String WEBPUSH_URL = "https://webpush.weixin.qq.com/cgi-bin/mmwebwx-bin";

    protected String SKEY = "e" + DateKit.getCurrentUnixTime();
    protected String SYNCKEY = "e" + DateKit.getCurrentUnixTime();
    protected String WXSID = "e" + DateKit.getCurrentUnixTime();
    protected String WXUIN = "e" + DateKit.getCurrentUnixTime();
    protected String PASS_TICKET = "e" + DateKit.getCurrentUnixTime();
    protected String DEVICEID = "e" + DateKit.getCurrentUnixTime();

    protected int tip = 0;
    protected QRCodeFrame qrCodeFrame = null;
    protected String cookie = null;
    protected JSONObject baseRequest = null;
    protected JSONObject syncKey = null;
    protected JSONObject user = null;
    protected JSONArray memberList = null;// 微信联系人列表
    protected JSONArray contactList = null;// 可聊天的联系人列表

}
