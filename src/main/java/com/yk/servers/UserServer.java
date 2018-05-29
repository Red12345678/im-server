package com.yk.servers;

import com.yk.entities.Message;
import com.yk.entities.User;
import com.yk.utils.CommonUtils;
import com.yk.utils.MysqlUtils;
import com.yk.utils.RedisUtils;
import com.yk.utils.SerializeUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 * [com.yk.servers desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/5/29
 */
public class UserServer {

    private static Logger logger = Logger.getLogger(UserServer.class);

    public User getUserInfo(Message m) {
        User u = null;
        try {
            u = userCache(null, m, false);
            logger.info("userCache=>" + u);
            if (null != u) return u;
            String userSql = ImServer.conf.getProperty("jdbc.s.user.info.sql");
            if (m.getRole().toLowerCase().equals("t"))
                userSql = ImServer.conf.getProperty("jdbc.t.user.info.sql");
            userSql = String.format(userSql, m.getUid());
            logger.info("MysqlUtils => " + userSql);
            u = new User();
            BeanUtils.populate(u, MysqlUtils.find(userSql));
            userCache(u, m, true);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return u;
    }


    /**
     * [描述： desc]
     *
     * @param u User
     * @param m Message
     * @param b true => insert false => get
     * @return
     * @author yangkun[Email:vectormail@163.com] 2018/5/29
     */
    public User userCache(User u, Message m, boolean b) {
        Jedis j;
        User  user = null;
        j = RedisUtils.instance();
        if (null == j) return null;
        String t = ImServer.conf.getProperty("im.redis.user.cache.table", "im:users");
        String k = CommonUtils.getUserChannelUK(m);
        if (b) {
            j.hset(t, k, SerializeUtils.serialize(u));
            user = u;
        } else {
            if (j.hexists(t, k))
                user = (User) SerializeUtils.unSerialize(j.hget(t, k));
        }
        return user;


    }
}
