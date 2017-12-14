package loadbalance;

import registry.ServerAddr;

/**
 * Created by wangye on 17/12/14.
 */
public interface LoadBalance {
    ServerAddr findServer(String fullInterfaceName);
}
