package com.hdsx.rabbitmq.consumer;


import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者
 */
@Component
@RabbitListener(queues = "helloQueue")
public class HelloReceiver1 {


    @RabbitHandler
    public void process(String hello) {
        try {
            Thread thread = Thread.currentThread();
            long id = thread.getId();
            System.out.println("消费者 1 : " + hello + "  ,线程ID:" + id);
//            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
