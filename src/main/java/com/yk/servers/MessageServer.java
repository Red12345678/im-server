package com.yk.servers;

import com.yk.entities.Message;

/**
 * [com.yk.servers desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/5/29
 */
public interface MessageServer {

     void insertMsgToDB(Message m);
}
