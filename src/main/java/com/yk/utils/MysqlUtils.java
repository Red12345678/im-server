package com.yk.utils;

import com.yk.entities.Message;
import com.yk.entities.User;
import com.yk.servers.ImServer;
import org.apache.log4j.Logger;

import java.sql.*;

/**
 * [com.yk.utils desc]
 *
 * @author yangkun[Email:vectormail@163.com] 2018/5/28
 */
public class MysqlUtils {

    private static Logger logger = Logger.getLogger(MysqlUtils.class);

    public static Connection createConn() {//用这个方法获取mysql的连接
        Connection conn = null;
        try {
            Class.forName(ImServer.conf.getProperty("jdbc.driver")).newInstance();//加载驱动类
            conn = DriverManager.getConnection(
                    ImServer.conf.getProperty("jdbc.host"),
                    ImServer.conf.getProperty("jdbc.username"),
                    ImServer.conf.getProperty("jdbc.password")
            );//（url数据库的IP地址，user数据库用户名，password数据库密码）
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return conn;
    }


    public static int insertMsgToDB(Message m) {
        Connection conn = null;
        PreparedStatement pstmt;
        int i = 0;
        try {
            conn = createConn();
            String insertSql = ImServer.conf.getProperty("jdbc.inser.msg.record.sql");
            pstmt = conn.prepareStatement(insertSql);
            //(`user_name`,`uid`,`touid`,`touname`,`room_id`,`content`,`type`,`stype`,`inputtime`,`role`,`head_img`,`status`)
            pstmt.setString(1, m.getUser_name());
            pstmt.setInt(2, m.getUid());
            pstmt.setInt(3, m.getTouid());
            pstmt.setString(4, m.getTouname());
            pstmt.setString(5, m.getRoom_id());
            pstmt.setString(6, m.getContent());
            pstmt.setString(7, m.getType());
            pstmt.setString(8, m.getStype());
            pstmt.setString(9, m.getInputtime());
            pstmt.setString(10, m.getRole());
            pstmt.setString(11, m.getHead_img());
            pstmt.setString(12, m.getStatus().toString());
            i = pstmt.executeUpdate();
            logger.info(pstmt);
            pstmt.close();
        } catch (Exception e) {
            logger.info(e.getMessage());
        } finally {
            closeResource(conn, null, null);
        }
        return i;
    }

    public static User getUserInfo(Message m) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        User u = null;
        try {
            conn = createConn();
            u = new User();
            String userSql = ImServer.conf.getProperty("jdbc.s.user.info.sql");
            if (m.getRole().toLowerCase().equals("t"))
                userSql = ImServer.conf.getProperty("jdbc.t.user.info.sql");
            userSql = String.format(userSql, m.getUid());
            logger.info("userSql => " + userSql);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(userSql);
            while (rs.next()) {
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setFace(rs.getString("face"));
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        } finally {
            closeResource(conn, stmt, rs);
        }
        return u;

    }

    public static void closeResource(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (null != rs) rs.close();
            if (null != stmt) stmt.close();
            if (null != conn) conn.close();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

    }
}
