package cn.moremind;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World");
        RpcProxyClient rpcProxyClient = new RpcProxyClient();
        IHelloService iHelloService = rpcProxyClient.clientProxy
                (IHelloService.class, "localhost", 8080);
        String result = iHelloService.sayHello("MoreMind");
        System.out.println(result);


    }
}
