package cn.moremind.registry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @author
 * @description
 * @see
 * @since
 */
public class RpcRegistry {
    private int port;

    public RpcRegistry(int port) {
        this.port = port;
    }

    public void start() {
        // 基于 NIO 实现
        // Selector 主线程，Work线程

        // 主线城池初始化
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 子线程初始化，具体对应客户端的处理逻辑
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 在netty中，把所有业务逻辑的处理全部归总到一个队列中
                            // 这个队列中包含了各种处理逻辑，对这些处理逻辑在netty中有一个封装
                            // 封装成一个对象，无锁化串行任务队列
                            // Pipeline
                            ChannelPipeline pipeline = ch.pipeline();

                            // 对处理逻辑的封装
                            //自定义协议解码器
                            /** 入参有5个，分别解释如下
                             maxFrameLength：框架的最大长度。如果帧的长度大于此值，则将抛出TooLongFrameException。
                             lengthFieldOffset：长度字段的偏移量：即对应的长度字段在整个消息数据中得位置
                             lengthFieldLength：长度字段的长度。如：长度字段是int型表示，那么这个值就是4（long型就是8）
                             lengthAdjustment：要添加到长度字段值的补偿值
                             initialBytesToStrip：从解码帧中去除的第一个字节数
                             */
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            //自定义协议编码器
                            pipeline.addLast(new LengthFieldPrepender(4));

                            // 这两个主要是还原出 private Class<?> [] params; private Object[] values;
                            // 反序列化成Java能够识别的语法对象
                            //对象参数类型解码器
                            pipeline.addLast("encoder",new ObjectEncoder());
                            //对象参数类型解码器
                            pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE,ClassResolvers.cacheDisabled(null)));

                            // 最后一步,执行自己的逻辑
                            // 1.注册，给每个对象起一个名字，对外提供服务的名字
                            // 2.服务的位置进行登记
                            pipeline.addLast(new RegistryHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // 启动服务
            ChannelFuture future = server.bind(this.port).sync();
            System.out.println("MoreMind Netty Registry start listen: " + this.port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new RpcRegistry(8080).start();
    }
}
