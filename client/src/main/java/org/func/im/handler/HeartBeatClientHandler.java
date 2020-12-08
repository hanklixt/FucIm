package org.func.im.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.func.im.client.ClientSession;
import org.func.im.common.bean.User;
import org.func.im.common.bean.msg.ProtoMsg;
import org.func.im.protoBuilder.HeartMsgBuilder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author lxt
 * @create 2020-12-02 15:00
 */
@Slf4j
@ChannelHandler.Sharable
@Service("heartBeatClientHandler")
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {

    private final  static  int HEARTBEAT_INTERVAL=100;
    /**
     * 当处理器被加入到流水线之后开始发送心跳
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ClientSession session = ClientSession.getSession(ctx);
        User user = session.getUser();
        ProtoMsg.Message message = HeartMsgBuilder.buildHeartBeat(session, user);
        heartBeat(ctx,message);
    }

    public void heartBeat(ChannelHandlerContext ctx,ProtoMsg.Message message){

        ctx.executor().schedule(new Runnable() {
            @Override
            public void run() {
                if (ctx.channel().isActive()){
//                    ctx.channel().writeAndFlush(message);      //发送到尾部
                    //两个write flush 有区别。ctx writeAndFlush 发到下一站--
                    ctx.writeAndFlush(message);
//                    heartBeat(ctx,message);
                }
            }
        },HEARTBEAT_INTERVAL, TimeUnit.SECONDS);

    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg==null||!(msg instanceof ProtoMsg.Message)){
            super.channelRead(ctx,msg);
            return;
        }
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;

        ProtoMsg.HeadType type = pkg.getType();

        if (!type.equals(ProtoMsg.HeadType.HEART_BEAT)){
            super.channelRead(ctx,msg);
        }else {
            log.info("收到回写的心跳from server");
        }


    }
}
