package com.hdsx.rabbitmq.service.impl;

import com.hdsx.rabbitmq.service.QueueService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author huyue@hdsxtech.com
 * @date 2018/6/26 9:18
 */

@Service
//@RPC
public class QueueServiceImpl implements QueueService {


    @Autowired
    private ConnectionFactory connectionFactory;

    private RabbitAdmin rabbitAdmin = null;

    RabbitAdmin getRabbitAdmin() {
        if (this.rabbitAdmin != null) {
            return this.rabbitAdmin;
        } else {
            this.rabbitAdmin = new RabbitAdmin(connectionFactory);
            return this.rabbitAdmin;
        }
    }

    @Override
    public String declareQueue(String queue) {
        return getRabbitAdmin().declareQueue(new Queue(queue));
    }

    @Override
    public boolean deleteQueue(String queueName) {
        return getRabbitAdmin().deleteQueue(queueName);
    }

    @Override
    public void declareExchange(String exchangeName) {
        getRabbitAdmin().declareExchange(new TopicExchange(exchangeName));
    }

    @Override
    public boolean deleteExchange(String exchangeName) {
        return getRabbitAdmin().deleteExchange(exchangeName);
    }

    @Override
    public boolean declareBinding(String queueName, String exchangeName, String routingKey) {
        boolean flag = false;
        try {
            getRabbitAdmin().declareBinding(new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null));
            flag = true;
        } catch (Exception e) {
            // TODO do someThing
        }
        return flag;
    }
}
