package org.func.im.sender;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.func.im.common.bean.ChatMsg;
import org.func.im.common.bean.msg.ProtoMsg;
import org.func.im.protoBuilder.ChatMsgBuilder;
import org.springframework.stereotype.Service;

/**
 * @author lxt
 * @create 2020-12-02 14:29
 */
@Slf4j
@Data
@Service("chatSender")
public class ChatSender extends BaseSender{

    public void sendChatMsg(String toUid,String content){
        if (!isConnected()){
            log.info("尚未建立连接");
            return;
        }
        if (!isLogin()){
            log.info("尚未登录");
        }

        ChatMsg chatMsg = new ChatMsg(getUser());

        chatMsg.setTo(toUid);
        chatMsg.setContent(content);
        chatMsg.setMsgType(ChatMsg.MSGTYPE.TEXT);
        chatMsg.setMsgId(System.currentTimeMillis());

        ProtoMsg.Message message = ChatMsgBuilder.buildChatMsg(getSession(), getUser(), chatMsg);

        sendMsg(message);

    }

    @Override
    public void sendSucced(ProtoMsg.Message message) {
        log.info("发送成功:"+message.getMessageRequest().getContent());
    }

    @Override
    public void sendFailed(ProtoMsg.Message message) {
        log.info("发送失败:"+message.getMessageRequest().getContent());
    }

}
