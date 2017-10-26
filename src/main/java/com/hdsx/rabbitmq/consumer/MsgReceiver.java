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

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // TODO 需要相关配置 masterSecret & appkey
    private SMSClient client = new SMSClient("", "");

    @RabbitHandler
    public boolean process(String message) {
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber("1760020095")
                .setTempId(1)
                .build();
        try {
            SendSMSResult res = client.sendSMSCode(payload);
            return res.isResultOK();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        return false;
    }
}
