package loadbalance;

import registry.Registry;
import registry.ServerAddr;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wangye on 17/12/14.
 */
public class RandRobinLoadBalance implements LoadBalance {
    Registry registry;
    ConcurrentMap<String, AtomicInteger> serverIndex = new ConcurrentHashMap<String, AtomicInteger>();

    public RandRobinLoadBalance(Registry registry) {
        this.registry = registry;
    }

    @Override
    public ServerAddr findServer(String fullInterfaceName) {
        List<ServerAddr> serverAddrs = registry.find(fullInterfaceName);
        if (serverAddrs != null && serverAddrs.size() > 0) {
            AtomicInteger index = serverIndex.putIfAbsent(fullInterfaceName, new AtomicInteger());
            if (index == null) {
                index = new AtomicInteger();
            }
            return serverAddrs.get(index.getAndIncrement() % serverAddrs.size());
        } else {
            return null;
        }
    }
}
