package com.donews.middle.abswitch;

import com.donews.common.BuildConfig;
import com.donews.middle.bean.globle.ABBean;
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

    public ABBean getABBean() {
        if (mAbBean == null) {
            mAbBean = new ABBean();
            mAbBean.setAb(true);
        }
        return mAbBean;
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
                    }
                });
    }
}
