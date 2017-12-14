package proxy;

import command.RpcRequest;
import remoting.NettyClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by wangye on 17/12/11.
 */
public class ClientProxy {
    private NettyClient nettyClient;

    public ClientProxy(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    public <T> T createRpcProxy(Class<T> interfaceClass, String remoteIp, int remotePort) {
        if (interfaceClass == null) {
            throw new RuntimeException("请求的类不存在");
        }
        T t = (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{interfaceClass}, new RpcInvocationHandler(interfaceClass, remoteIp, remotePort));
        return t;

    }

    class RpcInvocationHandler implements InvocationHandler {
        private Class interfaceClass;
        private String remoteIp;
        private int remotePort;

        public RpcInvocationHandler(Class interfaceClass, String remoteIp, int remotePort) {
            this.interfaceClass = interfaceClass;
            this.remoteIp = remoteIp;
            this.remotePort = remotePort;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setInterfaceFullName(interfaceClass.getName());
            rpcRequest.setMethodName(method.getName());
            rpcRequest.setParams(args);
            rpcRequest.setRemoteIp(remoteIp);
            rpcRequest.setRemotePort(remotePort);
            rpcRequest.setParamTypes(method.getParameterTypes());
            Object result = nettyClient.invokeSync(rpcRequest);
            return result;

        }


    }

}
