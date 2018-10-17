package com.hdsx.rabbitmq.consumer;


import com.hdsx.rabbitmq.service.JsmsService;
import com.hdsx.rabbitmq.util.GsonUtil;
import com.hdsx.rabbitmq.vo.Info;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者-短信
 * 自定义短信内容发送
 */

@Component
@RabbitListener(queues = "msg")
public class MsgReceiver {

//    @Autowired
    private JsmsService jsmsService;

    @RabbitHandler
    public void process(String message) {
        Info info = (Info) GsonUtil.jsonToObject(message, Info.class);
        jsmsService.sendScheduleSMS(info.getMsg(), info.getAddressee());
    }
}
