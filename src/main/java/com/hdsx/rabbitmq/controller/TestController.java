package com.hdsx.rabbitmq.controller;


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

    @RequestMapping(value = "sendMsg", method = RequestMethod.GET, produces = "application/json")
    public void sendEmail1(@RequestParam(value = "types") List<String> types,
                           @RequestParam(value = "msg") String msg) {
        if (types.isEmpty()) return;
        for (int i = 0; i < types.size(); i++) {
            if ("websocket".equals(types.get(i))) {
                rabbitTemplate.convertAndSend("websocket", msg);
            }
            rabbitTemplate.convertAndSend("topicExchange", "topic." + types.get(i), msg);
        }
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