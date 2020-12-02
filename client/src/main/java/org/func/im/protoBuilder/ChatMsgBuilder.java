package org.func.im.protoBuilder;

import org.func.im.client.ClientSession;
import org.func.im.common.bean.ChatMsg;
import org.func.im.common.bean.User;
import org.func.im.common.bean.msg.ProtoMsg;

/**
 * @author lxt
 * @create 2020-12-02 11:06
 */
public class ChatMsgBuilder extends BaseBuilder {

    private User user;

    private ChatMsg chatMsg;

    public ChatMsgBuilder(ClientSession session,User user,ChatMsg chatMsg) {
        super(ProtoMsg.HeadType.MESSAGE_REQUEST, session);
        this.user=user;
        this.chatMsg=chatMsg;
    }

    public ProtoMsg.Message build(){
        ProtoMsg.Message message = buildCommon(-1);

        ProtoMsg.MessageRequest.Builder mb = ProtoMsg
                .MessageRequest
                .newBuilder();
        //填充信息
        chatMsg.fillMsg(mb);

        message.toBuilder().setMessageRequest(mb);
        return message;
    }

    public static ProtoMsg.Message buildChatMsg(ClientSession clientSession,User user,ChatMsg chatMsg){
        ChatMsgBuilder cb = new ChatMsgBuilder(clientSession,user,chatMsg);
        return cb.build();
    }


}
