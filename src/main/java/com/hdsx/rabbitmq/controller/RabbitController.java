package com.hdsx.rabbitmq.controller;

import com.hdsx.rabbitmq.producer.HelloSender;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试Controller
 */

@RestController
@RequestMapping("rabbit")
@Api(value = "测试Controller")
public class RabbitController {

    @Autowired
    private HelloSender helloSender1;


    @GetMapping("/hello")
    @ApiOperation(value = "hello消费者")
    public void hello() {
        helloSender1.send("hello1");
    }

    /**
     * 单生产者-多消费者
     */
    @GetMapping("/oneToMany")
    @ApiOperation(value = "单生产者对多消费者")
    public void oneToMany() {
        for (int i = 0; i < 10; i++) {
            helloSender1.send("hellomsg:" + i);
        }
    }
}
