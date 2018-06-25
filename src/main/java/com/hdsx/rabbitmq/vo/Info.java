package com.hdsx.rabbitmq.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Info implements Serializable{

    private static final long serialVersionUID = 3204420177000310894L;

    // 消息内容
    private String msg;
    // 信息类型
    private List<String> types;
    // 收件人
    private String addressee;
    // 发件时间
    private Date date;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
