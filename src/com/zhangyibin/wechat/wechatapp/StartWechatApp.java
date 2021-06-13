package com.zhangyibin.wechat.wechatapp;

public class StartWechatApp {
    /**
     * 启动微信幽灵程序
     *
     * @param args
     * @author zhangyibin
     */
    public static void main(String[] args) {
        WechatApp wechatApp = WechatApp.getWechatApp();
        wechatApp.startWechatApp();

    }
}
