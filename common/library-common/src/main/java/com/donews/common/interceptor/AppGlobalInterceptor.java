package com.donews.common.interceptor;

import android.util.Log;

import com.donews.common.AppGlobalConfigManager;
import com.donews.common.bean.AppGlobalConfigBean;
import com.donews.network.model.ApiResult;
import com.donews.network.utils.HttpLog;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

import static com.donews.network.utils.HttpUtil.UTF8;

/**
 * @author by SnowDragon
 * Date on 2021/4/9
 * Description:
 */
public class AppGlobalInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        try {
            ResponseBody responseBody = response.body();
            BufferedSource source = responseBody.source();
            // Buffer the entire body.
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            Gson gson = new Gson();
            if (charset != null) {
                String bodyString = buffer.clone().readString(charset);
                //判断是否可以解析为AppGlobalConfigBean 对象
                ApiResult<AppGlobalConfigBean> bean = gson.fromJson(bodyString, ApiResult.class);

                if (bean != null && bean.getCode() == 0 && bean.getData() != null) {
                    SPUtils.setInformain(KeySharePreferences.APP_GLOBAL_CRASH_CONFIG, gson.toJson(bean.getData()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}
