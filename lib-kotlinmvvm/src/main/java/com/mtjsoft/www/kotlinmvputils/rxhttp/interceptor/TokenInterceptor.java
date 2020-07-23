package com.mtjsoft.www.kotlinmvputils.rxhttp.interceptor;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rxhttp.wrapper.param.RxHttp;
import rxhttp.wrapper.parse.SimpleParser;

/**
 * token 失效，自动刷新token，然后再次发送请求，用户无感知
 */
public class TokenInterceptor implements Interceptor {

    //token刷新时间
    private static volatile long SESSION_KEY_REFRESH_TIME = 0;

    private volatile String newToken = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        // 添加一个请求时间
        Request request = chain.request().newBuilder().addHeader("request_time", String.valueOf(System.currentTimeMillis())).build();
        Response originalResponse = chain.proceed(request);
        int code = originalResponse.code();
        if (code == 401) { //token 失效  1、这里根据自己的业务需求写判断条件
            return handleTokenInvalid(chain, request);
        }
        return originalResponse;
    }


    //处理token失效问题
    private Response handleTokenInvalid(Chain chain, Request request) throws IOException {
        // 获取请求发出的时间
        String requestTime = request.header("request_time");  //3、发请求前需要addheader("request_time",System.currentTimeMillis())
        // 同步刷新token
        boolean success = refreshToken(requestTime);
        Request newRequest;
        if (success) { //刷新成功，重新签名
            //使用新的Token，创建新的请求
            newRequest = chain.request()
                    .newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer " + newToken)
                    .build();
        } else {
            newRequest = request;
        }
        return chain.proceed(newRequest);
    }

    // 同步刷新token，加锁，保证不多次刷新token
    private boolean refreshToken(Object value) {
        long requestTime = 0;
        try {
            requestTime = Integer.parseInt(value.toString());
        } catch (Exception ignore) {
        }
        //请求时间小于token刷新时间，说明token已经刷新，则无需再次刷新
        if (requestTime <= SESSION_KEY_REFRESH_TIME) {
            return true;
        }
        synchronized (this) {
            //再次判断是否已经刷新
            if (requestTime <= SESSION_KEY_REFRESH_TIME) {
                return true;
            }
            try {
                //获取到最新的token，这里需要同步请求token,千万不能异步  5、根据自己的业务修改
                newToken = RxHttp.postForm("/refreshToken/...")
                        .execute(SimpleParser.get(String.class));
                SESSION_KEY_REFRESH_TIME = System.currentTimeMillis();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
}
