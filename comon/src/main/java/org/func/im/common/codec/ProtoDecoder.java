package org.func.im.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.func.im.ProtoInstant;
import org.func.im.common.bean.exception.InvalidFrameException;
import org.func.im.common.bean.msg.ProtoMsg;

import java.util.List;

/**
 * @author lxt
 * @create 2020-12-03 9:58
 */
@Slf4j
public class ProtoDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx,
                          ByteBuf in, List<Object> list) throws Exception {
        in.resetReaderIndex();

        int readableLength = in.readableBytes();

        //一个short是2个字节  魔数+版本号 4个字节了，消息长度随便发个int就8个字节了，用8个字节来判断包头长度
        //不够读取直接返回
        if (readableLength<8){
            return;
        }

        short magic = in.readShort();

        //校验魔数
        if (magic!=ProtoInstant.MAGIC_CODE){
            String error="登陆口令不正确:"+ctx.channel().remoteAddress().toString();
            throw new InvalidFrameException(error);
        }

        //读取版本号
        short versionNo = in.readShort();

        //版本号不对暂时就先不管了--不知道如何处理
        if (versionNo!=ProtoInstant.VERSION_CODE){
            String info="当前版本:%s,请求版本号:%s,请求地址:"+ ctx.channel().remoteAddress();
            info = String.format(info, ProtoInstant.VERSION_CODE, versionNo);
            log.info(info);
        }

        //读取过来的消息长度
        int length = in.readInt();

        //可读数据不足，作半包处理
        if (length>in.readableBytes()){
           //重置可读位置
            in.resetReaderIndex();
        }

        byte[] array=null;
        //堆内存
        if (in.hasArray()){
            ByteBuf slice = in.slice();
            array= slice.array();
        }else {
            //堆外内存
            array = new byte[length];
            in.writeBytes(array,0,length);
        }

        ProtoMsg.Message outMsg = ProtoMsg.Message.parseFrom(array);

        if (outMsg!=null){
            list.add(outMsg);
        }

    }
}
