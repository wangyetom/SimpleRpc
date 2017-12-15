package proxy;

import command.RpcRequest;
import loadbalance.LoadBalance;
import loadbalance.RandRobinLoadBalance;
import registry.Registry;
import registry.RegistryFactory;
import registry.ServerAddr;
import remoting.NettyClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by wangye on 17/12/11.
 */
public class ClientProxy {
    private NettyClient nettyClient;
    private Registry registry;
    private LoadBalance loadBalance;

    public ClientProxy(ServerAddr registryAddr) {
        nettyClient = new NettyClient();

        this.registry = RegistryFactory.getRegistry(registryAddr,nettyClient);
        this.loadBalance = new RandRobinLoadBalance(registry);
    }

    public <T> T createRpcProxy(Class<T> interfaceClass) {
        if (interfaceClass == null) {
            throw new RuntimeException("请求的类不存在");
        }
        T t = (T) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{interfaceClass}, new RpcInvocationHandler(interfaceClass));
        return t;

    }

    class RpcInvocationHandler implements InvocationHandler {
        private Class interfaceClass;


        public RpcInvocationHandler(Class interfaceClass) {
            this.interfaceClass = interfaceClass;

        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.setInterfaceFullName(interfaceClass.getName());
            rpcRequest.setMethodName(method.getName());
            rpcRequest.setParams(args);
            ServerAddr server = loadBalance.findServer(interfaceClass.getName());
            if (server != null) {
                rpcRequest.setRemoteIp(server.getIp());
                rpcRequest.setRemotePort(server.getPort());
            } else {
                throw new RuntimeException("服务不存在");
            }
            rpcRequest.setParamTypes(method.getParameterTypes());
            Object result = nettyClient.invokeSync(rpcRequest);
            return result;

        }


    }

}
