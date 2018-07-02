package com.yk.servers;

import com.yk.entities.RoomUser;

import java.util.List;

/**
 * [com.yk.servers desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/7/2
 */
public interface RoomServer {

    List<RoomUser> findRoomById(String id);

}
