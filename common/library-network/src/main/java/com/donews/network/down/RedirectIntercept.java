package com.donews.network.down;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author by SnowDragon
 * Date on 2021/4/2
 * Description:
 */
public class RedirectIntercept implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        okhttp3.Request request = chain.request();
        Response response = chain.proceed(request);
        int code = response.code();
        Log.e("RedirectIntercept：", "location = " + code);
        if (code == 307) {
            //获取重定向的地址
            String location = response.headers().get("Location");
            Log.e("RedirectIntercept：", "location = " + location);
            //重新构建请求
            Request newRequest = request.newBuilder().url(location).build();

            response = chain.proceed(newRequest);

        }
        return response;
    }
}
