package registry;

import annotation.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangye on 17/12/14.
 */

public class InMemoryRegistry implements Registry {
    private Map<String, List<ServerAddr>> registry = new HashMap<String, List<ServerAddr>>();

    @Override
    public synchronized void register(String fullInterfaceName, ServerAddr serverAddr) {

        if (registry.get(fullInterfaceName) == null) {

            registry.put(fullInterfaceName, new ArrayList<ServerAddr>());

        }
        if (!registry.get(fullInterfaceName).contains(serverAddr))
            registry.get(fullInterfaceName).add(serverAddr);
    }

    @Override
    public synchronized List<ServerAddr> find(String fullInterfaceName) {
        return registry.get(fullInterfaceName);
    }
}
