package com.yk.utils;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtils {


    private static String charset = "ISO-8859-1";//必须是ISO-8859-1
    private static Logger logger  = Logger.getLogger(SerializeUtils.class);

    /**
     * 对象序列化为字符串
     *
     * @param object
     * @return
     */
    public static String serialize(Object object) {
        String                objectStr = null;
        ObjectOutputStream    out       = null;
        ByteArrayOutputStream bo        = null;
        try {
            bo = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bo);
            out.writeObject(object);
            objectStr = bo.toString(charset);
        } catch (Exception e) { logger.info(e.getMessage()); } finally {
            try {
                if (null != out) out.close();
                if (null != bo) bo.close();
            } catch (Exception e1) {logger.info(e1.getMessage());}
        }
        return objectStr;
    }

    /**
     * 字符串序列化为对象
     *
     * @param objectStr
     * @return
     * @throws Exception
     */
    public static Object unSerialize(String objectStr){

        Object               o  = null;
        ObjectInputStream    in = null;
        ByteArrayInputStream bi = null;
        try {
            bi = new ByteArrayInputStream(objectStr.getBytes(charset));
            in = new ObjectInputStream(bi);
            o = in.readObject();
        } catch (Exception e) { logger.info(e.getMessage()); } finally {
            try {
                if (null != in) in.close();
                if (null != bi) bi.close();
            } catch (Exception e2) {}
        }
        return o;
    }

    public static String getCharset() {
        return charset;
    }

    public static void setCharset(String charset) {
        SerializeUtils.charset = charset;
    }
}