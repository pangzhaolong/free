/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.donews.network.interceptor;


import com.blankj.utilcode.util.AppUtils;
import com.donews.network.model.HttpHeaders;
import com.donews.network.utils.HttpLog;
import com.donews.utilslibrary.utils.AppInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>描述：登录头部拦截,如果未登录则去掉token。只针对部分URL生效 </p>
 * 作者： created by lcl<br/>
 * 需要检查的URL地址：
 * https://lottery.
 */
public class LoginHeaderInterceptor implements Interceptor {

    //需要拦截的URLs
    private static List<String> interceptorUrls = new ArrayList<String>() {
        {
            add("https://lottery.");
        }
    };

    private HttpHeaders headers;

    public LoginHeaderInterceptor(HttpHeaders headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (headers.headersMap.isEmpty()) return chain.proceed(builder.build());
        try {
            if (!AppInfo.checkIsWXLogin() && checkIsInterceptUrl(chain.request().url().toString())) {
                builder.header(HttpHeaders.HEAD_AUTHORIZATION, "");
            }
        } catch (Exception e) {
            HttpLog.e(e);
        }
        return chain.proceed(builder.build());
    }

    //是否需要拦截的URL
    private boolean checkIsInterceptUrl(String reqUrl) {
        for (String interceptorUrl : interceptorUrls) {
            if (reqUrl.startsWith(interceptorUrl)) {
                return true;
            }
        }
        return false;
    }
}
