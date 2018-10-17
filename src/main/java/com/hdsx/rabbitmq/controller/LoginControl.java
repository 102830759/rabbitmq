package com.hdsx.rabbitmq.controller;

import com.hdsx.rabbitmq.service.JsmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huyue@hdsxtech.com
 * @date 2018/4/20 15:30
 */

@RestController
@RequestMapping("login")
@Api(value = "登陆")
public class LoginControl {

//    @Autowired
    private JsmsService jsmsService;

    public void login(String username, String password) {

    }

    /**
     * 获得验证码
     * @param phone
     * @return
     */
    @RequestMapping(value = "getVal" ,method = RequestMethod.GET)
    @ApiOperation(value = "获得验证码")
    public String getVal(@RequestParam("phone") String phone) {
        String s = jsmsService.sendSMSCode(phone);
        System.out.println("信息：" + s);
        return s;
    }

    /**
     * 校验短信验证码
     * @param msgId
     * @param code
     * @return
     */
    @RequestMapping("validSMSCode")
    @ApiOperation(value = "检验短信验证码")
    public Boolean validSMSCode(@RequestParam String msgId,@RequestParam String code) {
        Boolean aBoolean = jsmsService.sendValidSMSCode(msgId, code);
        return aBoolean;
    }

    /**
     * 发送短信
     * @param phone
     * @param content
     * @return
     */
    @RequestMapping(value = "setMSG",method = RequestMethod.GET)
    @ApiOperation(value = "发送短信")
    public Boolean setMSG(@RequestParam("phone") String phone,
                          @RequestParam("content") String content) {
        boolean f= jsmsService.sendScheduleSMS(content,phone);
        return f;
    }
}
