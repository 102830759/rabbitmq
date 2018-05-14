package com.hdsx.rabbitmq.service.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.ValidSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.BatchSMSResult;
import cn.jsms.api.common.model.RecipientPayload;
import cn.jsms.api.common.model.SMSPayload;
import cn.jsms.api.schedule.model.ScheduleResult;
import cn.jsms.api.schedule.model.ScheduleSMSPayload;
import cn.jsms.api.template.SendTempSMSResult;
import cn.jsms.api.template.TemplatePayload;
import com.hdsx.rabbitmq.service.JsmsService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author huyue@hdsxtech.com
 * @date 2018/4/20 15:44
 */

@Service
public class JsmsServiceImpl implements JsmsService {

    protected static final String APP_KEY = "f19c5462045ae94eefb44563";
    protected static final String MASTER_SECRET = "6de11ca30113c0cda42074c2";
    //未知
    protected static final String DEV_KEY = "8885227f9e44358a59aa0880";
    protected static final String DEV_SECRET = "281399a95fa3be40645f0558";
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // TODO 需要相关配置 masterSecret & appkey
    private SMSClient client = new SMSClient(MASTER_SECRET, APP_KEY);

    public String sendSMSCode(String phone) {
        SMSPayload payload
                = SMSPayload.newBuilder()
                .setMobileNumber(phone)
                .setTempId(1)
                .setTTL(60)
                .build();
        String resule = null;
        try {
            SendSMSResult res = client.sendSMSCode(payload);
            resule = res.toString();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        return resule;
    }

    @Override
    public Boolean sendValidSMSCode(String msgId, String code) {
        Boolean isValid = false;
        try {
            ValidSMSResult res = client.sendValidSMSCode(msgId, code);
            isValid = res.getIsValid();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        return isValid;
    }

    @Override
    public Boolean sendScheduleSMS(String content, String phone, Integer temp_id) {
        ScheduleSMSPayload payload = ScheduleSMSPayload.newBuilder()
                .setMobileNumber(phone)
                .setTempId(temp_id)
                .setSendTime(format.format(new Date().getTime() + (1000 * 60 * 3)))
                .addTempPara("code", content)
                .build();
        Boolean resultOK = false;
        try {
            ScheduleResult result = client.sendScheduleSMS(payload);
            resultOK = result.isResultOK();
        } catch (APIConnectionException e) {
        } catch (APIRequestException e) {
        }
        return resultOK;
    }

    public void testSendBatchScheduleSMS() {
        List<RecipientPayload> list = new ArrayList<RecipientPayload>();
        RecipientPayload recipientPayload1 = new RecipientPayload.Builder()
                .setMobile("13800138000")
                .addTempPara("code", "638938")
                .build();
        RecipientPayload recipientPayload2 = new RecipientPayload.Builder()
                .setMobile("13800138000")
                .addTempPara("code", "829302")
                .build();
        list.add(recipientPayload1);
        list.add(recipientPayload2);
        RecipientPayload[] recipientPayloads = new RecipientPayload[list.size()];
        ScheduleSMSPayload smsPayload = ScheduleSMSPayload.newBuilder()
                .setSendTime("2017-08-02 14:20:00")
                .setTempId(1)
                .setRecipients(list.toArray(recipientPayloads))
                .build();
        try {
            BatchSMSResult result = client.sendBatchScheduleSMS(smsPayload);
        } catch (APIConnectionException e) {
        } catch (APIRequestException e) {
        }
    }

    public Integer createTemplate() {
        Integer temp_id = 1;
        try {
            client = new SMSClient(MASTER_SECRET, APP_KEY);
            TemplatePayload payload = TemplatePayload.newBuilder()
                    .setTemplate("你收到了来自省路警的信息{{code}},请及时回复")
                    .setType(1)
                    .setTTL(120)
                    .setRemark("省监控中心")
                    .build();
            SendTempSMSResult result = client.createTemplate(payload);
            temp_id = result.getTempId();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        return temp_id;
    }
}