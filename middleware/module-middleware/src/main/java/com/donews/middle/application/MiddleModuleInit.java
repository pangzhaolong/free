package com.donews.middle.application;

import android.app.Application;

import com.blankj.utilcode.util.SPUtils;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.GsonUtils;
import com.donews.common.IModuleInit;
import com.donews.middle.BuildConfig;
import com.donews.middle.centralDeploy.ABSwitch;
import com.donews.middle.adutils.adcontrol.AdControlManager;
import com.donews.middle.bean.CriticalNumberBean;
import com.donews.middle.bean.WithdraWalletResp;
import com.donews.middle.bean.WithdrawConfigResp;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.command.CommandManager;
import com.donews.middle.request.RequestUtil;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DateManager;

import java.util.HashMap;
import java.util.Map;

/**
 * 应用模块: middle
 * <p>
 * 类描述: middle组件的业务初始化
 * <p>
 *
 * @author dw
 * @since 2021-11-10
 */
public class MiddleModuleInit implements IModuleInit {


    @Override
    public boolean onInitAhead(BaseApplication application) {
        GoodsCache.init(application);
        // 获取A/B开关
        ABSwitch.Ins().init();
        AdControlManager.INSTANCE.init();
        requestCommand(application);
        requestWithdraWallet();
        requestWithdrawCenterConfig();
        RequestUtil.requestHighValueGoodsInfo();
        //同步抽奖暴击次数
        if (AppInfo.checkIsWXLogin()) {
            requestCriticalWallet(null,"false");
        }
        return false;
    }


    /**
     * 获取口令
     */

    private void requestCommand(Application application) {
        CommandManager.Ins().init(application);
    }

    /**
     * 获取钱包详情数据。总额等
     *
     * @return
     */
    public void requestWithdraWallet() {
        EasyHttp.get(BuildConfig.API_WALLET_URL + "v1/wallet")
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(false)
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
     * 获取用户暴击次数
     *
     * @return participate 是否参与了暴击 true服务器会加一
     */
    public static void requestCriticalWallet(ICriticalWalletListener iCriticalWalletListener,String participate) {
        Map<String, String> params = new HashMap<>();
        params.put("start", participate);
        EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/bliz-lottery-times")
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .isShowToast(false)
                .execute(new SimpleCallBack<CriticalNumberBean>() {
                    @Override
                    public void onError(ApiException e) {
                        if (iCriticalWalletListener != null) {
                            iCriticalWalletListener.onError();
                        }
                    }

                    @Override
                    public void onSuccess(CriticalNumberBean numberBean) {
                        if (numberBean != null) {
//                            ToastUtil.showShort(BaseApplication.getInstance(), "暴击当前次数" + numberBean.getUseTimes() + "总次数" + numberBean.getTotalTimes());
                            //同步暴击的总次数
                            SPUtils.getInstance().put(DateManager.LOTTERY_SUN_NUMBER, numberBean.getTotalTimes());
                            //同步当前暴击的次数
                            SPUtils.getInstance().put(DateManager.LOTTERY_JD, numberBean.getUseTimes());
                            if (iCriticalWalletListener != null) {
                                iCriticalWalletListener.onSuccess(numberBean);
                            }
                        } else {
                            if (iCriticalWalletListener != null) {
                                iCriticalWalletListener.onError();
                            }
                        }
                    }
                });
    }


    /**
     * 获取提现中心的配置
     *
     * @return
     */
    private void requestWithdrawCenterConfig() {
        EasyHttp.get(BuildConfig.API_WALLET_URL + "v1/withdraw/config")
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(false)
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


    public  interface ICriticalWalletListener {


        public void onError();

        public void onSuccess(CriticalNumberBean numberBean);
    }


}
