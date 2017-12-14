package codec;

import command.RpcResult;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import serialize.JavaSerializier;
import serialize.Serializier;

/**
 * Created by wangye on 17/12/7.
 */
public class NettyServerEncoder extends MessageToByteEncoder<RpcResult> {
    private Serializier serializier = new JavaSerializier();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcResult rpcResult, ByteBuf byteBuf) throws Exception {

        byte[] data = serializier.serilize(rpcResult);
        int length = data.length;
        byteBuf.writeInt(length);
        byteBuf.writeBytes(data);


    }
}
