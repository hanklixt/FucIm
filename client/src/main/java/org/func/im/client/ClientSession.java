package org.func.im.client;

import io.netty.channel.Channel;
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

    public static void loginSuccess(ChannelHandlerContext ctx, ProtoMsg.Message msg){


    }















}
