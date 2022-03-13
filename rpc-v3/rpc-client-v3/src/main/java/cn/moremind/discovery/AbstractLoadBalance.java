package cn.moremind.discovery;

import java.util.List;

/**
 * @author
 * @description
 * @see
 * @since
 */
public abstract class AbstractLoadBalance implements LoadBalanceStrategy{
    @Override
    public String selectHost(List<String> repos) {
        if (repos == null || repos.size() == 0) {
            return null;
        }
        if (repos.size() == 1) {
            return repos.get(0);
        }
        return doSelect(repos);
    }

    protected abstract String doSelect(final List<String> repos);
}
