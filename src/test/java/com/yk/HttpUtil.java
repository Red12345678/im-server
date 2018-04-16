package com.yk;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {


    /**
     * @param url
     * @param params
     * @return
     * @throws IOException
     * @作用 使用urlconnection
     */
    public static String post(String url, String params, String method) {
        OutputStreamWriter out = null;
        BufferedReader reader = null;
        String response = "";
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
            if (method == null)
                conn.setRequestMethod("POST");
            else conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Host", "www.xhb.com");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            conn.setUseCaches(false);//设置不要缓存
            conn.setInstanceFollowRedirects(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.connect();
            out = new OutputStreamWriter(conn.getOutputStream());

            out.write(params);

            out.flush();
           // reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String lines;
            System.out.println(conn.getInputStream());
            conn.disconnect();
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }


}