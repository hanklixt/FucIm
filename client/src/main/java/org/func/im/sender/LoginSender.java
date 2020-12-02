package org.func.im.sender;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.func.im.common.bean.msg.ProtoMsg;
import org.func.im.protoBuilder.LoginMsgBuilder;
import org.springframework.stereotype.Service;

/**
 * @author lxt
 * @create 2020-12-02 10:02
 */
@Slf4j
@Service("loginSender")
@Data
public class LoginSender extends BaseSender{

    public void loginSend(){
        if (!isConnected()){
            log.info("还没有建立连接");
             return;
        }
        ProtoMsg.Message message = LoginMsgBuilder.buildLoginMsg(getSession(), getUser());
        log.info("发送登录信息");
        sendMsg(message);
    }



}
