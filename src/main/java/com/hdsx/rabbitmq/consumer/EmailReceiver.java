package com.hdsx.rabbitmq.consumer;


import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * 接收并处理消息
 */

@Component
@RabbitListener(queues = "topic.mail")
public class EmailReceiver {

    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender; //读取配置文件中的参数

    @RabbitHandler
    public void process(String message) {
        System.out.println(String.format("receive topic a message: %s", message));
        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo("18701324@qq.com");
            mimeMessageHelper.setSubject("测试邮件主题");
            mimeMessageHelper.setText(message);
            this.mailSender.send(mimeMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
