package com.donews.middle.searchs;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.donews.common.BuildConfig;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.middle.bean.globle.ABBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcl
 * Date on 2022/5/13
 * Description:
 */
public class TopSearchConfig {

    // 热门搜索数据
    private static TopSearchBean topScreshBean = null;

    private static final int UPDATE_CONFIG_MSG = 11005;

    private static final class Holder {
        private static final TopSearchConfig s_abSwitchMgr = new TopSearchConfig();
    }

    private static final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_CONFIG_MSG) {
                TopSearchConfig.update();
            }
        }
    };

    public static TopSearchConfig Ins() {
        return TopSearchConfig.Holder.s_abSwitchMgr;
    }

    /**
     * 获取热门搜索数据集合
     *
     * @return
     */
    public List<String> getTopSearchList() {
        if (topScreshBean == null) {
            return new ArrayList<>();
        }
        return topScreshBean.items;
    }

    /**
     * 更新数据
     */
    public static void update() {
        String json = SPUtils.getInstance(TopSearchConfig.class.getSimpleName())
                .getString("topSearch", "");
        try {
            topScreshBean = GsonUtils.fromJson(json, TopSearchBean.class);
        } catch (Exception e) {
        }
        LogUtil.e("热门搜索数据 update");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(BuildConfig.BASE_CONFIG_URL + BuildConfig.APP_IDENTIFICATION + "-" + "top-search"
                + BuildConfig.BASE_RULE_URL, true))
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<TopSearchBean>() {
                    @Override
                    public void onError(ApiException e) {
                        if (mHandler != null) {
                            if (mHandler.hasMessages(UPDATE_CONFIG_MSG)) {
                                mHandler.removeMessages(UPDATE_CONFIG_MSG);
                            }
                            mHandler.sendEmptyMessageDelayed(UPDATE_CONFIG_MSG, 20 * 1000);
                        }
                    }

                    @Override
                    public void onSuccess(TopSearchBean abBean) {
                        TopSearchConfig.topScreshBean = abBean;
                        saveCache(abBean);
                    }
                });
    }

    //缓存数据
    private static void saveCache(TopSearchBean abBean) {
        SPUtils.getInstance(TopSearchConfig.class.getSimpleName())
                .put("topSearch", GsonUtils.toJson(abBean));
    }
}
