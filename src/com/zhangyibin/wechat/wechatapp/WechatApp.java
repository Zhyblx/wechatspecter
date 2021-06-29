package com.zhangyibin.wechat.wechatapp;

public class WechatApp extends WechatMsg {

    private WechatApp() {
        /*
         * 1.设置指定键对值的系统属性
         * 2.System.setProperty 相当于一个静态变量存在内存里面
         * 3.System.setProperty("Property1", "abc");
         * 4.这样就把第一个參数设置成为系统的全局变量！能够在项目的不论什么一个地方 通过System.getProperty("变量")来获得
         */
        System.setProperty("jsse.enableSNIExtension", "false");

    }

    public static WechatApp getWechatApp() {
        return new WechatApp();

    }

    /**
     * 启动微信App后，根据设置好友黑名单进行自动回复消息
     *
     * @param message(消息内容)
     * @param partnerBlackList(好友黑名单列表)
     * @author zhangyibin
     */
    public void automaticallyReplyToBlacklistMessages(String message, String... partnerBlackList) {
        WechatApp wechatApp = WechatApp.getWechatApp();
        this.startWeChat(wechatApp); //启动微信
        wechatApp.listenMsgMode(message, partnerBlackList);// 监听微信消息

    }

    /**
     * 启动微信
     *
     * @param wechatApp
     */
    private void startWeChat(WechatApp wechatApp) {
        String uuid = wechatApp.UUID;
        /*
         * 1.如没有获取到uuid，返回"UUID获取失败"；
         * 2.获取到uuid，则显示微信的登陆二维码
         * 3.扫描微信二维码登陆；如果code=200那么说明微信登陆成功，否则循环每2秒请求登陆一次，直到登陆成功；
         * 4.登陆成功后关闭二维码窗口
         * 5.如果微信登陆失败,提示后则退出程序；
         * 6.登陆成功：
         *   a.初始化微信失败，则退出程序；
         *   b.初始化微信成功，则开启微信通知状态；
         *   c.通知失败，则退出程序；
         *   d.通知成功，则开启状态通知成功；
         *   e.状态通知成功，获取联系人；
         *   f.获取联系人失败，则退出程序；
         *   g.获取联系人成功，提示成功后并展示好友人数；
         *   h.监听微信消息；
         *
         */
        if (null == uuid) {
            System.err.println("[张益斌提示]-UUID获取失败");

        } else {
            System.out.println("[张益斌提示]-UUID为" + uuid);
            wechatApp.showQrCode();
            while (!wechatApp.waitForLogin().equals("200")) {
                try {
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
            }
            wechatApp.closeQrWindow();
            if (!wechatApp.login()) {
                System.err.println("[张益斌提示]-微信登录失败");
                return;

            }
            System.out.println("[张益斌提示]-微信登录成功");
            if (!wechatApp.initialization()) {
                System.err.println("[张益斌提示]-微信初始化失败");
                return;

            }
            System.out.println("[张益斌提示]-微信初始化成功");
            if (!wechatApp.statusNotify()) {
                System.err.println("[张益斌提示]-开启状态通知失败");
                return;

            }
            System.out.println("[张益斌提示]-开启状态通知成功");
            if (!wechatApp.getContact()) {
                System.err.println("[张益斌提示]-获取联系人失败");
                return;

            }
            System.out.println("[张益斌提示]-获取联系人成功");
            System.out.println("[张益斌提示]-共有" + wechatApp.contactList.size() + "位联系人");

        }
    }
}
