package com.hdsx.rabbitmq.consumer;


import com.hdsx.rabbitmq.util.GsonUtil;
import com.hdsx.rabbitmq.vo.Info;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * 消费者-邮箱
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
        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            Info info = (Info)GsonUtil.jsonToObject(message, Info.class);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(info.getAddressee());
            mimeMessageHelper.setSubject("测试邮件主题");
            mimeMessageHelper.setText(info.getMsg());
            this.mailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
