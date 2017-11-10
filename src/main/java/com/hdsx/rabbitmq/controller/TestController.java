package com.hdsx.rabbitmq.controller;


import org.springframework.amqp.core.AmqpTemplate;
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

    @RequestMapping(value = "sendMsg", method = RequestMethod.GET, produces = "application/json")
    public void sendEmail1(@RequestParam(value = "types") List<String> types,
                           @RequestParam(value = "msg") String msg) {
        if (types.size() < 0) return;
        for (int i = 0; i < types.size(); i++) {
            if("websocket".equals(types.get(i))){
                rabbitTemplate.convertAndSend("websocket",msg);
            }
            rabbitTemplate.convertAndSend("topicExchange", "topic." + types.get(i), msg);
        }
    }
}