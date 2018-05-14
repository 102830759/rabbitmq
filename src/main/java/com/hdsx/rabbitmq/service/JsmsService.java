package com.hdsx.rabbitmq.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;

/**
 * @author huyue@hdsxtech.com
 * @date 2018/4/20 16:13
 */
public interface JsmsService {
    /**
     * 发送验证码
     * @param phone
     * @return
     */
    public String sendSMSCode(String phone);
    /**
     * Send SMS verification code to server, to verify if the code valid 校验验证码
     * @param msgId The message id of the verification code
     * @param code Verification code
     * @return return ValidSMSResult includes is_valid
     * @throws APIConnectionException connection exception
     * @throws APIRequestException request exception
     */
    public Boolean sendValidSMSCode(String msgId,String code);

    public Boolean sendScheduleSMS(String content,String phone,Integer temp_id);

    public Integer createTemplate() ;
}
