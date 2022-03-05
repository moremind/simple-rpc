package cn.moremind;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author
 * @description
 * @see
 * @since
 */
@Target(ElementType.TYPE) //类或接口
@Retention(RetentionPolicy.RUNTIME)
@Component //被Spring扫描
public @interface RpcService {
    Class<?> value(); //拿到服务的接口

    String version() default "";
}
