package org.func.im.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.func.im.client.command.BaseCommand;
import org.func.im.client.command.impl.ChatConsoleCommand;
import org.func.im.client.command.impl.ClientCommandMenu;
import org.func.im.client.command.impl.LoginConsoleCommand;
import org.func.im.client.command.impl.LogoutConsoleCommand;
import org.func.im.common.bean.ChatMsg;
import org.func.im.common.bean.User;
import org.func.im.common.bean.msg.ProtoMsg;
import org.func.im.common.cocurrent.ExecuteTask;
import org.func.im.common.cocurrent.FutureTaskScheduler;
import org.func.im.protoBuilder.ChatMsgBuilder;
import org.func.im.protoBuilder.LoginMsgBuilder;
import org.func.im.sender.ChatSender;
import org.func.im.sender.LoginSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author lxt
 * @create 2020-12-01 11:02
 */
@Slf4j
@Data
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
                channel.closeFuture().addListener(closeFutureListener);

                connectFlag=true;
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


    //初始化菜单
    public void initCommandMap(){

        commandMap=new HashMap<>();
        commandMap.put(ChatConsoleCommand.KEY,chatConsoleCommand);
        commandMap.put(ClientCommandMenu.KEY,clientCommandMenu);
        commandMap.put(LoginConsoleCommand.KEY,loginConsoleCommand);
        commandMap.put(LogoutConsoleCommand.KEY,logoutConsoleCommand);

        StringBuilder sb = new StringBuilder();

        sb.append("[menu");
        commandMap.forEach((k,v)->{
            sb.append(v.getKey());
            sb.append(":");
            sb.append(v.getTip());
            sb.append("|");
        });
        sb.append("]");
        menuString=sb.toString();
        clientCommandMenu.setAllCommandsShow(menuString);

    }

    public void startConnnectServer(){
        FutureTaskScheduler.add(new ExecuteTask() {
            @Override
            public void execute() {
                nettyClient.setConnectFutureListener(connectFutureListener);
                nettyClient.doConnect();
            }
        });
    }


    public synchronized  void  notifyCommandThread(){
        this.notify();
    }


    public synchronized void waitCommandThread(){
        try{
            this.wait(1000);
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }


    /**
     * 启动命令线程
     */
    public void startCommandThread(){
        Thread.currentThread().setName("主线程");
        //一直重连...
        while (!connectFlag){
            startConnnectServer();
            waitCommandThread();
        }

        while (null!=session){

            Scanner scanner = new Scanner(System.in);

            String key = scanner.next();

            BaseCommand baseCommand = commandMap.get(key);

            if (baseCommand==null){
                log.error("输入的指令无法识别");
                continue;
            }

            switch (key){
                case LoginConsoleCommand.KEY:
                    baseCommand.exec(scanner);
                    startLogin((LoginConsoleCommand) baseCommand);

                    break;
                case  ChatConsoleCommand.KEY:
                    baseCommand.exec(scanner);
                    startOneChat((ChatConsoleCommand) baseCommand);
                    break;
                case LogoutConsoleCommand.KEY:
                    baseCommand.exec(scanner);
                    startLogOut((LogoutConsoleCommand)baseCommand);
                    break;

            }


        }




    }


    /**
     * 发送聊天消息
     */
    public void startOneChat(ChatConsoleCommand chatConsoleCommand){

        if (!session.isLogin()){
            log.info("当前尚未登录");
            return;
        }

        ChatMsg chatMsg = new ChatMsg(user);

        chatMsg.setContent(chatConsoleCommand.getMessage());

        chatMsg.setTo(chatConsoleCommand.getToUserId());

        ProtoMsg.Message msg = ChatMsgBuilder.buildChatMsg(session, user, chatMsg);

        chatSender.sendMsg(msg);

    }

    public void startLogin(LoginConsoleCommand loginConsoleCommand){

        if (!connectFlag){
            log.info("暂未建立连接..");
            return;
        }

        User user = new User();
        user.setDevId("1111");
        user.setNickName(loginConsoleCommand.getUserName());
        user.setToken(loginConsoleCommand.getPassword());

        session.setUser(user);

        ProtoMsg.Message message = LoginMsgBuilder.buildLoginMsg(session, this.user);

        loginSender.sendMsg(message);

    }

    private void startLogOut(LogoutConsoleCommand logoutConsoleCommand){
        if (!session.isLogin()){
            log.info("尚未登录");
            return;
        }

        //todo

    }

}
