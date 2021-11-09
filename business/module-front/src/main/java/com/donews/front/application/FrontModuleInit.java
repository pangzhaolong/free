package com.donews.front.application;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.GsonUtils;
import com.donews.common.IModuleInit;
import com.donews.front.BuildConfig;
import com.donews.front.api.FrontApi;
import com.donews.front.bean.CommandBean;
import com.donews.front.bean.WithdraWalletResp;
import com.donews.front.bean.WithdrawConfigResp;
import com.donews.front.cache.GoodsCache;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;

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

        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(FrontApi.commandUrl, true))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<CommandBean>() {

                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(CommandBean commandBean) {
                        ClipboardManager clipboardManager = (ClipboardManager) application.getSystemService(
                                Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText(null, commandBean.getCommand());
                        clipboardManager.setPrimaryClip(clipData);
                    }
                });

        requestWithdraWallet();
        requestWithdrawCenterConfig();
        return false;
    }

    /**
     * 获取钱包详情数据。总额等
     *
     * @return
     */
    public void requestWithdraWallet() {
        EasyHttp.get(BuildConfig.API_WALLET_URL + "v1/wallet")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<WithdraWalletResp>() {
                    @Override
                    public void onError(ApiException e) {
                    }

                    @Override
                    public void onSuccess(WithdraWalletResp queryBean) {
                        SPUtils.getInstance().put("withdraw_detail", GsonUtils.toJson(queryBean));
                    }
                });
    }

    /**
     * 获取提现中心的配置
     *
     * @return
     */
    public void requestWithdrawCenterConfig() {
        EasyHttp.get(BuildConfig.API_WALLET_URL + "v1/withdraw/config")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<WithdrawConfigResp>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(WithdrawConfigResp queryBean) {
                        SPUtils.getInstance().put("withdraw_config", GsonUtils.toJson(queryBean));
                    }
                });
    }


    @Override
    public boolean onInitLow(BaseApplication application) {
        return false;
    }
}
