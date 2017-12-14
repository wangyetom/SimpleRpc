package test;

import proxy.ClientProxy;
import registry.ServerAddr;
import remoting.NettyClient;

/**
 * Created by wangye on 17/12/11.
 */
public class TestClient {
    public static void main(String[] args) {

        ClientProxy clientProxy = new ClientProxy(new  ServerAddr("172.31.32.177", 3463));
        TestInterface rpcProxy = clientProxy.createRpcProxy(TestInterface.class);
        System.out.println(rpcProxy.helloWorld("tom"));
    }
}
