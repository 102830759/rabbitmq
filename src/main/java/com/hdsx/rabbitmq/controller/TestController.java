package com.hdsx.rabbitmq.controller;



import com.hdsx.rabbitmq.vo.Info;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消息控制中心
 */

@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private AmqpTemplate rabbitTemplate;


    @RequestMapping("sendMsg")
    public void sendEmail1(@RequestBody Info info){
//        rabbitTemplate.convertAndSend("topicExchange", "topic.mail", "测试邮件内容，哈哈哈哈哈！！！");


    }
}