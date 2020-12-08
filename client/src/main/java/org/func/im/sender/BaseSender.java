package org.func.im.sender;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.func.im.client.ClientSession;
import org.func.im.common.bean.User;
import org.func.im.common.bean.msg.ProtoMsg;

/**
 * @author lxt
 * @create 2020-12-02 9:47
 */
@Slf4j
@Data
public abstract class BaseSender {

    private ClientSession session;

    private User user;


    //判断是否连接
    public boolean isConnected(){

        if (null==session){
            log.info("session is null");
            return false;
        }
      return  session.isConnect();
    }

    //判断是否登录
    public boolean isLogin(){

        if (null==session){
            log.info("session is null");
            return false;
        }
        return session.isLogin();

    }


    public void  sendMsg(ProtoMsg.Message message){

        if (null==session||!session.isConnect()){
            log.info("连接还未成功");
            return;
        }

        Channel channel = session.getChannel();

        ChannelFuture f = channel.writeAndFlush(message);

        f.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()){
                    sendSucced(message);
                }else {
                    sendFailed(message);
                }
            }
        });


    }

    public void sendSucced(ProtoMsg.Message message){
        log.info("发送成功:"+message);
    }

    public void sendFailed(ProtoMsg.Message message){
        log.info("发送失败:"+message);
    }



}
