package com.xinzhuxiansheng.jnebulae.http.core.server;

import com.redant.core.common.constants.CommonConstants;
import com.redant.core.handler.ControllerDispatcher;
import com.redant.core.handler.ssl.SslContextHelper;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLEngine;

/**
 * @author houyi.wh
 * @date 2019-01-17
 */
public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {

    public static final Logger LOGGER = LoggerFactory.getLogger(NettyHttpServerInitializer.class);

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        // HttpServerCodec is a combination of HttpRequestDecoder and HttpResponseEncoder
        // 使用HttpServerCodec将ByteBuf编解码为httpRequest/httpResponse
        pipeline.addLast(new HttpServerCodec());
        addAdvanced(pipeline);
        pipeline.addLast(new ChunkedWriteHandler());
        // 路由分发器
        pipeline.addLast(new ControllerDispatcher());
    }

    private void initSsl(SocketChannel ch){
        ChannelPipeline pipeline = ch.pipeline();
        if(CommonConstants.USE_SSL){
            SslContext context = SslContextHelper.getSslContext(CommonConstants.KEY_STORE_PATH,CommonConstants.KEY_STORE_PASSWORD);
            if(context!=null) {
                SSLEngine engine = context.newEngine(ch.alloc());
                engine.setUseClientMode(false);
                pipeline.addLast(new SslHandler(engine));
            }else{
                LOGGER.warn("SslContext is null with keyPath={}",CommonConstants.KEY_STORE_PATH);
            }
        }
    }

    /**
     * 可以在 HttpServerCodec 之后添加这些 ChannelHandler 进行开启高级特性
     */
    private void addAdvanced(ChannelPipeline pipeline){
        if(CommonConstants.USE_COMPRESS) {
            // 对 http 响应结果开启 gizp 压缩
            pipeline.addLast(new HttpContentCompressor());
        }
        if(CommonConstants.USE_AGGREGATOR) {
            // 将多个HttpRequest组合成一个FullHttpRequest
            pipeline.addLast(new HttpObjectAggregator(CommonConstants.MAX_CONTENT_LENGTH));
        }
    }

}