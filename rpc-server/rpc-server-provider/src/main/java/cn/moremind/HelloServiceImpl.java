package cn.moremind;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class HelloServiceImpl implements IHelloService{
    @Override
    public String sayHello(String content) {
        System.out.println("Request in: " + content);
        return "Say Hello";
    }

    @Override
    public String saveUser(User user) {
        System.out.println("Request in: " + user);
        return "SUCCESS";
    }
}
