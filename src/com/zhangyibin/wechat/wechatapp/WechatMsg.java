package com.zhangyibin.wechat.wechatapp;

import com.blade.kit.DateKit;
import com.blade.kit.StringKit;
import com.blade.kit.http.HttpRequest;
import com.blade.kit.json.JSON;
import com.blade.kit.json.JSONArray;
import com.blade.kit.json.JSONObject;
import com.zhangyibin.wechat.util.*;
import com.zhangyibin.wechat.robot.Turing;

public class WechatMsg extends WechatLogin {

    /**
     * 检查微信消息
     *
     * @return 返回截取消息内容
     * @author zhangyibin
     */
    public int[] messageSyncCheck() {
        int[] array = new int[2];
        String url = super.WEBPUSH_URL + "/synccheck";
        JSONObject jsonObject_body = new JSONObject();
        jsonObject_body.put("BaseRequest", this.baseRequest);
        HttpRequest httpRequest = null;
        String request = null;
        httpRequest = HttpRequest.get(url,
                true,
                "r"
                , DateKit.getCurrentUnixTime() + StringKit.getRandomNumber(5)
                , "skey"
                , this.SKEY
                , "uin"
                , this.WXUIN
                , "sid"
                , this.WXSID
                , "deviceid"
                , this.DEVICEID
                , "synckey"
                , this.SYNCKEY
                , "_"
                , System.currentTimeMillis()).header("Cookie"
                , this.cookie);
        /*
         * 无穷循环的作用是排错(当request为空的情况下，会一直循环获取微信消息内容，直到有内容的才跳出循环)；
         * 1.当request为空的时候，程序就开始报错了；
         * 2.循环处理：当request为空的情况不处理此异常，而是继续执行循环，直到request获取到消息内容；
         *
         */
        while (true) {
            try {
                request = httpRequest.body();

            } catch (Exception e) {
                request = "";

            }
            if (request != null || request.length() != 0) {
                break;

            }
        }
        httpRequest.disconnect();
        if (StringKit.isBlank(request)) {
            return array;

        }
        String requestCode = Matchers.match("retcode:\"(\\d+)\",", request);
        String selector = Matchers.match("selector:\"(\\d+)\"}", request);
        if (null != requestCode && null != selector) {
            array[0] = Integer.parseInt(requestCode);
            array[1] = Integer.parseInt(selector);
            return array;

        }
        return array;

    }

