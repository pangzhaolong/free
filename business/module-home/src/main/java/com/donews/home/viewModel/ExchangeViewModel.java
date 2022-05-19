package com.donews.home.viewModel;

import static com.donews.utilslibrary.utils.KeySharePreferences.TIME_SERVICE;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.home.api.HomeApi;
import com.donews.home.model.ExchangeModel;
import com.donews.home.model.HomeModel;
import com.donews.middle.BuildConfig;
import com.donews.middle.bean.home.FactorySaleBean;
import com.donews.middle.bean.home.HomeCategory2Bean;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.bean.home.HomeCoinCritConfigBean;
import com.donews.middle.bean.home.HomeEarnCoinReq;
import com.donews.middle.bean.home.HomeEarnCoinResp;
import com.donews.middle.bean.home.HomeReceiveGiftReq;
import com.donews.middle.bean.home.HomeReceiveGiftResp;
import com.donews.middle.bean.home.RealTimeBean;
import com.donews.middle.bean.home.SecKilBean;
import com.donews.middle.bean.home.TopIconsBean;
import com.donews.middle.bean.home.UserBean;
import com.donews.middle.mainShare.bean.Ex;
import com.donews.middle.viewmodel.BaseMiddleViewModel;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.HttpConfigUtilsKt;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p> </p>
 * 作者： created by dw<br>
 * 日期： 2020/12/7 10:59<br>
 * 版本：V1.0<br>
 */
public class ExchangeViewModel extends BaseLiveDataViewModel<ExchangeModel> {

    @Override
    public ExchangeModel createModel() {
        return new ExchangeModel();
    }

    //宝箱倒计时是否开始计时
    public MutableLiveData<Boolean> giftCountdownIsStartCount = new MutableLiveData<>();
    //宝箱的倒计时的数值
    public MutableLiveData<Integer> giftCountdownCount = new MutableLiveData<>(0);

    /**
     * 查询宝箱剩余的任务次数
     *
     * @return
     */
    public int queryReceiveGiftCount() {
        try {
            String json = SPUtils.getInstance(this.getClass().getSimpleName())
                    .getString("data", "");
            JSONObject jo = new JSONObject(json);
            //得到保存此数据对应的日期时间(毫秒)
            long time = jo.getLong("time") * 1000;
            //比较计算时间是否为同一天
            if (getGiftIsDay(time)) {
                return jo.getInt("rest_times");
            }
            return 5;
        } catch (JSONException e) {
            return 5;//返回默认值5次
        }
    }

    /**
     * 查询宝箱倒计时时长
     *
     * @return 需要倒计时的秒数，0：不需要计时，>0需要倒计时的秒数
     */
    public int queryGiftCountdownStep() {
        long curLocalTime = System.currentTimeMillis() / 1000;
        long fastSaveTime = SPUtils.getInstance(this.getClass().getSimpleName())
                .getLong("giftCountdownTime", curLocalTime / 1000);
        if (fastSaveTime > curLocalTime) {
            return 0;// 无效时间
        }
        if (curLocalTime - fastSaveTime > 2 * 60) {
            return 0; //超过两分钟了。那么不在计时
        }
        // 返回需要计时的差值
        return (int) (curLocalTime - fastSaveTime);
    }

