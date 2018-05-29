package com.yk.servers;

import com.yk.entities.Message;
import com.yk.utils.MysqlUtils;
import org.apache.log4j.Logger;

/**
 * [com.yk.servers desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/5/29
 */
public class MessageServer {

    private static Logger logger = Logger.getLogger(UserServer.class);

    public void insertMsgToDB(Message m) {
        MysqlUtils.insertMsgToDB(m);
    }
}
