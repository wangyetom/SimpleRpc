package registry;

import java.util.Collection;
import java.util.List;

/**
 * Created by wangye on 17/12/14.
 */
public interface Registry {
    public void register(String fullInterfaceName,ServerAddr serverAddr);
    public List<ServerAddr> find(String fullInterfaceName);
}
