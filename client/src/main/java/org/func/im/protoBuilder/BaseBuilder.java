package org.func.im.protoBuilder;

import lombok.Data;
import org.func.im.client.ClientSession;
import org.func.im.common.bean.msg.ProtoMsg;

/**
 * @author lxt
 * @create 2020-12-02 10:13
 */
public class BaseBuilder {

    private long seqId;

    private ProtoMsg.HeadType type;

    private ClientSession session;


    public BaseBuilder(ProtoMsg.HeadType type,ClientSession session){
        this.type=type;
        this.session=session;
    }

    public ProtoMsg.Message buildCommon(long seqId){

        this.seqId=seqId;

        return ProtoMsg.Message
                .newBuilder()
                .setSequence(seqId)
                .setSessionId(session.getSessionId())
                .setType(type).buildPartial();
    }


}
