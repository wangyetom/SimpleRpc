package test;

import proxy.ClientProxy;
import proxy.RegistryProxy;
import registry.ServerAddr;
import remoting.NettyClient;

/**
 * Created by wangye on 17/12/11.
 */
public class TestClientDirect {
    public static void main(String[] args) {

        RegistryProxy registryProxy = new RegistryProxy(new NettyClient());
        TestInterface rpcProxy = registryProxy.createRpcProxy(TestInterface.class,"172.31.32.177",3463);
        System.out.println(rpcProxy.helloWorld("tom"));
    }
}
