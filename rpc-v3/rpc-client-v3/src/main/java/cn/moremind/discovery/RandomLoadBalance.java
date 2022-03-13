package cn.moremind.discovery;

import java.util.List;
import java.util.Random;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> repos) {
        int length = repos.size();
        Random random = new Random();
        return repos.get(random.nextInt(length));
    }
}
