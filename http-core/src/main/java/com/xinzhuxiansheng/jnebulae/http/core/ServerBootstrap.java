package com.xinzhuxiansheng.jnebulae.http.core;

import com.xinzhuxiansheng.jnebulae.http.core.server.NettyHttpServer;
import com.xinzhuxiansheng.jnebulae.http.core.server.Server;


public final class ServerBootstrap {

    public static void main(String[] args) {
        //启动HTTP
        Server nettyServer = new NettyHttpServer();
        // 各种初始化工作
        nettyServer.preStart();
        // 启动服务器
        nettyServer.start();
    }

}
