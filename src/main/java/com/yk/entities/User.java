package com.yk.entities;

/**
 * [com.yk.entities desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/5/28
 */
public class User {

    private Integer id;
    private String username;
    private String face;

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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", face='" + face + '\'' +
                '}';
    }
}
