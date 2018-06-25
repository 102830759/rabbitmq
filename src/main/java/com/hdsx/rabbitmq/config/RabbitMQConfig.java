package com.hdsx.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitMQConfig {

    /**
     * 测试用到的队列 helloQueue
     *
     * @return
     */
    @Bean
    public Queue helloQueue() {
        return new Queue("helloQueue");
    }

    /**
     * 邮箱专用队列
     *
     * @return
     */
    @Bean
    public Queue topicMailQueue() {
        return new Queue("mail");
    }

    /**
     * websocket 专用队列
     *
     * @return
     */
    @Bean
    public Queue topicWebQueue() {
        return new Queue("web");
    }

    /**
     * 短信 message队列
     *
     * @return
     */
    @Bean
    public Queue topicMsgQueue() {
        return new Queue("msg", false, false, true);
    }

    @Bean
    public Queue topicJpushQueue() {
        return new Queue("jpush");
    }

    /**
     * Topic是RabbitMQ中最灵活的一种方式，可以根据routing_Key自由的绑定不同的队列。
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }


}