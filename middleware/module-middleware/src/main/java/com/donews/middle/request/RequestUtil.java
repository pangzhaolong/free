package com.donews.middle.request;


import com.donews.middle.BuildConfig;
import com.donews.middle.bean.HighValueGoodsBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;

public class RequestUtil {

    /**
     * 获取高价值商品列表
     *
     * @return
     */
    public static void requestHighValueGoodsInfo() {
        EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/recommend-high-value-goods?limit=4")
                .cacheMode(CacheMode.CACHEANDREMOTEDISTINCT)
                .cacheKey("high_value_goods_1")
                .execute(new SimpleCallBack<HighValueGoodsBean>() {
                    @Override
                    public void onError(ApiException e) {

                    }

                    @Override
                    public void onSuccess(HighValueGoodsBean highValueGoodsBean) {
                        if (highValueGoodsBean == null) {
                            return;
                        }

                        GoodsCache.saveGoodsBean(highValueGoodsBean, "exit");
                    }
                });
    }

}
