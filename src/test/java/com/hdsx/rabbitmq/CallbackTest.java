package com.hdsx.rabbitmq;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
/**
 * @author huyue@hdsxtech.com
 * @date 2018/6/27 15:22
 */
public class CallbackTest {
    private final static String URL = "http://localhost:8088/callback"; // 开发者将自己的回调URL写入
    private final static String APPKEY = "f19c5462045ae94eefb44563";       // 开发者将自己的appKey写入
    private final static String APPMASTERSECRET = "6de11ca30113c0cda42074c2"; // 开发者将自己的appMasterSecret写入
    public static void main(String[] args) throws IOException {
        long nonce = new Random().nextLong();
        long timestamp = System.currentTimeMillis();
        String str = String.format("appKey=%s&appMasterSecret=%s&nonce=%s&timestamp=%s",
                APPKEY, APPMASTERSECRET, nonce, timestamp);
        String signature = encrypt(str);
        Map<String, Object> params = new HashMap<>();
        params.put("nonce", nonce);
        params.put("timestamp", timestamp);
        params.put("signature", signature);

        // 测试用户回复消息回调
        ReplyMessage replyMessage = new ReplyMessage();
        replyMessage.setPhone("13233853721");
        replyMessage.setReplyTime(new Date());
        replyMessage.setContent("TD");
        params.put("data", JSON.toJSONString(replyMessage));
        params.put("type", "SMS_REPLY");
//        System.out.println("post params: " + JSON.toJSONString(params));
        sendPost(URL, params);
/*
        // 测试短信送达状态回调
        ReportMessage reportMessage = new ReportMessage();
        reportMessage.setMsgId("1652496");
        reportMessage.setStatus(4001);
        reportMessage.setPhone("15822889320");
        reportMessage.setReceiveTime(new Date());
        params.put("data", JSON.toJSONString(reportMessage));
        params.put("type", "SMS_REPORT");
        System.out.println("post params: " + JSON.toJSONString(params));
        sendPost(URL, params);

        // 测试短信模板审核结果回调
        TemplateMessage templateMessage = new TemplateMessage();
        templateMessage.setTempId(57496);
        templateMessage.setStatus(1);
        templateMessage.setRefuseReason(null);
        params.put("data", JSON.toJSONString(templateMessage));
        params.put("type", "SMS_TEMPLATE");
        System.out.println("post params: " + JSON.toJSONString(params));
        sendPost(URL, params);*/
    }

    private static void sendPost(String url, Map<String, Object> params) throws IOException {
        HttpClient httpClient = HttpClients.custom().build();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> formParams = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams, "UTF-8");
        httpPost.setEntity(uefEntity);
        HttpResponse response = httpClient.execute(httpPost);
        System.out.println("response: " + JSON.toJSONString(response));
    }

    private static class ReplyMessage {
        private String phone;
        private Date replyTime;
        private String content;
        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
        public Date getReplyTime() {
            return replyTime;
        }
        public void setReplyTime(Date replayTime) {
            this.replyTime = replayTime;
        }
    }

    private static class ReportMessage {
        private String msgId;
        private Integer status;
        private String phone;
        private Date receiveTime;
        public String getMsgId() {
            return msgId;
        }
        public void setMsgId(String msgId) {
            this.msgId = msgId;
        }
        public Integer getStatus() {
            return status;
        }
        public void setStatus(Integer status) {
            this.status = status;
        }
        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
        public Date getReceiveTime() {
            return receiveTime;
        }
        public void setReceiveTime(Date receiveTime) {
            this.receiveTime = receiveTime;
        }
    }

    private static class TemplateMessage {
        private Integer tempId;
        private Integer status;
        private String refuseReason;
        public Integer getTempId() {
            return tempId;
        }
        public void setTempId(Integer tempId) {
            this.tempId = tempId;
        }
        public Integer getStatus() {
            return status;
        }
        public void setStatus(Integer status) {
            this.status = status;
        }
        public String getRefuseReason() {
            return refuseReason;
        }
        public void setRefuseReason(String refuseReason) {
            this.refuseReason = refuseReason;
        }
    }

    /**
     * SHA1加密
     *
     * @param strSrc 明文
     * @return 加密之后的密文
     */
    public static String encrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-1");// 将此换成SHA-1、SHA-512、SHA-384等参数
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * byte数组转换为16进制字符串
     *
     * @param bts 数据源
     * @return 16进制字符串
     */
    private static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }
}
