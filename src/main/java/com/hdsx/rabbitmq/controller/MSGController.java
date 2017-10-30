package com.hdsx.rabbitmq.controller;



import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试发送邮箱
 */

@RestController
@RequestMapping("msg")
public class MSGController {

    @Autowired
    private AmqpTemplate rabbitTemplate;


    @RequestMapping("sendMsg")
    public void sendEmail1(){
        rabbitTemplate.convertAndSend("topicExchange", "topic.msg", "你好！");
    }
}