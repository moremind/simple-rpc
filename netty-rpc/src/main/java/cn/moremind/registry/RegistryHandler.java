package cn.moremind.registry;

import cn.moremind.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class RegistryHandler  extends ChannelInboundHandlerAdapter {

    // 1.根据一个包名讲所有的服务条件的class全部扫描出来，放到一个容器中
    // 如果是分布式就是读配置文件
    // 2.给没一个对应的Class起一个唯一的名字，作为服务名称，保存到一个容器中
    // 3.当有客户端链接过来之后，就会获取协议内容

    // 4.要去注册好的容器中找到符合条件的服务
    // 5.通过远程调用Provider得到返回结果，并回复给客户端

    private List<String> classNames = new ArrayList<>();
    public static ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap<>();


    public RegistryHandler() {
        // 描述
        scannerClass("cn.moremind.provider");

        // 注册
        doRegister();
    }

    // 有客户端连上的时候进行回调
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        InvokerProtocol request = (InvokerProtocol)msg;

        //当客户端建立连接时，需要从自定义协议中获取信息，拿到具体的服务和实参
        //使用反射调用
        if(registryMap.containsKey(request.getClassName())){
            Object service = registryMap.get(request.getClassName());
            Method method = service.getClass().getMethod(request.getMethodName(), request.getParames());
            result = method.invoke(service, request.getValues());
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }

    // 连接发生异常时进行回调
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void doRegister() {
        if(classNames.size() == 0){ return; }
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                Class<?> i = clazz.getInterfaces()[0];
                // 本来这里应该存一个网络的路径。从配置文件中读取
                // 在调用的时候进行解析
                registryMap.put(i.getName(), clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 正常的话是读配置文件
    private void scannerClass(String packageName) {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            //如果是一个文件夹，继续递归
            if(file.isDirectory()){
                scannerClass(packageName + "." + file.getName());
            }else{
                classNames.add(packageName + "." + file.getName().replace(".class", "").trim());
            }
        }
    }
}
