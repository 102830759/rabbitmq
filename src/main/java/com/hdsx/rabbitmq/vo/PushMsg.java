package com.hdsx.rabbitmq.vo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * Jpush  推送的消息体
 */
public class PushMsg implements Serializable {

    private static final long serialVersionUID = 1L;


    //发送平台类型
    private String type;

    //标题
    private String title;
    //消息内容
    private String alert;
    //通知内容
    private String msg_content;
    //消息类型
    private String contentType;
    //角标
    private Integer badge;
    //声音
    private String sound;
    //registrationId
    private Collection<String> registrationIds = null;
    //alias
    private Collection<String> alias = null;
    //tag
    private String tag;
    //消息体
    private Map<String, String> extras = null;

    public PushMsg() {

    }

    public PushMsg(String type, String title, String contentType, String msg_content, Collection<String> alias, Map<String, String> extras) {
        this.type = type;
        this.title = title;
        this.contentType = contentType;
        this.msg_content = msg_content;
        this.alias = alias;
        this.extras = extras;
    }


    public Map<String, String> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, String> extras) {
        this.extras = extras;
    }

    public Collection<String> getAlias() {
        return alias;
    }

    public void setAlias(Collection<String> alias) {
        this.alias = alias;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Integer getBadge() {
        return badge;
    }

    public void setBadge(Integer badge) {
        this.badge = badge;
    }

    public String getMsg_content() {
        return msg_content;
    }

    public void setMsg_content(String msg_content) {
        this.msg_content = msg_content;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<String> getRegistrationIds() {
        return registrationIds;
    }

    public void setRegistrationIds(Collection<String> registrationIds) {
        this.registrationIds = registrationIds;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