    /**
     * 给好友发送信息
     *
     * @param content(消息内容)
     * @param toPartner(指定好友名称)
     * @author zhangyibin
     */
    public void sendMessage(String content, String toPartner) {
        String url = this.BASE_URL + "/webwxsendmsg?lang=zh_CN&pass_ticket=" + this.PASS_TICKET;
        JSONObject jsonObject_body = new JSONObject();
        String clientMsgId = DateKit.getCurrentUnixTime() + StringKit.getRandomNumber(5);
        JSONObject msg = new JSONObject();
        msg.put("Type", 1);
        msg.put("Content", content);
        msg.put("FromUserName", this.user.getString("UserName"));
        msg.put("ToUserName", toPartner);
        msg.put("LocalID", clientMsgId);
        msg.put("ClientMsgId", clientMsgId);
        jsonObject_body.put("BaseRequest", this.baseRequest);
        jsonObject_body.put("Msg", msg);
        HttpRequest httpRequest = HttpRequest.post(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Cookie", this.cookie)
                .send(jsonObject_body.toString());
        //System.out.println("[张益斌提示]-" + httpRequest);
        httpRequest.body();
        httpRequest.disconnect();

    }

    /**
     * 获取最新消息内容
     *
     * @return 消息json
     * @author zhangyibin
     */
    public JSONObject getNewNews() {
        String url = this.BASE_URL
                + "/webwxsync?lang=zh_CN&pass_ticket="
                + this.PASS_TICKET
                + "&skey="
                + this.SKEY
                + "&sid="
                + this.WXSID
                + "&r="
                + DateKit.getCurrentUnixTime();
        JSONObject jsonObject_body = new JSONObject();
        jsonObject_body.put("BaseRequest", this.baseRequest);
        jsonObject_body.put("SyncKey", this.syncKey);
        jsonObject_body.put("rr", DateKit.getCurrentUnixTime());
        HttpRequest httpRequest = HttpRequest
                .post(url)
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Cookie", this.cookie)
                .send(jsonObject_body.toString());
        //System.out.println("[张益斌提示]-" + httpRequest);
        String request = httpRequest.body();
        httpRequest.disconnect();
        if (StringKit.isBlank(request)) {
            return null;

        }
        JSONObject jsonObject = (JSONObject) JSON.parse(request);
        JSONObject baseResponse = (JSONObject) jsonObject.get("BaseResponse");
        if (null != baseResponse) {
            int ret = baseResponse.getInt("Ret", -1);
            if (ret == 0) {
                this.syncKey = (JSONObject) jsonObject.get("SyncKey");
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

            }
        }
        return jsonObject;

    }

    /**
     * 给微信好友回复消息
     *
     * @param data(收到的消息内容)
     * @param message(消息内容)
     * @param blackList(黑名单用户)
     * @author zhangyibin
     */
    public void replyMessage(JSONObject data, String message, String... blackList) {
        String partnerName = "";//好友名称
        if (null == data) {
            return;

        }
        JSONArray addMsgList = (JSONArray) data.get("AddMsgList");
        for (int i = 0, len = addMsgList.size(); i < len; i++) {
            System.out.println("[张益斌提示]-你有新的消息，请注意查收");
            JSONObject msg = (JSONObject) addMsgList.get(i);
            //System.out.println(msg);//消息内容json
            int msgType = msg.getInt("MsgType", 0);
            partnerName = this.getUserRemarkName(msg.getString("FromUserName"));// 获取好友的备注名称
            String receiveMessages = msg.getString("Content");// receiveMessages 接收好友消息
            if (msgType == 51) {
                System.out.println("[张益斌提示]-成功截获微信消息!");

            }
            /*
             * 黑名单判断逻辑如下：
             * 1.如果好友备注名称在黑名单列表中，那么系统自动回复预先设置好的消息内容"。
             * 2.如果好友备注名称不在黑名单列表中，那么"不回复该好友消息!"(设置黑名单是对白名单好友表示尊重)。
             * 3.黑名单的消息内容答复机制：
             *   a.判断message为空：那么消息的答复内容就调用图灵机器人的消息。
             *   b.判断message非空：那么就以默认设置的消息进行回复。
             *
             */
            for (String blackListName : blackList) {
                if (partnerName.equals(blackListName)) {
                    if (msgType == 1) {
                        System.out.println("来自好友" + partnerName + "的消息内容: " + receiveMessages);
                        String replyMessage = "";// ReplyMessage 回复好友消息
                        if (message.equals("")) {
                            replyMessage = Turing.getTuring(receiveMessages).getTuringReplyMessage();

                        } else {
                            replyMessage = message;

                        }
                        this.sendMessage(replyMessage, msg.getString("FromUserName")); // 回复好友消息
                        System.out.println("回复好友" + partnerName + "的消息内容: " + replyMessage);

                    } else if (msgType == 3) {
                        this.sendMessage("我识别不了图片消息", msg.getString("FromUserName"));
                        System.out.println("回复好友" + partnerName + "的消息内容: " + "我识别不了图片消息");

                    } else if (msgType == 34) {
                        this.sendMessage("我识别不了语音消息", msg.getString("FromUserName"));
                        System.out.println("回复好友" + partnerName + "的消息内容: " + "我识别不了语音消息");

                    } else if (msgType == 42) {
                        System.out.println("回复好友" + partnerName + "的消息内容: " + " 给你发了一张名片!");

                    }
                }
            }
        }
    }

    /**
     * 获得好友备注名称
     *
     * @param id (用户id)
     * @return name(好友备注名称)
     * @author zhangyibin
     */
    public String getUserRemarkName(String id) {
        String name = "未知好友";
        for (int i = 0, len = this.memberList.size(); i < len; i++) {
            JSONObject jsonObject_member = (JSONObject) this.memberList.get(i);
            if (jsonObject_member.getString("UserName").equals(id)) {
                if (StringKit.isNotBlank(jsonObject_member.getString("RemarkName"))) {
                    name = jsonObject_member.getString("RemarkName"); //微信好友备注名称

                } else {
                    name = jsonObject_member.getString("NickName"); //微信好友昵称

                }
                return name;

            }
        }
        return name;

    }

    /**
     * 监听微信消息
     *
     * @param message(消息内容)
     * @param partnerBlackList(好友黑名单列表)
     * @author zhangyibin
     */
    public void listenMsgMode(String message, String... partnerBlackList) {
        try {
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            // 线程延时1000毫秒
                            try {
                                Thread.sleep(1000);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            System.out.println("[张益斌提示]-进入消息监听模式 ...");
                            while (true) {

                                while (true) {
                                    try {
                                        /*
                                         * 回复消息程序跳转到标签处，间隔3秒监听一次消息
                                         */
                                        Thread.sleep(3000);
                                        int[] arr = messageSyncCheck();
                                        System.out.println("[张益斌提示]-监听模式：" + arr[0] + "," + arr[1]);
                                        if (arr[0] == 1101 && arr[1] == 0) {
                                            System.out.println("[张益斌提示]-你在手机上登出了微信,再见!");
                                            break;

                                        }
                                        if (arr[0] == 0) {
                                            if (arr[1] == 2) {
                                                System.out.println("[张益斌提示]-收到文本消息类型；");
                                                JSONObject data = getNewNews();
                                                replyMessage(data, message, partnerBlackList);
                                                //break label;
                                                Thread.sleep(3000);

                                            }
                                            if (arr[1] == 6) {
                                                System.out.println("[张益斌提示]-收到未知消息类型；");
                                                //break label;
                                                Thread.sleep(3000);

                                            }
                                            if (arr[1] == 7) {
                                                System.out.println("[张益斌提示]-收到未知消息类型；");
                                                //break label;
                                                Thread.sleep(3000);

                                            }
                                            if (arr[1] == 3) {
                                                System.out.println("[张益斌提示]-收到未知消息类型；");
                                                //break label;
                                                Thread.sleep(3000);

                                            }
                                            if (arr[1] == 0) {
                                                System.out.println("[张益斌提示]-收到未知消息类型；");
                                                //break label;
                                                Thread.sleep(3000);

                                            }
                                        } else {
                                            System.out.println("[张益斌提示]-收到未知消息类型；");
                                            //break label;
                                            Thread.sleep(3000);

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();

                                    }

                                }
                            }
                        }
                    }, "listenMsgMode").start();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
