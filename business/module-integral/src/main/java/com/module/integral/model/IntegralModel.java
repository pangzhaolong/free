package com.module.integral.model;


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
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.bean.MaylikeBean;
import com.module.lottery.bean.ParticipateBean;
import com.module.lottery.bean.WinLotteryBean;

import java.util.Map;

//对数据进行访问，并且通知观察者
public class IntegralModel extends BaseLiveDataModel {

    private static String LOTTERY_BASE = BuildConfig.API_LOTTERY_URL;
    //抽奖列表猜你喜欢
    public static String LOTTERY_GUESS_LIKE = LOTTERY_BASE + "v1/similar-goods-list";
    //抽奖商品详情
    public static String LOTTERY_GUESS_INFO = LOTTERY_BASE + "v1/goods-detail";





}
