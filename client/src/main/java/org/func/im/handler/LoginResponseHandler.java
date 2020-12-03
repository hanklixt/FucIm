package org.func.im.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import lombok.extern.slf4j.Slf4j;
import org.func.im.ProtoInstant;
import org.func.im.client.ClientSession;
import org.func.im.common.bean.User;
import org.func.im.common.bean.msg.ProtoMsg;
import org.springframework.stereotype.Service;

/**
 * @author lxt
 * @create 2020-12-02 15:02
 */
@Slf4j
@ChannelHandler.Sharable
@Service("loginResponseHandler")
public class LoginResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMsg.Message)){
            super.channelRead(ctx,msg);
            return;
        }
        ProtoMsg.Message pkg = (ProtoMsg.Message) msg;
        if (!pkg.getType().equals(ProtoMsg.HeadType.LOGIN_RESPONSE)){
            super.channelRead(ctx,msg);
            return;
        }

        ProtoMsg.LoginResponse loginResponse = pkg.getLoginResponse();

        ProtoInstant.ResultCodeEnum result = ProtoInstant.ResultCodeEnum.values()[loginResponse.getCode()];

        if (!ProtoInstant.ResultCodeEnum.SUCCESS.getCode().equals(result.getCode())){
            log.info("登录失败，失败描述:{}",result.getDesc());
        }else {
            //登录成功
            ClientSession session = ClientSession.getSession(ctx);
            User user = session.getUser();
            ClientSession.loginSuccess(ctx,pkg);
            log.info("用户:{} 登录成功",user.getNickName());
            //获取流水线
            ChannelPipeline pipeline = ctx.pipeline();

            //登录成功移除当前处理器
            pipeline.remove(this);

            //在已存在的名叫encoder的业务处理器后面加上一个名叫heartBeat的业务处理器
            pipeline.addAfter("encoder","heartBeat",new HeartBeatClientHandler());


        }
    }
}
