package com.yk.servers;

import com.yk.entities.Message;
import com.yk.entities.User;

/**
 * [com.yk.servers desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/5/29
 */
public interface UserServer {



     User getUserInfo(Message m) ;

    /**
     * [描述： desc]
     *
     * @param u User
     * @param m Message
     * @param b true => insert false => get
     * @return
     * @author yangkun[Email:vectormail@163.com] 2018/5/29
     */
     User userCache(User u, Message m, boolean b);
}
