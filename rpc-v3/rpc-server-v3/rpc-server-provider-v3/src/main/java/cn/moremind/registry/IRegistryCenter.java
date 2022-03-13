package cn.moremind.registry;

/**
 * @author
 * @description
 * @see
 * @since
 */
public interface IRegistryCenter {

    /**
     * 服务注册的服务名称和服务地址
     * @param serviceName
     * @param serviceAddress
     */
    void registry(String serviceName, String serviceAddress);
}
