package com.yk.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    private static Map<String, String> extraHeader = null;
    private static String url;
    private static Map<String, Object> params  = null;
    private static MetHodEnum          method  = MetHodEnum.POST;
    private static Map<String, String> headers = null;
    private static Logger              logger  = Logger.getLogger(HttpUtils.class);

    /**
     * [描述： desc]
     *
     * @return String
     * @author yangkun[Email:vectormail@163.com] 2018/4/8
     */
    public static String httpRequest() throws Exception {
        OutputStreamWriter out    = null;
        BufferedReader     reader = null;
        StringBuilder      response;
        HttpURLConnection  conn   = null;
        try {
            URL httpUrl = new URL(url);
            conn = initHeader((HttpURLConnection) httpUrl.openConnection());
            setConnMethod(conn);
            conn.setConnectTimeout(5000);
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();
            String paramString = prarmBuild();
            if (paramString != null) {
                out = new OutputStreamWriter(conn.getOutputStream());
                out.write(paramString);
                out.flush();
            }
            if (conn.getResponseCode() != 200) return null;
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String lines;
            response = new StringBuilder();
            while ((lines = reader.readLine()) != null)
                response.append(new String(lines.getBytes(), "utf-8"));
            reader.close();
            reader = null;
            logger.info(response.toString());
            return response.toString();
        } finally {
            if (out != null) out.close();
            if (reader != null) reader.close();
            if (conn != null) conn.disconnect();
        }
    }

    /**
     * [描述： 设置请求方法]
     *
     * @param connection HttpURLConnection
     * @author yangkun[Email:vectormail@163.com] 2018/5/4
     */
    private static void setConnMethod(HttpURLConnection connection) {
        try {
            switch (method) {
                case GET:
                    connection.setRequestMethod("GET");
                    break;
                case PUT:
                    connection.setRequestMethod("PUT");
                    break;
                case POST:
                    connection.setRequestMethod("POST");
                    break;
                default:
                    connection.setRequestMethod("POST");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * [描述： 构建参数]
     *
     * @return String
     * @author yangkun[Email:vectormail@163.com] 2018/5/4
     */
    private static String prarmBuild() {
        StringBuilder paramStr = new StringBuilder();
        if (params != null && params.size() > 0) {
            for (String k : params.keySet())
                paramStr.append(k).append("=").append(params.get(k)).append("&");

        }
        return paramStr.length() > 0 ? paramStr.replace(paramStr.length() - 1, paramStr.length(), "").toString() : null;
    }

    /**
     * [描述： 初始化边境头部]
     *
     * @param connection HttpURLConnection
     * @return
     * @author yangkun[Email:vectormail@163.com] 2018/5/4
     */
    private static HttpURLConnection initHeader(HttpURLConnection connection) {

        if (headers == null || headers.size() == 0) {
            headers = new HashMap<>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            headers.put("Accept", "application/json");
            headers.put("connection", "akeep-alive");
        }
        if (extraHeader != null && extraHeader.size() > 0) {
            for (String k : extraHeader.keySet())
                connection.setRequestProperty(k, extraHeader.get(k));
        }
        for (String k : headers.keySet())
            connection.setRequestProperty(k, headers.get(k));

        return connection;
    }


    public static Map<String, Object> getParams() {
        return params;
    }

    public static void setParams(Map<String, Object> params) {
        HttpUtils.params = params;
    }

    public static MetHodEnum getMethod() {
        return method;
    }

    public static void setMethod(MetHodEnum method) {
        HttpUtils.method = method;
    }


    public static Map<String, String> getHeaders() {
        return headers;
    }

    public static void setHeaders(Map<String, String> headers) {
        HttpUtils.headers = headers;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        HttpUtils.url = url;
    }

    public static Map<String, String> getExtraHeader() {
        return extraHeader;
    }

    public static void setExtraHeader(Map<String, String> extraHeader) {
        HttpUtils.extraHeader = extraHeader;
    }
}