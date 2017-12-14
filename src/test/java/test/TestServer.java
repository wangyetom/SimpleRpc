package test;

import remoting.NettyServer;

/**
 * Created by wangye on 17/12/11.
 */
public class TestServer {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer(3462);
    }
}
