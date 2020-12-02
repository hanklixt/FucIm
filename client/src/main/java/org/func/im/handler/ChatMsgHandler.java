package org.func.im.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.func.im.common.bean.msg.ProtoMsg;
import org.springframework.stereotype.Service;

/**
 * @author lxt
 * @create 2020-12-02 15:00
 */
@Slf4j
@ChannelHandler.Sharable
@Service("chatMsgHandler")
public class ChatMsgHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //判断消息实例
        if (null==msg||!(msg instanceof ProtoMsg.Message)){
            super.channelRead(ctx, msg);
            return;
        }
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        ProtoMsg.HeadType type = pkg.getType();
        //判断消息类型
        if (type.ordinal()!=ProtoMsg.HeadType.MESSAGE_REQUEST.ordinal()){
            super.channelRead(ctx,msg);
            return;
        }
        ProtoMsg.MessageRequest messageRequest = pkg.getMessageRequest();

        String content = messageRequest.getContent();
        String from = messageRequest.getFrom();
        String to = messageRequest.getTo();
        log.info("收到消息:{},来自:{}-> 给:{}",content,from,to);

    }






}
