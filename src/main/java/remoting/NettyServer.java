package remoting;

import codec.NettyDecoder;
import codec.NettyServerEncoder;
import command.RpcRequest;
import command.RpcResult;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import registry.Registry;
import registry.RegistryFactory;
import registry.ServerAddr;
import util.ClassUtils;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangye on 17/12/7.
 */
public class NettyServer {
    private EventLoopGroup parentEventLoopGroup;
    private EventLoopGroup childEventLoopGroup;
    private ExecutorService businessExecutor = Executors.newFixedThreadPool(50);
    private ServerBootstrap serverBootstrap;

    private ConcurrentMap<String, Object> classCache = new ConcurrentHashMap<String, Object>();
    //需要注册的就填第二个参数
    public NettyServer(int port) {
        parentEventLoopGroup = new NioEventLoopGroup(1);
        childEventLoopGroup = new NioEventLoopGroup(8);
        serverBootstrap = new ServerBootstrap();


        serverBootstrap.option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_SNDBUF, NettyClientConfig.TCP_SEND_BUFFER)
                .option(ChannelOption.SO_RCVBUF, NettyClientConfig.TCP_RECV_BUFFER).
                option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NettyClientConfig.CONNECT_TIME_OUT);
        serverBootstrap.group(parentEventLoopGroup, childEventLoopGroup);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline()
                        .addLast(childEventLoopGroup,
                                new NettyDecoder(RpcRequest.class),
                                new NettyServerEncoder(),

                                new IdleStateHandler(0, 0, 60),
                                new NettyServerHandler());
            }
        });
        serverBootstrap.bind(port);

    }

    @ChannelHandler.Sharable
    class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {


        @Override
        protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final RpcRequest rpcRequest) throws Exception {

            RpcResult rpcResult = new RpcResult();
            rpcResult.setOpaque(rpcRequest.getOpaque());
            try {
                String interfaceFullName = rpcRequest.getInterfaceFullName();
                Object implementObject;
                implementObject = findImplementsObject(interfaceFullName);

                Method method = implementObject.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                if (method == null) {
                    throw new Exception("方法不存在！");
                }
                Object result = method.invoke(implementObject, rpcRequest.getParams());
                rpcResult.setOK(true);
                rpcResult.setResult(result);

            } catch (Exception e) {
                rpcResult.setOK(false);
                rpcResult.setResult(e.getMessage());
            }
            channelHandlerContext.writeAndFlush(rpcResult);


        }

        private Object findImplementsObject(String interfaceFullName) throws Exception {
            Object implementsClass;
            synchronized (classCache) {
                implementsClass = classCache.get(interfaceFullName);
                if (implementsClass == null) {
                    Class interfaceClass = Class.forName(interfaceFullName);
                    if (interfaceClass == null) {
                        throw new Exception("接口不存在！");
                    }
                    List<Class> allClassByInterface = ClassUtils.getAllClassByInterface(interfaceClass);
                    if (allClassByInterface.size() != 1) {
                        throw new Exception("不是有且仅有一个实现类！");
                    }
                    Class aClass = allClassByInterface.get(0);
                    implementsClass = aClass.newInstance();
                    classCache.put(interfaceFullName, implementsClass);
                }

            }
            return implementsClass;
        }

    }

}
