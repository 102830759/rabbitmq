package com.hdsx.rabbitmq.consumer;


import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * 消费者-短信
 */

@Component
@RabbitListener(queues = "topic.msg")
public class MsgReceiver {
    protected static final String APP_KEY ="d4ee2375846bc30fa51334f5";
    protected static final String MASTER_SECRET = "cfb11ca45888cdd6388483f5";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // TODO 需要相关配置 masterSecret & appkey
    private SMSClient client = new SMSClient(MASTER_SECRET, APP_KEY);

    @RabbitHandler
    public void process(String message) {
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber("17600200955")
                .setTempId(1)
                .build();
        try {
            SendSMSResult res = client.sendSMSCode(payload);
//            return res.isResultOK();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
//        return true;
    }
}
