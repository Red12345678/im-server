package com.yk.serverimpl;

import com.yk.entities.RoomUser;
import com.yk.im.ImServer;
import com.yk.servers.RoomServer;
import com.yk.utils.MysqlUtils;

import java.util.List;

/**
 * [com.yk.serverimpl desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/7/2
 */
public class RoomServerImpl implements RoomServer {
    @Override
    public List<RoomUser> findRoomById(String id) {
        return MysqlUtils.select(new RoomUser(),String.format(ImServer.conf.getProperty("im.jdbc.room.sql"),id));
    }
}
