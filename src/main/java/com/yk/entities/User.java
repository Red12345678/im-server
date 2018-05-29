package com.yk.entities;

import java.io.Serializable;

/**
 * [com.yk.entities desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/5/28
 */
public class User implements Serializable{

    private Integer id;
    private String  username;
    private String  face;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

}
