package test;

import registry.ServerAddr;
import remoting.NettyServer;
import remoting.NettyServerWithRegistry;

/**
 * Created by wangye on 17/12/11.
 */
public class TestServer {
    public static void main(String[] args) {
        NettyServer nettyServer1 = new NettyServer(3463);//registry
        NettyServer nettyServer = new NettyServerWithRegistry(3462,new ServerAddr("172.31.32.177",3463));//server
    }
}
