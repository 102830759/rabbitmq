package com.hdsx.rabbitmq.service;

/**
 * @author huyue@hdsxtech.com
 * @date 2018/6/25 17:56
 *  队列和转换器的操作
 */
public interface QueueService {

    /**
     * 声明一个队列
     *
     * @param queue
     * @return
     */
    public String declareQueue(String queue);

    /**
     * 删除一个队列
     *
     * @param queueName
     * @return
     */
    public boolean deleteQueue(String queueName);

    /**
     * 声明一个转换
     * TopicExchange 类型
     *
     * @param exchangeName
     */
    public void declareExchange(String exchangeName);

    /**
     * 删除一个转换
     *
     * @param exchangeName
     * @return
     */
    public boolean deleteExchange(String exchangeName);

    /**
     * 声明一个绑定
     *
     * @param queueName    队列名
     * @param exchangeName 转换名
     * @param routingKey   路由规则
     * @return
     */
    public boolean declareBinding(String queueName, String exchangeName, String routingKey);
}
