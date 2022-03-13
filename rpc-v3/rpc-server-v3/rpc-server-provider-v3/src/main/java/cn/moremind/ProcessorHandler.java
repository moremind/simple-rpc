package cn.moremind;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class ProcessorHandler implements Runnable{
    private Socket socket;

    private Map<String, Object> handlerMap;

    public ProcessorHandler(Socket socket, Map<String, Object> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;

        try {
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            // 请求那个类
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = invoke(rpcRequest);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(result);
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            if (objectInputStream != null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object invoke(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String serviceName = rpcRequest.getClassName();

        String version = rpcRequest.getVersion();

        // 增加版本号
        if (!StringUtils.isEmpty(version)) {
            serviceName += "-" + version;
        }

        Object service = handlerMap.get(serviceName);
        if (service == null) {
            throw new RuntimeException("Service not found" + serviceName);
        }

        // 反射调用
        Object[] args = rpcRequest.getParameters(); // 拿到客户端的参数
        Method method=null;
        if (args != null) {
            Class<?>[] types = new Class[args.length]; // 获得每个参数的类型
            for (int i = 0; i < types.length; i++) {
                types[i] = args[i].getClass();
            }
            Class clazz=Class.forName(rpcRequest.getClassName()); //跟去请求的类进行加载
            method=clazz.getMethod(rpcRequest.getMethodName(), types); //sayHello, saveUser找到这个类中的方法
        } else {
            Class clazz=Class.forName(rpcRequest.getClassName()); //跟去请求的类进行加载
            method=clazz.getMethod(rpcRequest.getMethodName()); //sayHello, saveUser找到这个类中的方法
        }
        Object result = method.invoke(service, args);
        return result;
    }
}
