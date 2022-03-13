package cn.moremind;

import cn.moremind.discovery.IServiceDiscovery;
import cn.moremind.discovery.ServiceDiscoveryWithZk;

import java.lang.reflect.Proxy;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class RpcProxyClient {
    private IServiceDiscovery serviceDiscovery = new ServiceDiscoveryWithZk();

    //public RpcProxyClient(IServiceDiscovery serviceDiscovery) {
    //    this.serviceDiscovery = serviceDiscovery;
    //}

    public <T> T clientProxy(final Class<T> interfaceCls, String version) {
        return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(),
                new Class<?>[]{interfaceCls}, new RemoteInvocationHandler(serviceDiscovery, version));

    }
}
