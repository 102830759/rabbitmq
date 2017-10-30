package com.hdsx.rabbitmq.consumer;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 消费者
 */
@Component
@RabbitListener(queues = "helloQueue")
public class HelloReceiver2 {


    @RabbitHandler
    public void process(String message, Channel channel) {
        try {
//            channel.basicQos(1);
//            int s = Integer.parseInt("s");
            Thread thread = Thread.currentThread();
            long id = thread.getId();
            System.out.println("消费者 2 : " + message + "  ,线程ID:" + id);
            Thread.sleep(10000);
        } catch (Exception e) {
            try {
                channel.basicAck(0, false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
