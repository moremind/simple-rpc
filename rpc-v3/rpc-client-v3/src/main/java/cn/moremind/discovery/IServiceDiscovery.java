package cn.moremind.discovery;

/**
 * @author
 * @description
 * @see
 * @since
 */
public interface IServiceDiscovery {

    /**
     * 根据服务名称返回服务地址
     * @param serviceName
     * @return
     */
    String discovery(String serviceName);
}
