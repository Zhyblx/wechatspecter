package com.zhangyibin.wechat.wechatapp;

import java.util.Scanner;

public class StartWechatApp {
    /**
     * 启动微信幽灵程序
     *
     * @param args(参数)
     * @author zhangyibin
     */
    public static void main(String[] args) {
        //String[] partnerBlackList = new String[]{"胖比", "1幢5幢楼管杨惠莉", "寒宁-仟金顶"};// 黑名单列表
        System.out.println("请设置黑名单好友(设置一组黑名单以逗号分割；例如：用户1,用户2,用户3)：");
        String partnerBlack = new Scanner(System.in).nextLine();
        String[] partnerBlackList = partnerBlack.split(",");
        System.out.println("黑名单好友列表如下：");
        for (String name : partnerBlackList) {
            System.out.println("--" + name);

        }
        System.out.println("设置默认回复消息？\n " +
                "1.设置：系统回复默认消息(可直接输入文字);\n " +
                "2.不设置：系统调用图灵机器人答复消息;");
        String message = new Scanner(System.in).nextLine();
        WechatApp wechatApp = WechatApp.getWechatApp();
        wechatApp.automaticallyReplyToBlacklistMessages(message, partnerBlackList);

    }
}

