package cn.moremind.discovery;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class ServiceDiscoveryWithZk implements IServiceDiscovery {

    private CuratorFramework curatorFramework;

    List<String> serviceRepos = new ArrayList<>(); // 服务地址的本地缓存

    {
        // 初始化zookeeper链接
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(ZkConfig.CONNECTION_STR)
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .namespace("registry")
                .build();
        curatorFramework.start();
    }

    /**
     * 1.获取服务的查找
     * 2.设置监听
     * @param serviceName
     * @return
     */
    @Override
    public String discovery(String serviceName) {
        // 完成服务地址的查找(服务地址的被删除)
        String path = "/" + serviceName; // /registry/com.moremind.IHelloService-v1.0
        try {
            serviceRepos = curatorFramework.getChildren().forPath(path);
            registryWatch(path);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        // 针对已有的地址做负载均衡
        LoadBalanceStrategy loadBalanceStrategy = new RandomLoadBalance();
        return loadBalanceStrategy.selectHost(serviceRepos);
    }

    private void registryWatch(final String path) throws Exception {
        PathChildrenCache nodeCache=new PathChildrenCache(curatorFramework,path,true);
        PathChildrenCacheListener nodeCacheListener= (curatorFramework1, pathChildrenCacheEvent) -> {
            System.out.println("客户端收到节点变更的事件");
            serviceRepos=curatorFramework1.getChildren().forPath(path);// 再次更新本地的缓存地址
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        nodeCache.start();
    }
}
