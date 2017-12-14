package test;

import proxy.ClientProxy;
import remoting.NettyClient;

/**
 * Created by wangye on 17/12/11.
 */
public class TestClient {
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        nettyClient.start();
        ClientProxy clientProxy = new ClientProxy(nettyClient);
        TestInterface rpcProxy = clientProxy.createRpcProxy(TestInterface.class, "127.0.0.1", 3462);
        System.out.println(rpcProxy.helloWorld("tom"));
    }
}
