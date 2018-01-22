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
        return new Queue("topic.mail");
    }

    /**
     * websocket 专用队列
     *
     * @return
     */
    @Bean
    public Queue topicWebQueue() {
        return new Queue("topic.web");
    }

    /**
     * 短信 message队列
     *
     * @return
     */
    @Bean
    public Queue topicMsgQueue() {
//        return new Queue("topic.msg");
        return new Queue("topic.msg", false, false, true);
    }

    // 订阅者模式,广播模式
    @Bean
    public Queue fanoutAQueue() {
        return new Queue("fanout.a");
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanoutExchange");
    }

    @Bean
    public Binding bindingExchangeFanoutA(Queue fanoutAQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutAQueue).to(fanoutExchange);
    }

    @Bean
    public Queue topicJpushQueue() {
        return new Queue("topic.jpush");
    }
    // 订阅者模式 end

    /**
     * Topic是RabbitMQ中最灵活的一种方式，可以根据routing_Key自由的绑定不同的队列。
     *
     * @return
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    public Binding bindingExchangeTopicMail(Queue topicMailQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicMailQueue).to(topicExchange).with("topic.mail");
    }

    @Bean
    public Binding bindingExchangeTopicWeb(Queue topicWebQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicWebQueue).to(topicExchange).with("topic.web.#");
    }

    @Bean
    public Binding bindingExchangeTopicMsg(Queue topicMsgQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(topicMsgQueue).to(topicExchange).with("topic.msg");
    }

}