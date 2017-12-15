package test;


import registry.ServerAddr;
import remoting.NettyServerWithRegistry;

/**
 * Created by wangye on 17/12/15.
 */
public class TestServer {
    public static void main(String[] args) {
        NettyServerWithRegistry nettyServer = new NettyServerWithRegistry(3489,new ServerAddr("172.31.32.177",3463));//server
    }
}
