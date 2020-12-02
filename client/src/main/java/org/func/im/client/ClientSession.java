package org.func.im.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.func.im.common.bean.User;
import org.func.im.common.bean.msg.ProtoMsg;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lxt
 * @create 2020-12-01 17:05
 */
@Data
@Slf4j
public class ClientSession {

    public static final AttributeKey<ClientSession> SESSION_KEY=
            AttributeKey.valueOf("SESSION_KEY");

    /**
     * 用户实现客户端管理
     */
    private Channel channel;

    private User user;

    // 保存登录后的sessionId
    private String sessionId;

    /**
     * 是否连接
     */
    private boolean isConnect=false;
    /**
     * 是否登录
     */
    private boolean isLogin=false;

    /**
     * 存储session的map
     */
    private Map<String,Object> map=new HashMap<>();


    public ClientSession(Channel channel){
        //绑定channel
        this.channel=channel;
        this.sessionId=String.valueOf(-1);
        //往channel里 设置session
        channel.attr(ClientSession.SESSION_KEY).set(this);
    }

    /**
     * 登录成功的方法
     */
    public static void loginSuccess(ChannelHandlerContext ctx, ProtoMsg.Message msg){
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        session.setSessionId(msg.getSessionId());
        session.setLogin(true);
        log.info("登录成功");
    }

    /**
     * 获取登录会话
     */
    public static ClientSession getSession(ChannelHandlerContext ctx){
        Channel channel = ctx.channel();
        return channel.attr(ClientSession.SESSION_KEY).get();
    }

    /**
     *获取远程地址
     */
    public String getRemoteAddress(){
       return channel.remoteAddress().toString();
    }

    /**
     * 写数据---
     */
    public ChannelFuture writeAndFlush(Object pkg){
        return channel.writeAndFlush(pkg);
    }

    /**
     * 写--设置监听器
     */
    public void WriteAndClose(Object pkg){
        ChannelFuture cf = channel.writeAndFlush(pkg);
        cf.addListener(ChannelFutureListener.CLOSE);
    }

    //关闭通道
    public void close(){
        isConnect=false;

        ChannelFuture close = channel.close();

        close.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.info("连接顺利断开");
            }
        });


    }
























}
