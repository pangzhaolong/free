package com.donews.front.application;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.donews.base.base.BaseApplication;
import com.donews.common.IModuleInit;
import com.donews.front.api.FrontApi;
import com.donews.front.bean.CommandBean;
import com.donews.front.cache.GoodsCache;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;

/**
 * 应用模块: main
 * <p>
 * 类描述: main组件的业务初始化
 * <p>
 *
 * @author darryrzhoong
 * @since 2020-02-26
 */
public class FrontModuleInit implements IModuleInit {


    @Override
    public boolean onInitAhead(BaseApplication application) {
        GoodsCache.init(application);
        ClipboardManager clipboardManager = (ClipboardManager) application.getSystemService(Context.CLIPBOARD_SERVICE);

        EasyHttp.get(FrontApi.commandUrl)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<CommandBean>() {

                    @Override
                    public void onError(ApiException e) {
//                        LogUtil.e(e.getMessage());
                    }

                    @Override
                    public void onSuccess(CommandBean commandBean) {
//                        LogUtil.e("xxxx:" + commandBean.toString());
                        ClipData clipData = ClipData.newPlainText(null, commandBean.getCommand());
                        clipboardManager.setPrimaryClip(clipData);
                    }
                });
        return false;
    }

    @Override
    public boolean onInitLow(BaseApplication application) {
        return false;
    }
}
