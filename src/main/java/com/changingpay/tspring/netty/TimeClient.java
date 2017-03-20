package com.changingpay.tspring.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by think on 2017/3/19.
 */
@Service
public class TimeClient {

    @Value("#{serverPortConfig[NettySocketServerAddr]}")
    private String serverHost;

    @Value("#{serverPortConfig[NettySocketServerPort]}")
    private int serverPort;

    public void connect() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TimeClientHandler());
                        }
            });

            ChannelFuture f = b.connect(serverHost, serverPort).sync();

            f.channel().closeFuture().sync();
        }finally{
            group.shutdownGracefully();
        }
    }
}
