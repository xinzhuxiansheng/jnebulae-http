package com.xinzhuxiansheng.jnebulae.http.core.router.context;

import com.xinzhuxiansheng.jnebulae.http.core.render.RenderType;
import com.xinzhuxiansheng.jnebulae.http.core.router.RouteResult;
import io.netty.handler.codec.http.HttpMethod;

/**
 * 路由上下文
 * @author houyi.wh
 * @date 2017-10-20
 */
public interface RouterContext {

    /**
     * 获取路由结果
     * @param method 请求类型
     * @param uri url
     * @return 路由结果
     */
    RouteResult<RenderType> getRouteResult(HttpMethod method, String uri);

}
