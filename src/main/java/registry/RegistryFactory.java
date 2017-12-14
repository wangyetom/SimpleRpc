package registry;

import proxy.RegistryProxy;
import remoting.NettyClient;

/**
 * Created by wangye on 17/12/14.
 */
public class RegistryFactory {
    public static Registry getRegistry(ServerAddr registryAddr) {
        NettyClient nettyClient = new NettyClient();
        nettyClient.start();
        RegistryProxy registryProxy = new RegistryProxy(nettyClient);
        return registryProxy.createRpcProxy(Registry.class, registryAddr.getIp(), registryAddr.getPort());

    }
}
