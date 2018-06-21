package com.hdsx.rabbitmq.controller;


import com.google.gson.Gson;
import com.hdsx.rabbitmq.service.JsmsService;
import com.hdsx.rabbitmq.util.GsonUtil;
import com.hdsx.rabbitmq.vo.Info;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息控制中心
 */

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private JsmsService jsmsService;

    @RequestMapping(value = "sendMsg", method = RequestMethod.GET, produces = "application/json")
    public Boolean sendEmail1(@RequestParam(value = "types") List<String> types,
                           @RequestParam(value = "msg") String msg,
                           @RequestParam(value = "sendee") String sendee) {
        if (types.isEmpty()) return false;
        for (int i = 0; i < types.size(); i++) {
            if ("websocket".equals(types.get(i))) {
                rabbitTemplate.convertAndSend("websocket", msg);
            }
            else if("msg".equals(types.get(i))){// 短信
                String s = jsmsService.sendSMSCode(sendee);
                System.out.println("信息：" + s);
            }else if("mail".equals(types.get(i))){// 邮件
                Info info = new Info();
                info.setAddressee(sendee);
                info.setMsg(msg);
                rabbitTemplate.convertAndSend("topicExchange", "topic." + types.get(i), GsonUtil.objectToJson(info));
            }else if("queue".equals(types.get(i))){// 对列
                rabbitTemplate.convertAndSend("topicExchange", "topic." + types.get(i), msg);
            }
        }
        return true;
    }

    @RequestMapping(value = "create", method = RequestMethod.GET, produces = "application/json")
    public void sendEmailsd(@RequestParam(value = "types") List<String> types,
                            @RequestParam(value = "msg") String msg) {

    }

    /**
     * 创建队列，绑定转换，路由规则    topoc.exchangeName.*
     * @param queueName
     * @param exchangeName
     */
    @RequestMapping(value = "createQueue", method = RequestMethod.GET, produces = "application/json")
    public void createQueue(@RequestParam(value = "queueName") String  queueName,
                       @RequestParam(value = "exchangeName") String exchangeName){

        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        String queueName1 = rabbitAdmin.declareQueue(new Queue(queueName));
        rabbitAdmin.declareExchange(new TopicExchange(exchangeName));
//        rabbitAdmin.declareBinding(BindingBuilder.bind(new Queue(queueName)).to(new TopicExchange(exchangeName)).with("topic."+exchangeName+".*"));
        rabbitAdmin.declareBinding(new Binding(queueName1,Binding.DestinationType.QUEUE,exchangeName,"topic."+exchangeName+".*",null));
    }

}