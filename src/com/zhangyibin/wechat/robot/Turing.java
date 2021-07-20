package com.zhangyibin.wechat.robot;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.json.JSONObject;

import java.io.IOException;

/*
 * 图灵机器人
 */

public class Turing {

    private String text = "";

    private Turing(String text) {
        this.text = text;

    }

    /**
     * 实例化图灵对象
     *
     * @param text(接收的好友消息)
     * @return 图灵对象
     * @author zhangyibin
     */
    public static Turing getTuring(String text) {
        return new Turing(text);

    }

    /**
     * 返回json解析内容
     *
     * @param json
     * @return text(文本消息)
     * @author zhangyibin
     */
    private String setJSONParsing(String json) {
        String text = "";
        JSONObject jsonObject = new JSONObject(json);
        JSONArray results = jsonObject.getJSONArray("results");
        for (int i = 0; i < results.length(); i++) {
            JSONObject values = results.getJSONObject(i).getJSONObject("values");
            text = values.get("text").toString();

        }
        return text;

    }

    /**
     * 对接图灵聊天机器人
     *
     * @return message(图灵回复消息)
     * @author zhangyibin
     */
    private String getText() {
        String message = "";
        String api = "http://openapi.tuling123.com/openapi/api/v2";
        Connection connection = Jsoup.connect(api);
        connection.ignoreContentType(true);
        connection.timeout(10000);
        connection.userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) " +
                "Chrome/91.0.4472.114 Mobile Safari/537.36");
        connection.requestBody("{\n" +
                "\t\"reqType\":0,\n" +
                "    \"perception\": {\n" +
                "        \"inputText\": {\n" +
                "            \"text\": \"+" + this.text + "+\"\n" +
                "        },\n" +
                "        \"inputImage\": {\n" +
                "            \"url\": \"imageUrl\"\n" +
                "        },\n" +
                "        \"selfInfo\": {\n" +
                "            \"location\": {\n" +
                "                \"city\": \"浙江\",\n" +
                "                \"province\": \"杭州\",\n" +
                "                \"street\": \"\"\n" +
                "            }\n" +
                "        }\n" +
                "    },\n" +
                "    \"userInfo\": {\n" +
                "        \"apiKey\": \"b7f5a870a15b4958852166350741b6b7\",\n" +
                "        \"userId\": \"281598\",\n" +
                "    }\n" +
                "}");
        Document document = null;
        try {
            document = connection.post();
            message = this.setJSONParsing(document.text());

        } catch (IOException e) {
            message = "你好！我正在休息，稍后回复你消息。";

        }
        return message;

    }

    /**
     * 返回图灵回复消息
     *
     * @return getText()
     * @author zhangyibin
     */
    public String getTuringReplyMessage() {
        return this.getText();

    }
}
