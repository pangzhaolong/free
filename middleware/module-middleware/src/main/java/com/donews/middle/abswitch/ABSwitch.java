package com.donews.middle.abswitch;

import com.donews.common.BuildConfig;
import com.donews.middle.bean.globle.ABBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;

public class ABSwitch {
    private ABBean mAbBean;

    private static ABSwitch sAbSwitch;

    public static ABSwitch Ins() {
        if (sAbSwitch == null) {
            sAbSwitch = new ABSwitch();
        }
        return sAbSwitch;
    }

    private void checkABBean() {
        if (mAbBean == null) {
            mAbBean = new ABBean();
            mAbBean.setAb(true);
        }
    }

    public boolean isOpenAB() {
        checkABBean();
        return mAbBean.isOpenAB();
    }

    public boolean isOpenVideoToast() {
        checkABBean();
        return mAbBean.isOpenVideoToast();
    }

    public boolean isOpenAutoLottery() {
        checkABBean();
        return mAbBean.isOpenAutoLottery();
    }

    public void initAbSwitch() {
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(BuildConfig.BASE_CONFIG_URL + "plus-abswitch" + BuildConfig.BASE_RULE_URL, true))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<ABBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mAbBean.setAb(true);
                    }

                    @Override
                    public void onSuccess(ABBean abBean) {
                        mAbBean = abBean;
                        GoodsCache.saveGoodsBean(abBean, "abswitch");
                    }
                });
    }
}
