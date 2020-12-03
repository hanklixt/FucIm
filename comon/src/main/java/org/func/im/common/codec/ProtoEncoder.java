package org.func.im.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.func.im.ProtoInstant;
import org.func.im.common.bean.msg.ProtoMsg;

/**
 * @author lxt
 * @create 2020-12-03 11:46
 */
public class ProtoEncoder extends MessageToByteEncoder<ProtoMsg.Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          ProtoMsg.Message message, ByteBuf byteBuf) throws Exception {



        if (message==null){
            return;
        }
        byteBuf.resetWriterIndex();
        //先魔数
        byteBuf.writeShort(ProtoInstant.MAGIC_CODE);
        //版本号
        byteBuf.writeShort(ProtoInstant.VERSION_CODE);

        //这里可以对消息进行加密-
        byte[] bytes = message.toByteArray();
        //字节长度
        byteBuf.writeInt(bytes.length);
        //字节
        byteBuf.writeBytes(bytes);

    }
}
