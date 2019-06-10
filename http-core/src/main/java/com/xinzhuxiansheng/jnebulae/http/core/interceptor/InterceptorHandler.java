package com.xinzhuxiansheng.jnebulae.http.core.interceptor;

import com.xinzhuxiansheng.common.utils.CollUtil;

import java.util.List;
import java.util.Map;

/**
 * @author houyi.wh
 * @date 2017/11/15
 **/
public class InterceptorHandler {

    public static boolean preHandle(Map<String, List<String>> paramMap){
        List<Interceptor> interceptors = InterceptorProvider.getInterceptors();
        if(CollUtil.isEmpty(interceptors)){
            return true;
        }
        for(Interceptor interceptor : interceptors){
            if(!interceptor.preHandle(paramMap)){
                return false;
            }
        }
        return true;
    }

    public static void postHandle(Map<String, List<String>> paramMap){
        List<Interceptor> interceptors = InterceptorProvider.getInterceptors();
        if(CollUtil.isEmpty(interceptors)){
            return;
        }
        for(Interceptor interceptor : interceptors){
            interceptor.postHandle(paramMap);
        }
    }




}