    /**
     * 领取礼盒
     *
     * @return
     */
    public MutableLiveData<HomeReceiveGiftResp> getReceiveGift(HomeReceiveGiftReq req) {
        SPUtils.getInstance(this.getClass().getSimpleName())
                .put("giftCountdownTime", System.currentTimeMillis());
        giftCountdownIsStartCount.postValue(true);
        MutableLiveData<HomeReceiveGiftResp> mutableLiveData = new MutableLiveData<>();
        String url = (BuildConfig.BASE_QBN_API + "exchange/v1/receive-gift");
        EasyHttp.post(url)
                .upObject(req)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<HomeReceiveGiftResp>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeReceiveGiftResp homeBean) {
                        mutableLiveData.postValue(homeBean);
                        //更新个人中心部分内容
                        if (BaseMiddleViewModel.getBaseViewModel().mine2JBCount.getValue() != null) {
                            BaseMiddleViewModel.getBaseViewModel().mine2JBCount.postValue(
                                    BaseMiddleViewModel.getBaseViewModel().mine2JBCount.getValue() + homeBean.coin);
                        } else {
                            BaseMiddleViewModel.getBaseViewModel().mine2JBCount.postValue(homeBean.coin);
                        }
                        //如果成功。记录本地
                        try {
                            JSONObject jo = new JSONObject(GsonUtils.toJson(homeBean));
                            jo.put("time", getCurrentServiceTime());
                            SPUtils.getInstance(this.getClass().getSimpleName())
                                    .put("data", jo.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return mutableLiveData;
    }

    /**
     * 赚金币的接口
     *
     * @return
     */
    public MutableLiveData<HomeEarnCoinResp> getEarnCoin(HomeEarnCoinReq req) {
        MutableLiveData<HomeEarnCoinResp> mutableLiveData = new MutableLiveData<>();
        String url = (BuildConfig.BASE_QBN_API + "exchange/v1/earn-coin");
        EasyHttp.post(url)
                .upObject(req)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<HomeEarnCoinResp>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeEarnCoinResp homeBean) {
                        mutableLiveData.postValue(homeBean);
                        //如果成功了。同步更新一次配置信息
                        getCoinCritConfig();
                    }
                });
        return mutableLiveData;
    }

    /**
     * 获取赚金币暴击模式配置信息
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<HomeCoinCritConfigBean> getCoinCritConfig() {
        MutableLiveData<HomeCoinCritConfigBean> mutableLiveData = new MutableLiveData<>();
        String url = (BuildConfig.BASE_QBN_API + "exchange/v1/coin-crit-config");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(url, true))
                .cacheKey("home_coin_crit_config")
                .cacheMode(CacheMode.CACHEANDREMOTEDISTINCT)
                .execute(new SimpleCallBack<HomeCoinCritConfigBean>() {
                    @Override
                    public void onError(ApiException e) {
                        HomeCoinCritConfigBean item = querySaveCoinCritConfig();
                        if (item != null) {
                            mutableLiveData.postValue(item);
                            return;
                        }
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeCoinCritConfigBean homeBean) {
                        saveCoinCritConfig(homeBean);
                        mutableLiveData.postValue(homeBean);
                    }
                });
        return mutableLiveData;
    }

    /**
     * 查询保存本地的配置
     *
     * @return
     */
    public HomeCoinCritConfigBean querySaveCoinCritConfig() {
        try {
            String saveFile = "coinCritConfigFile";
            String saveKey = "coinCritConfigKey";
            String json = SPUtils.getInstance(saveFile)
                    .getString(saveKey);
            HomeCoinCritConfigBean item = GsonUtils.fromJson(json, HomeCoinCritConfigBean.class);
            return item;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取网路数据
     *
     * @return 返回 homeBean的数据
     */
    public MutableLiveData<HomeCategory2Bean> getHomeCategoryBean() {
        MutableLiveData<HomeCategory2Bean> mutableLiveData = new MutableLiveData<>();
        String url = (BuildConfig.BASE_QBN_API + "exchange/v1/categories");
        EasyHttp.get(HttpConfigUtilsKt.withConfigParams(url, true))
                .cacheKey("home_category2")
                .cacheMode(CacheMode.CACHEANDREMOTEDISTINCT)
                .execute(new SimpleCallBack<HomeCategory2Bean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HomeCategory2Bean homeBean) {
                        mutableLiveData.postValue(homeBean);
                    }
                });
        return mutableLiveData;
    }

    /**
     * 保存配置信息
     *
     * @param bean
     */
    private void saveCoinCritConfig(HomeCoinCritConfigBean bean) {
        try {
            String saveFile = "coinCritConfigFile";
            String saveKey = "coinCritConfigKey";
            SPUtils.getInstance(saveFile)
                    .put(saveKey, GsonUtils.toJson(bean));
        } catch (Exception e) {
            if (BuildConfig.HTTP_DEBUG) {
                throw e;
            }
        }
    }

    /**
     * 获取当前服务器时间。单位：秒
     *
     * @return
     */
    private Long getCurrentServiceTime() {
        return com.donews.utilslibrary.utils.SPUtils.getLongInformain(TIME_SERVICE, System.currentTimeMillis() / 1000);
    }


    /**
     * 计算当前时间和记录的开启宝箱时间是否为同一天，比较给定的时间和当前服务器时间是否为同一天
     *
     * @param saveTimeMS 保存的时间(毫秒)
     * @return T:是同一天，F:不是同一天
     */
    private boolean getGiftIsDay(long saveTimeMS) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String curDate = df.format(new Date(getCurrentServiceTime() * 1000));
            String saveDate = df.format(saveTimeMS);
            return curDate.equals(saveDate);
        } catch (Exception e) {
            return false;
        }
    }

}
