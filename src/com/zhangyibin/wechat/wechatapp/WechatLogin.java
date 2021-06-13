package com.zhangyibin.wechat.wechatapp;

import com.blade.kit.DateKit;
import com.blade.kit.StringKit;
import com.blade.kit.http.HttpRequest;
import com.blade.kit.json.JSON;
import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONObject;
import com.zhangyibin.wechat.util.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;


public class WechatLogin extends WechatConfiguration {
    final String UUID; //UUID作为常量程序初始化后不可改变；

    /*
     * 微信登陆构造器中首先获取 uuid
     */
    public WechatLogin() {
        Uuid uuId = new Uuid();
        UUID = uuId.getUuid();

    }

    /**
     * 显示微信二维码
     *
     * @author zhangyibin
     */
    public void showQrCode() {
        String url = LoginInterface.SHOWQRCODEURL + this.UUID;
        final File output = new File("temp.jpg");
        /*
         * 1.请求获得微信的登陆二维码（即，post请求"https://login.weixin.qq.com/qrcode/" + this.uuid）
         * 2.将二维码存储在根目录下面（即，new File("temp.jpg")）
         * 3.通过post请求获得微信登陆二维码，同时将二维码存储在output(根目录下面temp.jpg)文件夹中
         */
        HttpRequest.post(url,
                true,
                "t", "webwx", "_", DateKit.getCurrentUnixTime()).receive(output);

        /*
         * 1.判断：output不等于空，
         * 2.isFile()测试此抽象路径名表示的文件是否是一个标准文件
         * 3.exists()测试此抽象路径名表示的文件或目录是否存在
         */
        if (null != output && output.exists() && output.isFile()) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");//设置窗口外观
                        qrCodeFrame = new QRCodeFrame(output.getPath());//将二维码传到qrCodeFrame 窗体中，并显示出来

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            });
        }
    }

    /**
     * 等待登陆微信(即，扫描微信二维码登陆)
     *
     * @return 登陆状态码
     * @author zhangyibin
     */
    public String waitForLogin() {
        this.tip = 1;
        HttpRequest httpRequest = HttpRequest.get(LoginInterface.LOGIN_URL,
                true,
                "tip", this.tip, "uuid", this.UUID, "_", DateKit.getCurrentUnixTime());
        //System.out.println("[张益斌提示]- " + httpRequest.toString());
        String request = httpRequest.body();
        httpRequest.disconnect();
        if (null == request) {
            System.err.println("[张益斌提示]-扫描二维码验证失败!");
            return "";

        }
        String code = Matchers.match("window.code=(\\d+);", request);

        if (null == code) {
            System.err.println("[张益斌提示]-扫描二维码验证失败!");
            return "";

        } else {
            if (code.equals("201")) {
                System.out.println("[张益斌提示]-成功扫描,请在手机上点击确认以登录!");
                this.tip = 0;

            } else if (code.equals("200")) {
                System.out.println("[张益斌提示]-正在登录...");
                String pm = Matchers.match("window.redirect_uri=\"(\\S+?)\";", request);
                this.REDIRECT_URL = pm + "&fun=new";
                //System.out.println("[张益斌提示]-" + this.REDIRECT_URL);
                this.BASE_URL = this.REDIRECT_URL.substring(0, this.REDIRECT_URL.lastIndexOf("/"));
                //System.out.println("[张益斌提示]-" + this.BASE_URL);

            } else if (code.equals("408")) {
                System.err.println("[张益斌提示]-登录超时");

            } else {
                System.out.println("[张益斌提示]- 扫描code=" + code);

            }
        }
        return code;

    }

    /**
     * 关闭二维码窗口
     *
     * @author zhangyibin
     */
    public void closeQrWindow() {
        qrCodeFrame.dispose();

    }

    /**
     * 登陆微信(true:成功，false:失败)
     *
     * @return true/false
     * @author zhangyibin
     */
    public boolean login() {
        HttpRequest httpRequest = HttpRequest.get(this.REDIRECT_URL);
        //System.out.println("[张益斌提示]- " + httpRequest.toString());
        String request = httpRequest.body();
        this.cookie = CookieUtil.getCookie(httpRequest);
        httpRequest.disconnect();
        if (StringKit.isBlank(request)) {
            return false;

        }
        this.SKEY = Matchers.match("<skey>(\\S+)</skey>", request);
        this.WXSID = Matchers.match("<wxsid>(\\S+)</wxsid>", request);
        this.WXUIN = Matchers.match("<wxuin>(\\S+)</wxuin>", request);
        this.PASS_TICKET = Matchers.match("<pass_ticket>(\\S+)</pass_ticket>", request);
        System.out.println("[张益斌提示]-SKEY:" + this.SKEY);
        System.out.println("[张益斌提示]-WXSID:" + this.WXSID);
        System.out.println("[张益斌提示]-WXUIN:" + this.WXUIN);
        System.out.println("[张益斌提示]-PASS_TICKET:" + this.PASS_TICKET);
        this.baseRequest = new JSONObject();
        this.baseRequest.put("Uin", this.WXUIN);
        this.baseRequest.put("Sid", this.WXSID);
        this.baseRequest.put("Skey", this.SKEY);
        this.baseRequest.put("DeviceID", this.DEVICEID);
        return true;

    }

    /**
     * 微信登陆初始化(true:成功，false:失败)
     *
     * @return true/false
     * @author zhangyibin
     */
    public boolean initialization() {
        String url = this.BASE_URL
                + "/webwxinit?r="
                + DateKit.getCurrentUnixTime()
                + "&pass_ticket="
                + this.PASS_TICKET
                + "&skey="
                + this.SKEY;
        JSONObject jsonObject_body = new JSONObject();
        jsonObject_body.put("BaseRequest", this.baseRequest);
        HttpRequest httpRequest = HttpRequest.post(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Cookie", this.cookie)
                .send(jsonObject_body.toString());
        //System.out.println("[张益斌提示]- " + httpRequest);
        String request = httpRequest.body();
        httpRequest.disconnect();
        if (StringKit.isBlank(request)) {
            return false;

        }
        try {
            JSONObject jsonObject = (JSONObject) JSON.parse(request);
            if (null != jsonObject) {
                JSONObject baseResponse = (JSONObject) jsonObject.get("BaseResponse");
                if (null != baseResponse) {
                    int ret = baseResponse.getInt("Ret", -1);
                    if (ret == 0) {
                        this.syncKey = (JSONObject) jsonObject.get("SyncKey");
                        this.user = (JSONObject) jsonObject.get("User");
                        StringBuffer stringBuffer = new StringBuffer();
                        JSONArray list = (JSONArray) this.syncKey.get("List");
                        for (int i = 0, len = list.size(); i < len; i++) {
                            JSONObject item = (JSONObject) list.get(i);
                            stringBuffer.append("|"
                                    + item.getInt("Key", 0)
                                    + "_"
                                    + item.getInt("Val", 0));
                        }

                        this.SYNCKEY = stringBuffer.substring(1);
                        return true;

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;

    }

    /**
     * 微信状态通知(true:成功，false:失败)
     *
     * @return true/false
     * @author zhangyibin
     */

    public boolean statusNotify() {
        String Url = this.BASE_URL + LoginInterface.WXSTATUS_CODE + this.PASS_TICKET;
        JSONObject jsonObject_body = new JSONObject();
        jsonObject_body.put("BaseRequest", this.baseRequest);
        jsonObject_body.put("Code", 3);
        jsonObject_body.put("FromUserName", this.user.getString("UserName"));
        jsonObject_body.put("ToUserName", this.user.getString("UserName"));
        jsonObject_body.put("ClientMsgId", DateKit.getCurrentUnixTime());
        HttpRequest httpRequest = HttpRequest.post(Url)
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Cookie", this.cookie)
                .send(jsonObject_body.toString());
        //System.out.println("[张益斌提示]-" + httpRequest);
        String request = httpRequest.body();
        httpRequest.disconnect();
        if (StringKit.isBlank(request)) {
            return false;

        }
        try {
            JSONObject jsonObject = (JSONObject) JSON.parse(request);
            JSONObject baseResponse = (JSONObject) jsonObject.get("BaseResponse");
            if (null != baseResponse) {
                int ret = baseResponse.getInt("Ret", -1);
                return ret == 0;

            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;

    }

    /**
     * 获取微信联系人(true:成功，false:失败)
     *
     * @return true/false
     * @author zhangyibin
     */
    public boolean getContact() {
        String url = this.BASE_URL
                + "/webwxgetcontact?pass_ticket="
                + this.PASS_TICKET
                + "&skey="
                + this.SKEY
                + "&r="
                + DateKit.getCurrentUnixTime();
        JSONObject jsonObject_body = new JSONObject();
        jsonObject_body.put("BaseRequest", this.baseRequest);
        HttpRequest httpRequest = HttpRequest.post(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Cookie", this.cookie)
                .send(jsonObject_body.toString());
        //System.out.println("[张益斌提示]-" + request);
        String request = httpRequest.body();
        httpRequest.disconnect();
        if (StringKit.isBlank(request)) {
            return false;

        }
        try {
            JSONObject jsonObject = (JSONObject) JSON.parse(request);
            JSONObject baseResponse = (JSONObject) jsonObject.get("BaseResponse");
            if (null != baseResponse) {
                int ret = baseResponse.getInt("Ret", -1);
                if (ret == 0) {
                    this.memberList = (JSONArray) jsonObject.get("MemberList");
                    this.contactList = new JSONArray();
                    if (null != this.memberList) {
                        for (int i = 0, len = this.memberList.size(); i < len; i++) {
                            JSONObject jsonObject_contact = (JSONObject) this.memberList.get(i);
                            /*
                             * 1.获取公众号联系列表
                             * 2.获取服务号联系列表
                             */
                            if (jsonObject_contact.getInt("VerifyFlag", 0) == 8) {
                                continue;

                            }
                            /*
                             * 获取群联系列表
                             */
                            if (jsonObject_contact.getString("UserName").indexOf("@@") != -1) {
                                continue;

                            }
                            /*
                             * 获取微信好友列表
                             */
                            if (jsonObject_contact.getString("UserName").equals(this.user.getString("UserName"))) {
                                continue;

                            }
                            this.contactList.add(jsonObject_contact);

                        }
                        return true;

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;

    }
}
