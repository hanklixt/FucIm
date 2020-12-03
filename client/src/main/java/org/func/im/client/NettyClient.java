package org.func.im.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.func.im.common.bean.User;
import org.func.im.common.codec.ProtoDecoder;
import org.func.im.common.codec.ProtoEncoder;
import org.func.im.handler.ChatMsgHandler;
import org.func.im.handler.ExceptionHandler;
import org.func.im.handler.HeartBeatClientHandler;
import org.func.im.handler.LoginResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lxt
 * @create 2020-12-03 14:35
 */
@Data
@Slf4j
@Service("nettyClient")
public class NettyClient {

    @Value("{server.host}")
    private String host;

    @Value("{server.port}")
    private Integer port;


    @Autowired
    private ChatMsgHandler chatMsgHandler;

    @Autowired
    private ExceptionHandler exceptionHandler;

    @Autowired
    private LoginResponseHandler loginResponseHandler;

    //连接成功的监听器，通过setter来设置
    private GenericFutureListener<Future<? super Void>> genericFutureListener;


    private Channel channel;

    private User user;

    //初始化标记
    private static boolean initFlag=false;

    private  Bootstrap bootstrap;

    private EventLoopGroup g;

    public NettyClient() {
        this.g = new NioEventLoopGroup();
    }

    /**
     * 重连或连接
     */
    public void  doConnect() {

        try{
            bootstrap=new Bootstrap();

            bootstrap.group(g);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.SO_KEEPALIVE,true);
            bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            bootstrap.remoteAddress(host,port);

            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sch) throws Exception {

                    sch.pipeline().addLast("decoder",new ProtoEncoder());
                    sch.pipeline().addLast("encoder",new ProtoDecoder());
                    sch.pipeline().addLast(loginResponseHandler);
                    sch.pipeline().addLast(chatMsgHandler);
                    sch.pipeline().addLast(exceptionHandler);

                }
            });
            log.info("开始连接服务端了");
            ChannelFuture connect = bootstrap.connect().sync();
            connect.addListener(genericFutureListener);

//            ChannelFuture sync = connect.channel().closeFuture().sync();
        }catch (Exception e){
            log.error("连接失败");
        }finally {
              g.shutdownGracefully();
        }

    }









}
