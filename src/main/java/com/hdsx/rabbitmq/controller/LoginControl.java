package com.hdsx.rabbitmq.controller;

import com.hdsx.rabbitmq.service.JsmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huyue@hdsxtech.com
 * @date 2018/4/20 15:30
 */

@RestController
@RequestMapping("login")
public class LoginControl {

    @Autowired
    private JsmsService jsmsService;


    public void login(String username, String password) {

    }

    @RequestMapping("getVal")
    public String getVal() {
        String phone = "13233853721";
        String s = jsmsService.sendSMSCode(phone);
        System.out.println("信息：" + s);
        return s;
    }

    @RequestMapping("setMSG")
    public Boolean setMSG() {
        String phone = "13233853721";
        Integer template = jsmsService.createTemplate();
        boolean f= jsmsService.sendScheduleSMS("今天星期五",phone,template);
        return f;
    }
}
