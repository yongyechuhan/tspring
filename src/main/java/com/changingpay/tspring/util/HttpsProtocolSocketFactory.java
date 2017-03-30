package com.changingpay.tspring.util;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.*;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Created by Liuxin on 2017/3/30.
 */
public class HttpsProtocolSocketFactory implements ProtocolSocketFactory{

    private final String keyFilePath;

    private final String keyPasswd;

    public HttpsProtocolSocketFactory(String filepath, String keyPasswd){
        this.keyFilePath = filepath;
        this.keyPasswd = keyPasswd;
    }

    private Log logger = LogFactory.getLog(HttpsProtocolSocketFactory.class);

    private SSLContext initSSLContext(){
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            //加载本地证书
            if(StringUtils.isEmpty(keyFilePath) || StringUtils.isEmpty(keyPasswd)){
                logger.info("证书路径及证书密码不能为空！");
                return sslContext;
            }
            FileInputStream inputStream = new FileInputStream(new File(keyFilePath));
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(inputStream, keyPasswd.toCharArray());

            TrustManagerFactory tmFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmFactory.init(keyStore);
            sslContext.init(null,
                     tmFactory.getTrustManagers(),
                    new SecureRandom());
        } catch (Exception e) {
            logger.error("初始化ssl上下文失败！", e);
        }
        return sslContext;
    }

    private SSLContext getSSLContext(){
        return initSSLContext();
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort)
            throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort, HttpConnectionParams params)
            throws IOException, UnknownHostException, ConnectTimeoutException {
        if(params == null){
            throw new IllegalArgumentException("请求参数不能为空！");
        }
        int timeout = params.getConnectionTimeout();
        SocketFactory socketFactory = getSSLContext().getSocketFactory();
        if(timeout == 0){
            return socketFactory.createSocket(host, port, clientHost, clientPort);
        } else {
            Socket socket = socketFactory.createSocket();
            SocketAddress clientAddr = new InetSocketAddress(clientHost, clientPort);
            SocketAddress remoteAddr = new InetSocketAddress(host, port);
            socket.bind(clientAddr);
            socket.connect(remoteAddr);
            return socket;
        }
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }
}
