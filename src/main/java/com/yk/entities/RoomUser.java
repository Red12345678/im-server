package com.yk.entities;

/**
 * [com.yk.entities desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/7/2
 */
public class RoomUser {
    //SELECT id,room_id as roomId,room_name as roomName,uid as userId,role FROM hd_room_user WHERE id = %s
    private Integer id;
    private String  roomName;
    private Integer userId;
    private String  roomId;
    private String  role;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "RoomUser{" +
                "id=" + id +
                ", roomName='" + roomName + '\'' +
                ", userId='" + userId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
