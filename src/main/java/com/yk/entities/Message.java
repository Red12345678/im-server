package com.yk.entities;

import com.yk.utils.MessageStatus;
import com.yk.utils.TimeUtil;

/**
 * [com.yk.entities desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/4/16
 */
public class Message {
    private String  user_name;
    private Integer uid;
    private Integer touid;
    private String  touname;
    private String  room_id;
    private String  type;
    private String  content;
    private String inputtime = TimeUtil.getCurrentDateString();
    private String stype;
    private String role = "s";
    private String head_img;
    private MessageStatus status = MessageStatus.SUCCESS;////[消息发送状态 SUCCESS(成功) | FAIL(失败)

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getTouid() {
        return touid;
    }

    public void setTouid(Integer touid) {
        this.touid = touid;
    }

    public String getTouname() {
        return touname;
    }

    public void setTouname(String touname) {
        this.touname = touname;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }

    public String getStype() {
        return stype;
    }

    public void setStype(String stype) {
        this.stype = stype;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Message{" +
                "user_name='" + user_name + '\'' +
                ", uid=" + uid +
                ", touid=" + touid +
                ", touname='" + touname + '\'' +
                ", room_id='" + room_id + '\'' +
                ", type='" + type + '\'' +
                ", content='" + content + '\'' +
                ", inputtime='" + inputtime + '\'' +
                ", stype='" + stype + '\'' +
                ", role='" + role + '\'' +
                ", head_img='" + head_img + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
