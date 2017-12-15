package test;

import registry.ServerAddr;
import remoting.NettyServer;
import remoting.NettyServerWithRegistry;

/**
 * Created by wangye on 17/12/11.
 */
public class TestRegistry {
    public static void main(String[] args) {
        NettyServer nettyServer1 = new NettyServer(3463);//registry

    }
}
