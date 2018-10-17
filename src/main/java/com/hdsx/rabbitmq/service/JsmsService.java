package com.hdsx.rabbitmq.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import com.hdsx.rabbitmq.vo.Info;

import java.util.List;

/**
 * @author huyue@hdsxtech.com
 * @date 2018/4/20 16:13
 */
public interface JsmsService {
    /**
     * 发送验证码
     *
     * @param phone
     * @return
     */
    public String sendSMSCode(String phone);

    /**
     * Send SMS verification code to server, to verify if the code valid 校验验证码
     *
     * @param msgId The message id of the verification code
     * @param code  Verification code
     * @return return ValidSMSResult includes is_valid
     * @throws APIConnectionException connection exception
     * @throws APIRequestException    request exception
     */
    public Boolean sendValidSMSCode(String msgId, String code);

    /**
     * 发送单条定时短信
     * @param content
     * @param phone
     * @return
     */
    public Boolean sendScheduleSMS(String content, String phone);

    /**
     * 批量发送短信
     * @param infoList
     * @return
     */
    public Boolean SendBatchScheduleSMS(List<Info> infoList);

    /**
     * 创建模板  此方法最好不要用 ，有缺陷
     * 模板创建后 极光后台需要审核的  ，创建的模板不能直接使用
     * @return
     */
    public Integer createTemplate();
}
