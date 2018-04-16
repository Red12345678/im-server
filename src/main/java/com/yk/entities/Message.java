package com.yk.entities;

/**
 * [com.yk.entities desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/4/16
 */
public class Message {
    private String fromUser;
    private String fromToken;
    private String toUser;
    private String toToken;
    private String message;
    private String type;
    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getFromToken() {
        return fromToken;
    }

    public void setFromToken(String fromToken) {
        this.fromToken = fromToken;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getToToken() {
        return toToken;
    }

    public void setToToken(String toToken) {
        this.toToken = toToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
