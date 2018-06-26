package com.hdsx.rabbitmq.controller;


import com.hdsx.rabbitmq.service.QueueService;
import com.hdsx.rabbitmq.util.GsonUtil;
import com.hdsx.rabbitmq.vo.Info;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息控制中心
 */

@RestController
@Api(description = "测试")
@RequestMapping("test")
public class TestController {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Autowired
    private QueueService queueService;

    @RequestMapping(value = "sendMsg", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "集成测试")
    public Boolean sendEmail1(@RequestParam(value = "types") List<String> types,
                              @RequestParam(value = "msg") String msg,
                              @RequestParam(value = "sendee") String sendee) {
        if (types.isEmpty()) return false;
        for (int i = 0; i < types.size(); i++) {
            if ("websocket".equals(types.get(i)) || "queue".equals(types.get(i))) { // websocket和queue都是发送到对应的队列上的
                rabbitTemplate.convertAndSend(sendee, msg);
            } else {
                Info info = new Info();
                info.setAddressee(sendee);
                info.setMsg(msg);
                rabbitTemplate.convertAndSend(types.get(i),GsonUtil.objectToJson(info));
            }
        }
        return true;
    }

    /**
     * 创建队列，绑定转换，路由规则    topoc.exchangeName.*
     * 适用于一对多信息发布
     *
     * @param queueName    队列名（习惯是用户名）
     * @param exchangeName 单位id
     */
    @RequestMapping(value = "createQueue", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "创建一个队列，并设置其转换名exchange，以及转换规则routingkey")
    public void createQueue(@RequestParam(value = "queueName") String queueName,
                            @RequestParam(value = "exchangeName") String exchangeName) {
        String queue1 = queueService.declareQueue(queueName);
        queueService.declareExchange(exchangeName);
        queueService.declareBinding(queue1,exchangeName,"topic." + exchangeName + ".*");
        boolean topicExchange = queueService.declareBinding(queue1, "topicExchange", "topic.*.*");
    }

    /**
     * 给指定一个队列发送信息
     *
     * @param queueName
     */
    @RequestMapping(value = "msg_to_queue", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "给指定一个队列发送信息")
    public Boolean sendMSG_TO_QUEUE(@RequestParam(value = "queueName") String queueName,
                                    @RequestParam(value = "content") String content) {
        Boolean flag = false;
        try {
            rabbitTemplate.convertAndSend(queueName, content);
            flag = true;
        } catch (Exception e) {
            // TODO do SomeThing
        }
        return flag;
    }

    /**
     * 给某一单位发送信息
     * 单位下是多个用户
     *
     * @param dept
     * @param content
     * @return
     */
    @RequestMapping(value = "msg_to_dept", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "给某一单位发送信息")
    public Boolean setMSG_TO_DEPT(@RequestParam(value = "dept") String dept,
                                  @RequestParam(value = "content") String content) {
        Boolean flag = false;
        try {
            rabbitTemplate.convertAndSend(dept, "topic." + dept + ".*", content);
            flag = true;
        } catch (Exception e) {
            // TODO do SomeThing
        }
        return flag;
    }

    /**
     * 给所有用户发送信息
     *
     * @param content
     * @return
     */
    @RequestMapping(value = "msg_to_users", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "给某一单位发送信息")
    public Boolean setMSG_TO_Users(@RequestParam(value = "content") String content) {
        Boolean flag = false;
        try {
            rabbitTemplate.convertAndSend("topicExchange", "topic.*.*", content);
            flag = true;
        } catch (Exception e) {
            // TODO do SomeThing
        }
        return flag;
    }
}