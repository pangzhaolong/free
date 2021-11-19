package com.module.lottery.model;


import androidx.lifecycle.MutableLiveData;

import com.donews.base.BuildConfig;
import com.donews.base.model.BaseLiveDataModel;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.ContactCustomerBean;
import com.module.lottery.bean.GenerateCodeBean;
import com.module.lottery.bean.LotteryBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.bean.MaylikeBean;
import com.module.lottery.bean.ParticipateBean;
import com.module.lottery.bean.WinLotteryBean;

import java.util.Map;

//对数据进行访问，并且通知观察者
public class LotteryModel extends BaseLiveDataModel {

    private static String LOTTERY_BASE = BuildConfig.API_LOTTERY_URL;
    //抽奖列表猜你喜欢
    public static String LOTTERY_GUESS_LIKE = LOTTERY_BASE + "v1/similar-goods-list";
    //抽奖商品详情
    public static String LOTTERY_GUESS_INFO = LOTTERY_BASE + "v1/goods-detail";

    //抽奖商品参与人数
    public static String LOTTERY_PARTICIPATE_NUM = LOTTERY_BASE + "v1/goods-history-lottery";

    //抽奖码
    public static String LOTTERY_LOTTERY_CODE = LOTTERY_BASE + "v1/list-lottery-code";

    //生成抽奖码
    public static String LOTTERY_GENERATE_CODE = LOTTERY_BASE + "v1/gen-lottery-code";

    //抽奖次数达到上限后,推荐一个抽奖商品
    public static String LOTTERY_RECOMMEND_CODE = LOTTERY_BASE + "v1/recommend-lottery-goods";


    //获取抽奖中奖人员列表
    public static String LOTTERY_WIN_LOTTERY = LOTTERY_BASE + "v1/rand-lottery-info";

    /**
     * 获取网路数据
     *
     * @return 返回 SpikeBean
     */
    public void getNetData(MutableLiveData<MaylikeBean> mutableLiveData, String url, Map<String, String> params) {
        unDisposable();
        addDisposable(EasyHttp.get(url)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new SimpleCallBack<MaylikeBean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(MaylikeBean maylikeBean) {
                        if (maylikeBean != null) {
                            mutableLiveData.postValue(maylikeBean);
                        }
                    }
                }));
    }


    public void getNetCommodityData(MutableLiveData<CommodityBean> mutableLiveData, String url, Map<String, String> params) {
        unDisposable();
        addDisposable(EasyHttp.get(url)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new SimpleCallBack<CommodityBean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(CommodityBean commodityBean) {
                        if (commodityBean != null) {
                            mutableLiveData.postValue(commodityBean);
                        }
                    }
                }));
    }


    public void getNetParticipateData(MutableLiveData<ParticipateBean> mutableLiveData, String url, Map<String, String> params) {
        unDisposable();
        addDisposable(EasyHttp.get(url)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new SimpleCallBack<ParticipateBean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(ParticipateBean participateBean) {
                        if (participateBean != null) {
                            mutableLiveData.postValue(participateBean);
                        }
                    }
                }));
    }


    public void getNetLotteryCodeData(MutableLiveData<LotteryCodeBean> mutableLiveData, String url, Map<String, String> params) {
        unDisposable();
        addDisposable(EasyHttp.get(url)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .isShowToast(false)
                .execute(new SimpleCallBack<LotteryCodeBean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(LotteryCodeBean participateBean) {
                        if (participateBean != null) {
                            mutableLiveData.postValue(participateBean);
                        }
                    }
                }));
    }


    //获取联系客服的数据
    public void getContactCustomerData(MutableLiveData<ContactCustomerBean> mutableLiveData, String url, Map<String, String> params) {
        unDisposable();
        addDisposable(EasyHttp.get(url)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new SimpleCallBack<ContactCustomerBean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(ContactCustomerBean raidersBean) {
                        if (raidersBean != null) {
                            mutableLiveData.postValue(raidersBean);
                        }
                    }
                }));
    }


    /**
     * 获取网路数据
     *
     * @return 返回 SpikeBean
     */
    public void getGenerateCode(MutableLiveData<GenerateCodeBean> mutableLiveData, String url, Map<String, String> params) {
        unDisposable();
        addDisposable(EasyHttp.get(url)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new SimpleCallBack<GenerateCodeBean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(GenerateCodeBean maylikeBean) {
                        if (maylikeBean != null) {
                            mutableLiveData.postValue(maylikeBean);
                        }
                    }
                }));
    }


    /**
     * 查询中奖人员列表
     *
     * @return 返回 SpikeBean
     */
    public void getWinLotteryList(MutableLiveData<WinLotteryBean> mutableLiveData, String url, Map<String, String> params) {
        unDisposable();
        addDisposable(EasyHttp.get(url)
                .cacheMode(CacheMode.NO_CACHE)
                .params(params)
                .execute(new SimpleCallBack<WinLotteryBean>() {
                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(WinLotteryBean winLotteryBean) {
                        if (winLotteryBean != null) {
                            mutableLiveData.postValue(winLotteryBean);
                        }
                    }
                }));
    }


}
