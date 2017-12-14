package codec;

import command.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import serialize.JsonSerializier;
import serialize.Serializier;

/**
 * Created by wangye on 17/12/7.
 */
public class NettyClientEncoder extends MessageToByteEncoder<RpcRequest> {
    private Serializier serializier = new JsonSerializier();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest, ByteBuf byteBuf) throws Exception {

        byte[] data = serializier.serilize(rpcRequest);
        int length = data.length;
        byteBuf.writeInt(length);
        byteBuf.writeBytes(data);


    }
}
