package com.donews.middle.abswitch;

import com.donews.common.BuildConfig;
import com.donews.middle.bean.globle.ABBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.SPUtils;

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
            if (!SPUtils.getInformain("Is_Open_AB", true)) {
                mAbBean.setOpenAB(false);
            } else {
                mAbBean.setOpenAB(true);
            }
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

    public int isOpenHomeGuid() {
        checkABBean();
        return mAbBean.getOpenHomeGuid();
    }

    public void initAbSwitch() {
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(BuildConfig.BASE_CONFIG_URL + "plus-abswitch" + BuildConfig.BASE_RULE_URL, true))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<ABBean>() {

                    @Override
                    public void onError(ApiException e) {
                        if (!SPUtils.getInformain("Is_Open_AB", true)) {
                            mAbBean.setOpenAB(false);
                        } else {
                            mAbBean.setOpenAB(true);
                        }
                    }

                    @Override
                    public void onSuccess(ABBean abBean) {
                        mAbBean = abBean;
                        GoodsCache.saveGoodsBean(abBean, "abswitch");
                        if (!mAbBean.isOpenAB()) {
                            SPUtils.setInformain("Is_Open_AB", false);
                        } else {
                            if (!SPUtils.getInformain("Is_Open_AB", true)) {
                                mAbBean.setOpenAB(false);
                            }
                        }
                    }
                });
    }
}
