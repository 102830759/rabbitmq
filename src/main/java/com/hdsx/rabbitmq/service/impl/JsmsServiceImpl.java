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
import com.hdsx.rabbitmq.vo.Info;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author huyue@hdsxtech.com
 * @date 2018/4/20 15:44
 */

//@Service
public class JsmsServiceImpl implements JsmsService {

    @Value("${APP_KEY}")
    private String APP_KEY;

    @Value("${MASTER_SECRET}")
    private String MASTER_SECRET;

    @Value("${temp_id}")
    private Integer temp_id;

    // TODO 需要相关配置 masterSecret & appkey
    private SMSClient client = new SMSClient(MASTER_SECRET, APP_KEY);

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    public Boolean sendScheduleSMS(String content, String phone) {
        ScheduleSMSPayload payload = ScheduleSMSPayload.newBuilder()
                .setMobileNumber(phone)
                .setTempId(temp_id)
                .setSendTime(format.format(new Date().getTime() + (1000 * 60 * 1)))
                .addTempPara("number", content)
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

    @Override
    public Boolean SendBatchScheduleSMS(List<Info> infoList) {
        boolean resultOK = false;
        List<RecipientPayload> list = new ArrayList<RecipientPayload>();
        for (Info info : infoList) {
            RecipientPayload recipientPayload = new RecipientPayload.Builder()
                    .setMobile(info.getAddressee())
                    .addTempPara("number", info.getMsg())
                    .build();
            list.add(recipientPayload);
        }
        RecipientPayload[] recipientPayloads = new RecipientPayload[list.size()];
        ScheduleSMSPayload smsPayload = ScheduleSMSPayload.newBuilder()
                .setSendTime(format.format(new Date().getTime() + (1000 * 60 * 1)))
                .setTempId(temp_id)
                .setRecipients(list.toArray(recipientPayloads))
                .build();
        try {
            BatchSMSResult result = client.sendBatchScheduleSMS(smsPayload);
            resultOK = result.isResultOK();
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
        return resultOK;
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