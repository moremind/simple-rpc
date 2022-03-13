package cn.moremind.discovery;

import java.util.List;

/**
 * @author
 * @description
 * @see
 * @since
 */
public interface LoadBalanceStrategy {

    String selectHost(List<String> repos);
}
