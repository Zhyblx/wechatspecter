# 微信幽灵

微信幽灵(WeChatSpecter)基于网页版微信实现了一套使用Java语言编写的消息管理机制。

### 快速上手

工具上手极其简单；如以下案例：

    package com.zhangyibin.wechat.wechatapp;
    public class StartWechatApp {
    /**
    * 启动微信幽灵程序
    * @author zhangyibin
    */
    public static void main(String[] args) {
        String[] partnerBlackList = new String[]{"用户1", "用户2", "用户3"};// 黑名单列表
        String message = "你好！我正在休息，稍后回复你消息。";
        WechatApp wechatApp = WechatApp.getWechatApp();
        wechatApp.startWechatApp(message, partnerBlackList);
    
        }
    }

上面的例子就是根据用户的休息场景对特定的好友名单实现了统一的消息回复机制。

### 实现机制

· 获取到uuid；<br>
· 基于uuid可成功获得微信登陆二维码；<br>
· 使用手机微信扫码登录；<br>
· 初始化消息和微信好友列表；<br>
· 启动微信消息监听机制；<br>
· 获得好友消息内容可实现消息回复；<br>

### API参考

点击查看：[API文件](docs/index.html) <br>
下载文件(版本：1.1.1)：[WeChatSpecter.jar](out/artifacts/wechatspecter_jar/wechatspecter.jar)

### 版本更新

|版本号|发布日期|
|---|---|
|v1.1.1|2021年6月29日|
|v1.1.0|2021年6月22日|
|v1.0.2(Beta)|2021年6月21日|
|v1.0|2021年6月13日|

### 免责声明

本项目不建议应用于任何商业场景，如果因使用本工具的能力从而触犯了国家的相关法律法规后果需自行承担。

