package org.func.im.protoBuilder;

import org.func.im.client.ClientSession;
import org.func.im.common.bean.User;
import org.func.im.common.bean.msg.ProtoMsg;

/**
 * @author lxt
 * @create 2020-12-02 10:31
 */
public class LoginMsgBuilder extends BaseBuilder {

    //用户
    private User user;

    public LoginMsgBuilder(ClientSession session,User user) {
        super(ProtoMsg.HeadType.LOGIN_REQUEST, session);
        this.user=user;
    }

    public ProtoMsg.Message build(){

        ProtoMsg.Message message = buildCommon(-1);
        ProtoMsg.LoginRequest build = ProtoMsg.LoginRequest.newBuilder()
                .setDeviceId(user.getDevId())
                .setToken(user.getToken())
                .setPlatform(user.getPlatform().ordinal())
                .setUid(user.getUid()).build();
       return message.toBuilder().setLoginRequest(build).build();
    }

    public static ProtoMsg.Message buildLoginMsg(ClientSession session,User user){

        LoginMsgBuilder lb = new LoginMsgBuilder(session, user);

        return lb.build();

    }



}
