package org.func.imserver.server;

import com.google.common.collect.Maps;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.func.im.common.bean.User;

import java.util.Map;
import java.util.UUID;

/**
 * @author lxt
 * @create 2020-12-09 15:45
 */
@Data
@Slf4j
public class ServerSession {

    //map 的包装类 -- key 是包装过的 ，如果传入的name是存在的，就从该内内部维护的PlatformDependent.newConcurrentHashMap() 的map中取出实例
    private final static AttributeKey<String> KEY_USER_ID=AttributeKey
            .valueOf("key_user_id");

    private final static AttributeKey<ServerSession> SESSION_KEY=AttributeKey.valueOf("session_key");


    private Channel channel;

    private User user;

    /**
     * 是否连接
     */
    private boolean connected;

    private final String sessionId;

    /**
     * 是否登录
     */
    private boolean login;

    private Map<String,Object> map= Maps.newHashMap();


    public ServerSession(Channel channel){
        this.channel=channel;
        this.sessionId=buildSessionId();
    }

    public String buildSessionId(){
        String uuid = UUID.randomUUID().toString();
       return  uuid.replaceAll("-","");
    }

    //反向导航 --通过ctx 获取 channel 绑定的session
    public static ServerSession getSession(ChannelHandlerContext ctx){
       Channel channel = ctx.channel();
        return channel.attr(ServerSession.SESSION_KEY).get();
    }


    //设置用户
    public void setUser(User user){
        this.user=user;
        user.setSessionId(sessionId);
    }


    public User getUser(){
        return user;
    }





}
