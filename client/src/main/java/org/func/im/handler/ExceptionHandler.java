package org.func.im.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.func.im.client.ClientSession;
import org.func.im.common.bean.exception.InvalidFrameException;
import org.springframework.stereotype.Service;

/**
 * @author lxt
 * @create 2020-12-02 15:02
 */
@Slf4j
@ChannelHandler.Sharable
@Service("exceptionHandler")
public class ExceptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        if (cause instanceof InvalidFrameException){
            log.info("无效桢异常:{}",cause.getMessage());
            ClientSession.getSession(ctx).close();
            return;
        }else {
            ctx.close();
            log.error(cause.getMessage());

            //todo 准备开始重连


        }

        super.exceptionCaught(ctx, cause);
    }

    /**
     * 通道数据读取完成，做刷新操作
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
