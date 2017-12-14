package remoting;

import codec.NettyClientEncoder;
import codec.NettyDecoder;
import command.RpcRequest;
import command.RpcResult;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by wangye on 17/12/7.
 */
public class NettyClient {
    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;
    private ConcurrentMap<Channel, ConcurrentMap<Integer, RpcResponse>> resultMap = new ConcurrentHashMap<Channel, ConcurrentMap<Integer, RpcResponse>>();
    private ConcurrentMap<String, Channel> channelCache = new ConcurrentHashMap<String, Channel>();
    private ConcurrentMap<Channel, AtomicInteger> channelOpaque = new ConcurrentHashMap<Channel, AtomicInteger>();

    public NettyClient() {
        eventLoopGroup = new NioEventLoopGroup(8);
        bootstrap = new Bootstrap();
    }

    public void start() {
        bootstrap.group(eventLoopGroup)
                .option(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NettyClientConfig.CONNECT_TIME_OUT)
                .option(ChannelOption.SO_SNDBUF, NettyClientConfig.TCP_SEND_BUFFER)
                .option(ChannelOption.SO_RCVBUF, NettyClientConfig.TCP_RECV_BUFFER)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(eventLoopGroup,
                                        new NettyDecoder(RpcResult.class))
                                .addLast(new NettyClientEncoder()).addLast(new IdleStateHandler(0, 0, 60))
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    public Object invokeSync(RpcRequest rpcRequest) throws InterruptedException {
        String remoteIp = rpcRequest.getRemoteIp();
        int remotePort = rpcRequest.getRemotePort();
        String addr = remoteIp + ":" + remotePort;
        Channel channel = createChannel(addr);

        RpcResponse rpcResponse = new RpcResponse();
        int opaque = channelOpaque.get(channel).getAndIncrement();
        rpcRequest.setOpaque(opaque);
        rpcRequest.setLocalIp(channel.localAddress().toString());
        resultMap.get(channel).put(opaque, rpcResponse);
        channel.writeAndFlush(rpcRequest);
        rpcResponse.wait(NettyClientConfig.WAIT_RESPONSE_TIME_OUT);
        if (rpcResponse.isOk() == false) {
            throw new RuntimeException("请求超时");
        }
        return rpcResponse.getData();


    }

    private synchronized Channel createChannel(final String addr) throws InterruptedException {
        Channel cw = this.channelCache.get(addr);
        if (cw != null && cw.isActive()) {
            return cw;
        }
        ChannelFuture channelFuture = bootstrap.connect(addr.split(":")[0], Integer.parseInt(addr.split(":")[1]));
        channelFuture.awaitUninterruptibly();
        Channel channel = channelFuture.channel();
        channelCache.put(addr, channel);
        channelOpaque.put(channel, new AtomicInteger());
        resultMap.put(channel, new ConcurrentHashMap<Integer, RpcResponse>());
        return channel;

    }

    @ChannelHandler.Sharable
    private class NettyClientHandler extends SimpleChannelInboundHandler<RpcResult> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResult rpcResult) throws Exception {
            Channel channel = channelHandlerContext.channel();
            RpcResponse rpcResponse = resultMap.get(channel).remove(rpcResult.getOpaque());
            rpcResponse.done(rpcResult.getResult());

        }
    }
}
