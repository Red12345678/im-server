package com.yk.serverimpl;

import com.yk.entities.Message;
import com.yk.servers.MessageServer;
import com.yk.utils.MysqlUtils;

/**
 * [com.yk.serverimpl desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/7/2
 */
public class MessageServerImpl implements MessageServer {
    @Override
    public void insertMsgToDB(Message m) {
        MysqlUtils.insertMsgToDB(m);
    }
}
