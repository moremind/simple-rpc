package cn.moremind.consumer;

import cn.moremind.api.IRpcHelloService;
import cn.moremind.api.IRpcService;
import cn.moremind.consumer.proxy.RpcProxy;
import cn.moremind.provider.RpcHelloServiceImpl;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class RpcConsumer {

    public static void main(String[] args) {
        IRpcHelloService rpcHello = RpcProxy.create(IRpcHelloService.class);

        System.out.println(rpcHello.hello("Tom老师"));

        IRpcService service = RpcProxy.create(IRpcService.class);

        System.out.println("8 + 2 = " + service.add(8, 2));
        System.out.println("8 - 2 = " + service.sub(8, 2));
        System.out.println("8 * 2 = " + service.mult(8, 2));
        System.out.println("8 / 2 = " + service.div(8, 2));
    }
}
