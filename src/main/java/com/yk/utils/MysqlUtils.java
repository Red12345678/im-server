package com.yk.utils;

import com.yk.entities.Message;
import com.yk.entities.User;
import com.yk.servers.ImServer;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        int               i     = 0;
        try {
            conn = createConn();
            String insertSql = ImServer.conf.getProperty("jdbc.inser.msg.record.sql");
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

    public static int insert(String table, Map<String, Object> m) {
        Connection        conn  = null;
        PreparedStatement pstmt = null;
        int               i     = 0;
        StringBuilder     sb;
        ResultSet         rs    = null;
        try {
            conn = createConn();
            sb = new StringBuilder("INSERT INTO ");
            sb.append(table);
            sb.append(" (");
            m.keySet().forEach((k) -> {
                sb.append(k);
                sb.append(",");
            });
            sb.delete(sb.lastIndexOf(","), sb.length());
            sb.append(") VALUES (");
            m.values().forEach((v) -> {
                sb.append("'");
                sb.append(v);
                sb.append("'");
                sb.append(",");
            });
            sb.delete(sb.lastIndexOf(","), sb.length());
            sb.append(")");
            logger.info(sb.toString());
            pstmt = conn.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS);
            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                i = rs.getInt(1);
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        } finally {
            closeResource(conn, null, rs, pstmt);
        }
        return i;
    }
    public static Map<String, Object> find(String sql) {
        Map<String, Object> m = null;
        StringBuilder       sb;
        try {
            sb = new StringBuilder(sql);
            sb.append(" LIMIT 1");
            m = select(sb.toString()).get(0);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return m;
    }

    public static List<Map<String, Object>> select(String sql) {
        Connection                conn = null;
        Statement                 stmt = null;
        ResultSet                 rs   = null;
        List<Map<String, Object>> lm   = null;
        try {
            conn = createConn();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            lm = new ArrayList<>();
            Map<String, Object> m = new ConcurrentHashMap<>();
            while (rs.next()) {
                ResultSetMetaData rsm = rs.getMetaData();
                int               c   = rsm.getColumnCount();
                for (int i = 1; i <= c; i++) m.put(rsm.getColumnLabel(i), rs.getObject(rsm.getColumnLabel(i)));
                lm.add(m);
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
