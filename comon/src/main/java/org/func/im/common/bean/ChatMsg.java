package org.func.im.common.bean;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.func.im.common.bean.msg.ProtoMsg;

/**
 * @author lxt
 * @create 2020-12-02 11:08
 */
@Data
public class ChatMsg {

    //--聊天消息  消息类型
    public  enum MSGTYPE{
        TEXT,
        AUDIO,
        VIDEO,
        POS,
        OTHER
    }

    private User user;

    public ChatMsg(User user) {
        if (null==user){
            return;
        }
        this.user = user;
        this.time=System.currentTimeMillis();
        this.from=user.getUid();
        this.fromNick=user.getNickName();
        //默认是文本信息
        this.msgType=MSGTYPE.TEXT;

    }

    private long msgId;
    private String from;
    private String to;
    private long time;

    private MSGTYPE msgType;
    private String content;
    private String url;          //多媒体地址
    private String property;     //附加属性
    private String fromNick;     //发送者昵称
    private String json;         //附加的json串

    //填充消息
    public void fillMsg(ProtoMsg.MessageRequest.Builder mb){
        if (msgId>0){
            mb.setMsgId(msgId);
        }
        if (StringUtils.isNotBlank(from)){
            mb.setFrom(from);
        }
        if (StringUtils.isNotBlank(to)){
            mb.setTo(to);
        }
        if (time>0){
            mb.setTime(time);
        }
        if (null!=msgType){
            mb.setMsgType(msgType.ordinal());
        }
        if (StringUtils.isNotBlank(content)){
            mb.setContent(content);
        }
        if (StringUtils.isNotBlank(url)){
            mb.setUrl(url);
        }

        if (StringUtils.isNotBlank(property)){
            mb.setContent(property);
        }
        if (StringUtils.isNotBlank(fromNick)){
            mb.setContent(fromNick);
        }

        if (StringUtils.isNotBlank(json)){
            mb.setContent(json);
        }


    }






}
