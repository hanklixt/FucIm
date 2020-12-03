package org.func.im.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.func.im.client.command.BaseCommand;
import org.func.im.client.command.impl.ChatConsoleCommand;
import org.func.im.client.command.impl.ClientCommandMenu;
import org.func.im.client.command.impl.LoginConsoleCommand;
import org.func.im.client.command.impl.LogoutConsoleCommand;
import org.func.im.common.bean.User;
import org.func.im.sender.ChatSender;
import org.func.im.sender.LoginSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lxt
 * @create 2020-12-01 11:02
 */
@Slf4j
@AllArgsConstructor
@Service("commandController")
public class CommandController {

    //聊天命令收集类
    @Autowired
    private ChatConsoleCommand chatConsoleCommand;

    /**
     * 客户端命令收集类
     */
    @Autowired
    private ClientCommandMenu clientCommandMenu;

    /**
     * 登录命令收集
     */
    @Autowired
    private LoginConsoleCommand loginConsoleCommand;

    /**
     * 登出命令收集
     */
    @Autowired
    private LogoutConsoleCommand logoutConsoleCommand;


    @Autowired
    private LoginSender loginSender;

    @Autowired
    private ChatSender chatSender;

    @Autowired
    private NettyClient nettyClient;

    //命令map
    private Map<String, BaseCommand> commandMap;

    //菜单字符
    private String menuString;

    //连接状态
    private boolean connectFlag=false;

    private ClientSession session;

    private Channel channel;

    private User user;



    //关闭通道的回调监听器
    GenericFutureListener<ChannelFuture> closeFutureListener=new GenericFutureListener<ChannelFuture>() {
        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            log.info("连接已关闭");
            channel = channelFuture.channel();

            ClientSession clientSession = channel
                    .attr(ClientSession.SESSION_KEY)
                    .get();

            clientSession.close();
            //唤醒用户线程
            notifyCommandThread();

        }
    };


    //连接成功的回调监听器
    GenericFutureListener<ChannelFuture> connectFutureListener=new GenericFutureListener<ChannelFuture>() {
        @Override
        public void operationComplete(ChannelFuture cf) throws Exception {
            EventLoop eventLoop = cf.channel().eventLoop();
            if (cf.isSuccess()){
                connectFlag=true;
                channel = cf.channel();
                //连接成功绑定渠道会话
                session = new ClientSession(channel);
                session.setConnect(true);

                //添加监听器
                ChannelFuture channelFuture = channel.closeFuture().addListener(closeFutureListener);
            }else {
                connectFlag=false;
                log.info("连接失败,将在10s后重新连接");
                eventLoop.schedule(new Runnable() {
                    @Override
                    public void run() {
                        nettyClient.doConnect();
                    }
                },10, TimeUnit.SECONDS);
            }

        }
    };



    public synchronized  void  notifyCommandThread(){
        this.notify();
    }




    /**
     * 启动命令线程
     */
    public void startCommandThread(){
        Thread.currentThread().setName("主线程");

    }

}
