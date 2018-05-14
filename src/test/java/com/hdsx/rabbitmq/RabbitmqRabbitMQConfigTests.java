package com.hdsx.rabbitmq;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import com.google.gson.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqRabbitMQConfigTests {

	@Test
	public void contextLoads() {
	}

	private SMSClient client = null;
	@Test
	public void testSendVoiceSMSCode() {
		SMSPayload payload = SMSPayload.newBuilder()
				.setMobileNumber("")
				.setVoiceLang(2)
				.setCode("039482")
				.build();

		JsonObject json = new JsonObject();
		json.addProperty("mobile", "13800138000");
		json.addProperty("voice_lang", 2);
		json.addProperty("code", "039482");
		assertEquals(payload.toJSON(), json);

		try {
			SendSMSResult res = client.sendVoiceSMSCode(payload);
			System.out.println(res.toString());
		} catch (APIRequestException e) {
			System.out.println("Error response from JPush server. Should review and fix it. "+ e);
			System.out.println("HTTP Status: " + e.getStatus());
			System.out.println("Error Message: " + e.getMessage());
		} catch (APIConnectionException e) {
			System.out.println("Connection error. Should retry later. "+ e);
		}
	}
	
}
