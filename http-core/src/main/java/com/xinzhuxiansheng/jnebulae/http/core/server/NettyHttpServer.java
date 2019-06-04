package com.xinzhuxiansheng.jnebulae.http.core.server;

import com.xinzhuxiansheng.jnebulae.http.core.common.constants.CommonConstants;
import com.xinzhuxiansheng.jnebulae.http.core.init.InitExecutor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * NettyHttpServer
 * @author houyi.wh
 * @date 2017-10-20
 */
public final class NettyHttpServer implements Server {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyHttpServer.class);

    @Override
    public void preStart() {
        InitExecutor.init();
    }

    @Override
    public void start() {
        //主从Reactor多线程模型
        EventLoopGroup bossGroup = new NioEventLoopGroup(CommonConstants.BOSS_GROUP_SIZE, new DefaultThreadFactory("boss", true));
        EventLoopGroup workerGroup = new NioEventLoopGroup(CommonConstants.WORKER_GROUP_SIZE, new DefaultThreadFactory("worker", true));
        try {
            long start = System.currentTimeMillis();
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.group(bossGroup, workerGroup) //
             .channel(NioServerSocketChannel.class)   //设置NIO的双向通道
//             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new NettyHttpServerInitializer()); //配置子处理器，用于处理workdGroup的任务
            //每一个channel由多个handler共同组成管道(pipeline)

            //绑定端口，同步等待成功
            ChannelFuture future = b.bind(CommonConstants.SERVER_PORT).sync();
            long cost = System.currentTimeMillis()-start;
            LOGGER.info("[NettyHttpServer] Startup at port:{} cost:{}[ms]",CommonConstants.SERVER_PORT,cost);

            // 等待服务端Socket关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error("[NettyHttpServer] InterruptedException:",e);
        } finally {
            //优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}