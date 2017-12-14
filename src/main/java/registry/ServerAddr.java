package registry;

import java.io.Serializable;

/**
 * Created by wangye on 17/12/14.
 */
public class ServerAddr  implements Serializable {
    private final String ip;
    private final int port;

    public String getIp() {
        return ip;
    }



    public int getPort() {
        return port;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerAddr that = (ServerAddr) o;

        if (port != that.port) return false;
        if (!ip.equals(that.ip)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + port;
        return result;
    }

    public ServerAddr(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
}
