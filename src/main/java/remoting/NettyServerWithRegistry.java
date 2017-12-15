package remoting;

import annotation.Service;
import org.reflections.Reflections;
import registry.Registry;
import registry.RegistryFactory;
import registry.ServerAddr;

import java.lang.annotation.Annotation;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by wangye on 17/12/7.
 */
public class NettyServerWithRegistry extends NettyServer {

    private Registry registry;
    private ServerAddr serverAddr;

    //需要注册的就填第二个参数
    public NettyServerWithRegistry(int port, ServerAddr registryAddr) {
        super(port);

        try {
            serverAddr = new ServerAddr(InetAddress.getLocalHost().getHostAddress(), port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        if (registryAddr != null) {
            registry = RegistryFactory.getRegistry(registryAddr,new NettyClient());
        }
        registerServices();
    }

    private void registerServices() {
        Reflections ref = new Reflections("");
        for (Class<?> clazz : ref.getTypesAnnotatedWith(Service.class)) {


                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> inter : interfaces)
                        registry.register(inter.getCanonicalName(), serverAddr);

        }

    }


}
