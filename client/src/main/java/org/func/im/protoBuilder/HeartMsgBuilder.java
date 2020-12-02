package org.func.im.protoBuilder;

import org.func.im.client.ClientSession;
import org.func.im.common.bean.User;
import org.func.im.common.bean.msg.ProtoMsg;

/**
 * @author lxt
 * @create 2020-12-02 10:52
 */
public class HeartMsgBuilder  extends BaseBuilder{

    //用户
    private User user;

    public HeartMsgBuilder(ClientSession session,User user) {
        super(ProtoMsg.HeadType.HEART_BEAT, session);
        this.user=user;
    }


    //构建心跳信息
    public ProtoMsg.Message build(){
        ProtoMsg.Message message = buildCommon(-1);

        ProtoMsg.MessageHeartBeat lb = ProtoMsg.MessageHeartBeat
                .newBuilder()
                .setJson("{\"from\":\"client\"}")
                .setSeq(0)
                .setUid(user.getUid()).build();
        message.toBuilder().setHeartBeat(lb).build();
        return message;
    }

    //构建心跳信息
    public static ProtoMsg.Message buildHeartBeat(ClientSession session,User user){
        HeartMsgBuilder hb = new HeartMsgBuilder(session,user);
        ProtoMsg.Message build = hb.build();
        return build;
    }


}
