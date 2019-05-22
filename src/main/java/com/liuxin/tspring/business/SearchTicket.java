package com.liuxin.tspring.business;
import com.liuxin.tspring.util.HttpsProtocolSocketFactory;
import com.google.gson.Gson;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Liuxin on 2017/3/30.
 */
public class SearchTicket extends AbsBusi{
    private static Log logger = getLog(SearchTicket.class);

    /**
     * http请求
     * @param url
     * @param param
     * @return
     */
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

    /**
     * https请求
     */
    public static String sendByHttps(String keyFilePath, String keyPasswd, String url){
        String serverResp = "";
        Protocol https = new Protocol("https",
                new HttpsProtocolSocketFactory(keyFilePath, keyPasswd), 443);
        Protocol.registerProtocol("https", https);
        GetMethod get = new GetMethod(url);
        HttpClient client = new HttpClient();
        try{
            client.executeMethod(get);
            serverResp = get.getResponseBodyAsString();
            //serverResp = new String(serverResp.getBytes("ISO-8859-1"), "UTF-8");
            Protocol.unregisterProtocol("https");
            return serverResp;
        }catch (Exception e){
            logger.error("获取服务器响应失败！", e);
            return "error";
        }
    }
}
