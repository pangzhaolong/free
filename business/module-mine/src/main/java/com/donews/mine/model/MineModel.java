package com.donews.mine.model;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.SPUtils;
import com.donews.base.model.BaseLiveDataModel;
import com.donews.base.utils.GsonUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.middle.bean.front.AwardBean;
import com.donews.mine.BuildConfig;
import com.donews.mine.bean.QueryBean;
import com.donews.mine.Api.MineHttpApi;
import com.donews.mine.bean.reqs.WidthdrawalReq;
import com.donews.mine.bean.resps.CurrentOpenRecord;
import com.donews.mine.bean.resps.CurrentServiceTime;
import com.donews.mine.bean.resps.HistoryPeopleLottery;
import com.donews.mine.bean.resps.HistoryPeopleLotteryDetailResp;
import com.donews.mine.bean.resps.RecommendGoodsResp;
import com.donews.mine.bean.resps.WinRecordResp;
import com.donews.mine.bean.resps.WithdraWalletResp;
import com.donews.mine.bean.resps.WithdrawConfigResp;
import com.donews.mine.bean.resps.WithdrawRecordResp;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DeviceUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private Calendar rightNow = Calendar.getInstance();

    /**
     * 获取最近中奖名单人数。滚动通知
     *
     * @param mutableLiveData 通知数据
     * @return
     */
    public Disposable getAwardList(MutableLiveData<AwardBean> mutableLiveData) {
        Disposable disposable = EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/rotation-lottery-info")
                .params("offset", "1")
                .params("limit", "10")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<AwardBean>() {

                    @Override
                    public void onError(ApiException e) {
                        mutableLiveData.postValue(null);
                    }

                    @Override
                    public void onSuccess(AwardBean awardBean) {
                        mutableLiveData.postValue(awardBean);
                    }
                });
        addDisposable(disposable);
        return disposable;
    }

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
     * 个人中心，往期中奖详情(此接口需要登录)
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
                        if (livData.getValue() == null) {
                            livData.postValue(null);
                        } else {
                            livData.postValue(livData.getValue());
                        }
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
     * 获取精选推荐列表数据(数据随机的)
     *
     * @param livData 数据通知对象
     * @param limit   获取的数据数量
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
     * 获取当前最新一期的期数
     *
     * @param livData  数据通知对象
     * @param isGetOna 是否获取上一期的期数
     * @return
     */
    public Disposable requestCurrentPeriod(
            MutableLiveData<Integer> livData, boolean isGetOna) {
        Disposable disop = EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/get-today-lottery-period")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<CurrentOpenRecord>() {
                    @Override
                    public void onError(ApiException e) {
                        if (livData.getValue() == null) {
                            livData.postValue(null);
                        } else {
                            livData.postValue(livData.getValue());
                        }
                    }

                    @Override
                    public void onSuccess(CurrentOpenRecord queryBean) {
                        if (queryBean != null && queryBean.period > 0) {
                            //因为显示的嗾使最新一期。所以获取的数据 -1
                            if (isGetOna) {
                                livData.postValue(dateTheDay(queryBean.period));
                            } else {
                                livData.postValue(queryBean.period);
                            }
                        } else {
                            livData.postValue(-1);
                        }
                    }
                });
        addDisposable(disop);
        return disop;
    }

    /**
     * 获取服务器当前时间
     *
     * @param livData 数据通知对象
     * @return
     */
    public Disposable requestCurrentNowTime(
            MutableLiveData<String> livData) {
        Disposable disop = EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/get-now-time")
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<CurrentServiceTime>() {
                    @Override
                    public void onError(ApiException e) {
                        if (livData.getValue() == null) {
                            livData.postValue(null);
                        } else {
                            livData.postValue(livData.getValue());
                        }
                    }

                    @Override
                    public void onSuccess(CurrentServiceTime queryBean) {
                        if (queryBean == null || queryBean.now == null || queryBean.now.isEmpty()) {
                            livData.postValue("");
                        } else {
                            livData.postValue(queryBean.now);
                        }
                    }
                });
        addDisposable(disop);
        return disop;
    }

    /**
     * 获取提现列表，实际上 = 积分列表
     *
     * @param livData 数据通知对象
     * @return
     */
    public Disposable requestWithdrawRecord(
            MutableLiveData<List<WithdrawRecordResp.RecordListDTO>> livData,
            int offset,
            int limit) {
        Disposable disop = EasyHttp.get(BuildConfig.API_WALLET_URL + "v1/score")
                .params("offset", "" + offset)
                .params("limit", "" + limit)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<WithdrawRecordResp>() {
                    @Override
                    public void onError(ApiException e) {
                        livData.postValue(null);
                    }

                    @Override
                    public void onSuccess(WithdrawRecordResp queryBean) {
                        if (queryBean == null || queryBean.list == null || queryBean.list.isEmpty()) {
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
     * 获取提现中心的配置
     *
     * @param livData 数据通知对象,如果为空则表示只刷新数据不通知数据
     * @return
     */
    public Disposable requestWithdrawCenterConfig(
            MutableLiveData<List<WithdrawConfigResp.WithdrawListDTO>> livData) {
        Disposable disop = EasyHttp.get(BuildConfig.API_WALLET_URL + "v1/withdraw/config")
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(false)
                .execute(new SimpleCallBack<WithdrawConfigResp>() {
                    @Override
                    public void onError(ApiException e) {
                        String locJson = SPUtils.getInstance().getString("withdraw_config");
                        try {
                            WithdrawConfigResp queryBean = GsonUtils.fromLocalJson(locJson, WithdrawConfigResp.class);
                            if (queryBean == null || queryBean.list == null || queryBean.list.isEmpty()) {
                                if (livData != null) {
                                    livData.postValue(null);
                                }
                            } else {
                                if (livData != null) {
                                    livData.postValue(queryBean.list);
                                }
                            }
                        } catch (Exception err) {
                            if (livData != null) {
                                livData.postValue(null);
                            }
                        }
                    }

                    @Override
                    public void onSuccess(WithdrawConfigResp queryBean) {
                        if (queryBean == null || queryBean.list == null || queryBean.list.isEmpty()) {
                            if (livData != null) {
                                livData.postValue(new ArrayList<WithdrawConfigResp.WithdrawListDTO>());
                            }
                        } else {
                            SPUtils.getInstance().put("withdraw_config", GsonUtils.toJson(queryBean));
                            if (livData != null) {
                                livData.postValue(queryBean.list);
                            }
                        }
                    }
                });
        addDisposable(disop);
        return disop;
    }

    /**
     * 获取钱包详情数据。总额等
     *
     * @param livData 数据通知对象
     * @return
     */
    public Disposable requestWithdraWallet(
            MutableLiveData<WithdraWalletResp> livData) {
        Disposable disop = EasyHttp.get(BuildConfig.API_WALLET_URL + "v1/wallet")
                .cacheMode(CacheMode.NO_CACHE)
                .isShowToast(false)
                .execute(new SimpleCallBack<WithdraWalletResp>() {
                    @Override
                    public void onError(ApiException e) {
                        String locJson = SPUtils.getInstance().getString("withdraw_detail");
                        try {
                            WithdraWalletResp queryBean = GsonUtils.fromLocalJson(locJson, WithdraWalletResp.class);
                            if (queryBean == null) {
                                if (livData != null) {
                                    livData.postValue(null);
                                }
                            } else {
                                if (livData != null) {
                                    livData.postValue(queryBean);
                                }
                            }
                        } catch (Exception err) {
                            if (livData != null) {
                                livData.postValue(null);
                            }
                        }
                    }

                    @Override
                    public void onSuccess(WithdraWalletResp queryBean) {
                        if (queryBean != null) {
                            SPUtils.getInstance().put("withdraw_detail", GsonUtils.toJson(queryBean));
                        }
                        if (livData != null) {
                            livData.postValue(queryBean);
                        }
                    }
                });
        addDisposable(disop);
        return disop;
    }

    /**
     * 提现
     *
     * @param livData 数据通知对象
     * @param dto
     * @param context
     * @return
     */
    public Disposable requestWithdra(
            MutableLiveData<Integer> livData,
            WithdrawConfigResp.WithdrawListDTO dto,
            Context context) {
        WidthdrawalReq req = new WidthdrawalReq();
        req.id = dto.id;
        Disposable disop = EasyHttp.post(BuildConfig.API_WALLET_URL + "v1/withdraw")
                .upObject(req)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<Object>() {
                    @Override
                    public void onError(ApiException e) {
                        if (e.getCode() == 22101) {
                            ToastUtil.showShort(context.getApplicationContext(), e.getMessage());
                        } else if (e.getCode() == 22102) {
                            ToastUtil.showShort(context.getApplicationContext(),
                                    e.getMessage());
                        } else if (e.getCode() == 22103) {
                            ToastUtil.showShort(context.getApplicationContext(),
                                    e.getMessage());
                        } else if (e.getCode() == 22104) {
                            ToastUtil.showShort(context.getApplicationContext(),
                                    e.getMessage());
                        } else if (e.getCode() == 22106) {
                            ToastUtil.showShort(context.getApplicationContext(),
                                    e.getMessage());
                        } else if (e.getCode() == 22199) {
                            ToastUtil.showShort(context.getApplicationContext(),
                                    e.getMessage());
                        }
                        livData.postValue(e.getCode());
                    }

                    @Override
                    public void onSuccess(Object queryBean) {
                        livData.postValue(0);
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

    /**
     * 获取指定日期的上一天。数字形式。例如：20211030
     *
     * @param dateNum 数字形式的日子,例如：20211101
     * @return 返回当前日期上一天的日期。数字形式。如果不存在则不返回传入日期
     */
    public int dateTheDay(int dateNum) {
        String dataString = "" + dateNum;
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(df.parse(dataString));
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
            return Integer.parseInt(df.format(calendar.getTime()));
        } catch (Exception e) {
            return dateNum;
        }
    }

}
