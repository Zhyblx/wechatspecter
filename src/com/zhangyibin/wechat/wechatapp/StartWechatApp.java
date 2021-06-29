package com.zhangyibin.wechat.wechatapp;

public class StartWechatApp {
    /**
     * 启动微信幽灵程序
     *
     * @param args(参数)
     * @author zhangyibin
     */
    public static void main(String[] args) {
        String[] partnerBlackList = new String[]{"胖比", "1幢5幢楼管杨惠莉", "寒宁-仟金顶"};// 黑名单列表
        System.out.println("黑名单列表如下：");
        for (String name : partnerBlackList) {
            System.out.println(name);

        }
        String message = "你好！我正在休息，稍后回复你消息。";
        WechatApp wechatApp = WechatApp.getWechatApp();
        wechatApp.automaticallyReplyToBlacklistMessages(message, partnerBlackList);

    }
}
