package com.hdsx.rabbitmq.controller;



import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试发送邮箱
 */

@RestController
@RequestMapping("mail")
public class MailController {

    @Autowired
    private AmqpTemplate rabbitTemplate;


    @RequestMapping("sendEmail")
    public void sendEmail1(){
        rabbitTemplate.convertAndSend("topicExchange", "topic.mail", "测试邮件内容，哈哈哈哈哈！！！");
    }
}