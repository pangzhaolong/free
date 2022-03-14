package com.donews.network.interceptor;

import com.donews.network.exception.ApiException;
import com.donews.network.utils.HttpLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/12 16:04<br>
 * 版本：V1.0<br>
 */
public class HttpExprInterceptor extends BaseExpiredInterceptor {

    private int code;
    private String message = "";

    @Override
    public boolean isResponseExpired(Response response, String bodyString) {
        HttpLog.d(bodyString);
        try {
            JSONObject jsonObject = new JSONObject(bodyString);
            code = jsonObject.optInt("code");
            message = jsonObject.optString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code != ApiException.OK;
    }

    @Override
    public Response responseExpired(Chain chain, String bodyString) {

        HttpLog.i(Thread.currentThread().getName());
        try {
            return chain.proceed(chain.request());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
