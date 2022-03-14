package com.dn.drouter.interceptor;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.dn.drouter.helper.MainLooper;

/**
 * @author by SnowDragon
 * Date on 2020/11/17
 * Description:
 */
@Interceptor(priority = 9)
public class DInterceptor implements IInterceptor {
    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {


        switch (postcard.getPath()) {
            case InterceptorRouter.USER_MAIN_ACTIVITY:
                // 异步逻辑处理


                //主线程处理
                MainLooper.runOnUiThread(() -> {

                });

                break;
            default:
                callback.onContinue(postcard);
        }

    }

    @Override
    public void init(Context context) {

    }
}
