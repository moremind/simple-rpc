package cn.moremind;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 * @description
 * @see
 * @since
 */
@Configuration
@ComponentScan(basePackages = "cn.moremind")
public class SpringConfig {

    @Bean(name="mRpcServer")
    public MRpcServer mRpcServer(){
        return new MRpcServer(8080);
    }
}
