package com.donews.mine.model;

import androidx.lifecycle.MutableLiveData;

import com.donews.base.model.BaseLiveDataModel;
import com.donews.common.contract.LoginHelp;
import com.donews.common.contract.UserInfoBean;
import com.donews.mine.BuildConfig;
import com.donews.mine.bean.QueryBean;
import com.donews.mine.Api.MineHttpApi;
import com.donews.mine.bean.resps.HistoryPeopleLottery;
import com.donews.mine.bean.resps.HistoryPeopleLotteryDetailResp;
import com.donews.mine.bean.resps.RecommendGoodsResp;
import com.donews.mine.bean.resps.WinRecordResp;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DeviceUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/16 15:30<br>
 * 版本：V1.0<br>
 */
public class MineModel extends BaseLiveDataModel {

    private Disposable disposable;

    /**
     * 请求个人参与记录的数据
     *
     * @param livData 通知数据
     * @return
     */
    public Disposable requestPeopleLottery(MutableLiveData<List<HistoryPeopleLottery.Period>> livData) {
        Disposable disop = EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/history-people-lottery")
                .params("offset", "1")
                .params("limit", "10")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<HistoryPeopleLottery>() {
                    @Override
                    public void onError(ApiException e) {
                        livData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HistoryPeopleLottery queryBean) {
                        if (queryBean.list == null) {
                            livData.postValue(new ArrayList());
                        } else {
                            livData.postValue(queryBean.list);
                        }
                    }
                });
        addDisposable(disop);
        return disop;
    }

    /**
     * 个人中奖记录
     *
     * @param livData 通知数据
     * @param offset  页码
     * @param limit   页大小
     * @return
     */
    public Disposable requestWinRecord(
            MutableLiveData<List<WinRecordResp.ListDTO>> livData,
            int offset,
            int limit) {
        Disposable disop = EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/win-record")
                .params("offset", "" + offset)
                .params("limit", "" + limit)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<WinRecordResp>() {
                    @Override
                    public void onError(ApiException e) {
                        livData.postValue(null);
                    }

                    @Override
                    public void onSuccess(WinRecordResp queryBean) {
                        if (queryBean.list == null) {
                            livData.postValue(new ArrayList());
                        } else {
                            livData.postValue(queryBean.list);
                        }
                    }
                });
        addDisposable(disop);
        return disop;
    }

    /**
     * 个人中心，往期中奖详情
     *
     * @param livData 通知数据
     * @param period  期数
     * @return
     */
    public Disposable requestHistoryPeopleLootteryDetail(
            MutableLiveData<HistoryPeopleLotteryDetailResp> livData,
            int period) {
        Disposable disop = EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/history-people-lottery-detail")
                .params("period", "" + period)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<HistoryPeopleLotteryDetailResp>() {
                    @Override
                    public void onError(ApiException e) {
                        livData.postValue(null);
                    }

                    @Override
                    public void onSuccess(HistoryPeopleLotteryDetailResp queryBean) {
                        livData.postValue(queryBean);
                    }
                });
        addDisposable(disop);
        return disop;
    }

    /**
     * 获取精选推荐列表数据
     *
     * @param livData 数据通知对象
     * @param limit 获取的数据数量
     * @return
     */
    public Disposable requestRecommendGoodsList(
            MutableLiveData<List<RecommendGoodsResp.ListDTO>> livData,
            int limit) {
        Disposable disop = EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/recommend-goods-list")
                .params("limit", "" + limit)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<RecommendGoodsResp>() {
                    @Override
                    public void onError(ApiException e) {
                        livData.postValue(null);
                    }

                    @Override
                    public void onSuccess(RecommendGoodsResp queryBean) {
                        if (queryBean.list == null) {
                            livData.postValue(new ArrayList<>());
                        } else {
                            livData.postValue(queryBean.list);
                        }
                    }
                });
        addDisposable(disop);
        return disop;
    }

    /**
     * 获取金币明细
     */
    public MutableLiveData<QueryBean> getQuery() {
        MutableLiveData<QueryBean> liveData = new MutableLiveData<>();
        addDisposable(EasyHttp.get(MineHttpApi.QUERY)
                .params("score_type", "balance")
                .params("user_id", AppInfo.getUserId())
                .params("app_name", DeviceUtils.getPackage())
                .params("suuid", DeviceUtils.getMyUUID())
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<QueryBean>() {
                    @Override
                    public void onError(ApiException e) {
                        liveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(QueryBean queryBean) {

                        liveData.postValue(queryBean);
                    }
                }));
        return liveData;
    }


}
