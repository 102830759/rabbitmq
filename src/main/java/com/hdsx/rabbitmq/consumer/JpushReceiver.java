package com.hdsx.rabbitmq.consumer;


import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

import static cn.jpush.api.push.model.notification.PlatformNotification.ALERT;

/**
 * 消费者-短信
 */

@Component
@RabbitListener(queues = "topic.jpush")
public class JpushReceiver {
    protected static final String APP_KEY = "d4ee2375846bc30fa51334f5";
    protected static final String MASTER_SECRET = "1bdab6d2cb99727cf768cc9c";
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // TODO 这里需要相关一些配置
    JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());

    @RabbitHandler
    public void process(String message) {

        PushPayload payload = buildPushObject_android_tag_alertWithTitle(message);
        try {
            PushResult pushResult = jpushClient.sendPush(payload);
            boolean resultOK = pushResult.isResultOK();// 返回结果
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }

    }

    /**
     * 平台是 Android，目标是 tag 为 "tag1" 的设备，内容是 Android 通知 ALERT，并且标题为 TITLE
     *
     * @param message
     * @return
     */
    public static PushPayload buildPushObject_android_tag_alertWithTitle(String message) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag("tag1"))
                .setNotification(Notification.android(ALERT, "TITLE", null))
                .build();
    }
}
