package com.changingpay.tspring.business;
import com.google.gson.Gson;
import org.apache.commons.logging.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Liuxin on 2017/3/30.
 */
public class SearchTicket extends AbsBusi{
    private static Log logger = AbsBusi.getLog(SearchTicket.class);

    public static String sendGet(String url, String param){
        BufferedInputStream bufferInput = null;
        try {
            String urlString = url + "?" + param;
            URL realUrl = new URL(urlString);
            URLConnection conn = realUrl.openConnection();

            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Host", "kyfw.12306.cn");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0");

            conn.connect();

            //获取响应头字段
            Map<String, List<String>> map = conn.getHeaderFields();
            Set<Map.Entry<String, List<String>>> keyvalue = map.entrySet();
            for(Map.Entry<String, List<String>> entry : keyvalue){
                logger.info(entry.getKey() +" "+new Gson().toJson(entry.getValue()));
            }

            bufferInput = new BufferedInputStream(conn.getInputStream());
            if(bufferInput == null){
                logger.info("服务器无响应字段");
                return "";
            }

            StringBuffer res = new StringBuffer();
            byte[] serverSend = new byte[1024];
            int readable = -1;
            while((readable = bufferInput.read(serverSend)) > 0){
                res.append(new String(serverSend, 0, readable));
            }
            logger.info("服务器响应结果"+res.toString());
            return res.toString();
        } catch (Exception e) {
            logger.error("查询余票失败，请稍后再试！", e);
            return "error";
        } finally {
            if( bufferInput != null){
                try {
                    bufferInput.close();
                } catch (IOException e) {
                    logger.error("服务器输入流关闭失败！", e);
                }
            }
        }
    }
}
