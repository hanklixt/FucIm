package org.func.imserver.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lxt
 * @create 2020-12-10 17:02
 */
@Slf4j
@Service("chatServer")
public class ChatServer {

    private EventLoopGroup boss=new NioEventLoopGroup();

    private EventLoopGroup worker=new NioEventLoopGroup();

    private ServerBootstrap bootstrap=new ServerBootstrap();

    @Value("${server.port}")
    private Integer port;


    public void startServer(){

        bootstrap.group(boss,worker);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {

                //流水线中加入业务处理器
            }
        });

        try{
            ChannelFuture bind = bootstrap.bind(port).sync();
            ChannelFuture closeFuture = bind.channel().closeFuture();
            closeFuture.sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {

            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }



    }



}
