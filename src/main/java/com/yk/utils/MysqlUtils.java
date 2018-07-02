package com.yk.utils;

import com.yk.entities.Message;
import com.yk.im.ImServer;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            Class.forName(ImServer.conf.getProperty("im.jdbc.driver")).newInstance();//加载驱动类
            conn = DriverManager.getConnection(
                    ImServer.conf.getProperty("im.jdbc.host"),
                    ImServer.conf.getProperty("im.jdbc.username"),
                    ImServer.conf.getProperty("im.jdbc.password")
            );//（url数据库的IP地址，user数据库用户名，password数据库密码）
        } catch (Exception e) {
            e.printStackTrace();
            logger.info(e.getMessage());
        }
        return conn;
    }


    public static int insertMsgToDB(Message m) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        int               i     = 0;
        try {
            conn = createConn();
            String insertSql = ImServer.conf.getProperty("im.jdbc.inser.msg.record.sql");
            pstmt = conn.prepareStatement(insertSql);
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
        } catch (Exception e) {
            logger.info(e.getMessage());
        } finally {
            closeResource(conn, null, null, pstmt);
        }
        return i;
    }

    public static <T> T find(T t, String sql) {
        StringBuilder sb;
        T             tn = null;
        try {
            sb = new StringBuilder(sql);
            sb.append(" LIMIT 1");
            tn = select(t, sb.toString()).get(0);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return tn;
    }

    public static <T> List<T> select(T t, String sql) {
        Connection conn = null;
        Statement  stmt = null;
        ResultSet  rs   = null;
        List<T>    lm   = null;
        try {
            conn = createConn();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            lm = new ArrayList<>();
            Map<String, Object> m = new HashMap<>();
            while (rs.next()) {
                ResultSetMetaData rsm = rs.getMetaData();
                int               c   = rsm.getColumnCount();
                for (int i = 1; i <= c; i++) m.put(rsm.getColumnLabel(i), rs.getObject(rsm.getColumnLabel(i)));
                BeanUtils.populate(t, m);
                lm.add(t);
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        } finally {
            closeResource(conn, stmt, rs, null);
        }
        return lm;

    }

    public static void closeResource(Connection conn, Statement stmt, ResultSet rs, PreparedStatement pstmt) {
        try {
            if (null != rs) rs.close();
            if (null != stmt) stmt.close();
            if (null != pstmt) pstmt.close();
            if (null != conn) conn.close();
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

    }
}
