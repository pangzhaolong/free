package com.donews.mine.viewModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.blankj.utilcode.util.ConvertUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.mine.BuildConfig;
import com.donews.mine.R;
import com.donews.mine.bean.CSBean;
import com.donews.mine.bean.emus.WinTypes;
import com.donews.mine.bean.resps.HistoryPeopleLotteryDetailResp;
import com.donews.mine.databinding.MineActivityWinningCodeBinding;
import com.donews.mine.databinding.MineFragmentWinningCodeBinding;
import com.donews.mine.model.MineModel;
import com.donews.mine.utils.TextWinUtils;
import com.donews.mine.views.CSSView;
import com.donews.utilslibrary.utils.UrlUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 开奖Fragment
 */
public class MineOpenWinningViewModel extends BaseLiveDataViewModel<MineModel> {
    public Context mContext;
    private MineFragmentWinningCodeBinding viewDataBinding;

    /**
     * 中奖详情的结果参数
     */
    public MutableLiveData<HistoryPeopleLotteryDetailResp> detailLivData = new MutableLiveData<>();
    /**
     * 服务器最新时间
     * null = 获取失败
     * “” = 未获取到数据
     * 非空 = 获取到的时间(秒)
     */
    public MutableLiveData<String> serviceTimeLiveData = new MutableLiveData<>(null);
    /**
     * 服务器获取当前的期数
     * null = 获取错误
     * < 0 : 表示数据获取错误
     * = 0 : 未知或缺省期数。需要自动验证期数
     * > 0 : 正常期数
     */
    public MutableLiveData<Integer> openWinPeriod = new MutableLiveData<>(null);
    /**
     * 开奖倒计时的数字
     * null = 获取错误
     * <= 0 : 表示已开奖
     * > 0 : 表示未开奖
     */
    public MutableLiveData<Integer> openWinCountdown = new MutableLiveData<>(null);

    //开奖倒计时的计时器
    private CountDownTimer openWintimer;
    //城市区块的高度
    private int cidyLlHei = 0;

    public void setDataBinDing(MineFragmentWinningCodeBinding dataBinding, FragmentActivity act) {
        this.viewDataBinding = dataBinding;
        this.mContext = act;
    }

    @Override
    public MineModel createModel() {
        return new MineModel();
    }

    @Override
    protected void onCleared() {
        cancelNotOpenWinCountDownTimer();
        super.onCleared();
    }

    /**
     * 生命周期销毁了。需要终止的相关操作
     */
    public void destroy() {
        cancelNotOpenWinCountDownTimer();
    }

    /**
     * 取消未开奖的计时器
     */
    public void cancelNotOpenWinCountDownTimer() {
        if (openWintimer != null) {
            openWintimer.cancel();
        }
    }

    /**
     * 获取数据，
     *
     * @param period          期数 0：自动计算 ，>0 :加载指定期数
     * @param isMandatoryLoad 是否强制加载数据
     */
    public void loadData(int period, boolean isMandatoryLoad) {
        if (isMandatoryLoad) {
            //强制数据加载。不进行校验。否则将会递归调用
            mModel.requestHistoryPeopleLootteryDetail(detailLivData, period);
            return;
        }
        if (period != 0) {
            openWinPeriod.postValue(period);
        } else {
            Integer autoPeriod = openWinPeriod.getValue();
            if (autoPeriod == null || autoPeriod <= 0) {
                //没有的话从服务器获取
                mModel.requestCurrentPeriod(openWinPeriod, true);
                return;
            }
            Integer openWinCountDown = openWinCountdown.getValue();
            if (openWinCountDown == null || openWinCountDown > 0) {
                //还未开奖或者无法确定是否已开奖。重新获取一次服务器事件再次计算一次
                mModel.requestCurrentNowTime(serviceTimeLiveData);
                return;
            }
            //已经开奖。并且所有结果计算正确。加载开奖详情数据
            mModel.requestHistoryPeopleLootteryDetail(detailLivData, autoPeriod);
        }
    }

    /**
     * 获取服务器最新时间
     */
    public void getServiceTime() {
        mModel.requestCurrentNowTime(serviceTimeLiveData);
    }

    /**
     * 计算服务器时间。确定是否开奖。并且决定显示什么页面视图
     */
    public void calculateServiceTime() {
        cancelNotOpenWinCountDownTimer();
        try {
            DateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
            //获取当前期的开奖时间
            long currentPeriodOpenTime = df.parse(openWinPeriod.getValue() + 1 + " 10:00:00").getTime();
            //最新期数的时间。因为是早上10点开奖。所以计算到早上10点
            long newPeriodTime = df.parse(openWinPeriod.getValue() + " 10:00:00").getTime();
            if (serviceTimeLiveData.getValue() == null) {
                openWinCountdown.postValue(null);
                return;
            }
            long serviceTime = Long.parseLong(serviceTimeLiveData.getValue());
            if (serviceTime - currentPeriodOpenTime / 1000 >= 0) {
                //服务器时间大于开奖时间。表示已经开奖
                openWinCountdown.postValue(0);
                return;
            }
            //服务器时间小于开奖时间。那么计算开奖时间的差距准备计时
            int stepTime = (int) (serviceTime - currentPeriodOpenTime / 1000);
            if (stepTime < 0) {
                //开启计时器(通过服务器时间计算得出的需要计数的时间)
                int countDownTime = Math.abs(stepTime);
                if (openWinCountdown.getValue() == null) {
                    openWinCountdown.postValue(countDownTime);
                } else {
                    if (countDownTime > openWinCountdown.getValue()) {
                        //可以理解为。网络未能成功返回服务器时间导致计时不准。直接以上一次的时间继续计算
                        openWinCountdown.postValue(openWinCountdown.getValue());
                    } else {
                        //可以认为服务器时间准确。可以使用
                        openWinCountdown.postValue(countDownTime);
                    }
                }
                openWintimer = new CountDownTimer(countDownTime * 1000L, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (openWinCountdown.getValue() == null) {
                            openWinCountdown.postValue(openWinCountdown.getValue());
                        } else {
                            openWinCountdown.postValue(openWinCountdown.getValue() - 1);
                        }
                    }

                    @Override
                    public void onFinish() {
                        if (openWinCountdown.getValue() == null) {
                            openWinCountdown.postValue(openWinCountdown.getValue());
                        } else {
                            openWinCountdown.postValue(0);
                        }
                    }
                };
                openWintimer.start();
            } else {
                openWinCountdown.postValue(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-----------------------------以下是视图代理更新相关---------------------------------

    /**
     * 更新倒计时的UI，未开奖时候的倒计时UI
     *
     * @param hh 小时
     * @param mm 分钟
     * @param ss 秒
     */
    public void updateCountDownUI(TextView hh, TextView mm, TextView ss) {
        Integer oldBaseTime = openWinCountdown.getValue();
        int timeHH = 0;
        int timeMM = 0;
        int timeSS = 0;
        if (oldBaseTime == null) {
            hh.setText("00");
            mm.setText("00");
            ss.setText("00");
            return;
        }
        timeHH = oldBaseTime / (60 * 60);
        timeMM = (oldBaseTime - (timeHH * 60 * 60)) / (60);
        timeSS = (oldBaseTime - (timeHH * 60 * 60) - (timeMM * 60));
        if (timeHH < 10) {
            hh.setText("0" + timeHH);
        } else {
            hh.setText("" + timeHH);
        }
        if (timeMM < 10) {
            mm.setText("0" + timeMM);
        } else {
            mm.setText("" + timeMM);
        }
        if (timeSS < 10) {
            ss.setText("0" + timeSS);
        } else {
            ss.setText("" + timeSS);
        }
    }

    /**
     * 更新其他数据
     *
     * @param view
     */
    public void updateData(View view) {
        //更新UI
        //城市区块操作
        LinearLayout cidyLl = view.findViewById(R.id.icnl_mine_win_city_ll);
        ImageView aollwIv = view.findViewById(R.id.mine_win_code_scan_allow);
        if (cidyLlHei <= 0) {
            cidyLlHei = cidyLl.getHeight();
        }
        aollwIv.setOnClickListener(v -> {
            if (cidyLlHei <= 0) {
                cidyLlHei = cidyLl.getHeight();
            }
            if (cidyLl.getVisibility() == View.VISIBLE) {
                cidyLl.setVisibility(View.GONE);
                aollwIv.setRotation(0);
            } else {
                cidyLl.setVisibility(View.VISIBLE);
                aollwIv.setRotation(180);
            }
        });
        if (detailLivData.getValue() == null) {
            return;
        }
        //更新数据
        char[] codeChid = detailLivData.getValue().code.toCharArray();
        //更新顶部数字
        ViewGroup numGP = view.findViewById(R.id.main_win_code_num_layout);
        for (int i = 0; i < numGP.getChildCount(); i++) {
            View numItemV = numGP.getChildAt(i);
            TextView numTv = numItemV.findViewById(R.id.icnl_mine_win_num);
            try {
                if (i < codeChid.length) {
                    numTv.setText(String.valueOf(codeChid[i]));
                }
            } catch (Exception e) {
            }
        }
        //更新城市数据
        CSSView cityV = view.findViewById(R.id.mine_win_code_city_v);
        List<CSBean> list = new ArrayList<>();
        for (HistoryPeopleLotteryDetailResp.SpeedsDTO speed : detailLivData.getValue().speeds) {
            list.add(new CSBean(speed.city, speed.speed));
        }
        cityV.refreshData(list);
    }

    /**
     * 根据不同的状态。构建UI的位置顺序
     */
    public void uiPosResetBuild(View view) {
        //内容视图的根布局
        LinearLayout contentRootView = view.findViewById(R.id.mine_win_code_win_connect_layout);
        //中奖信息(开奖)模块视图
        LinearLayout openWinView = view.findViewById(R.id.mine_win_code_win_desc_layout);
        //我参加的视图模块
        LinearLayout myAddWinView = view.findViewById(R.id.mine_win_code_win_add_layout);
        //中奖模块列表
        LinearLayout winningView = view.findViewById(R.id.mine_win_code_sele_layout);
        if (detailLivData.getValue() == null) {
            return;
        }
        contentRootView.removeAllViews();
        if (detailLivData.getValue().record.isEmpty() &&
                detailLivData.getValue().myWin.isEmpty()) {
            //未参与
            winningView.setVisibility(View.VISIBLE);
            openWinView.setVisibility(View.VISIBLE);
            myAddWinView.setVisibility(View.GONE);
            contentRootView.addView(winningView);
            contentRootView.addView(openWinView);
            contentRootView.addView(myAddWinView);
        } else {
            //已参与(参与了本期)
            winningView.setVisibility(View.VISIBLE);
            openWinView.setVisibility(View.VISIBLE);
            myAddWinView.setVisibility(View.VISIBLE);
            contentRootView.addView(openWinView);
            contentRootView.addView(myAddWinView);
            contentRootView.addView(winningView);
        }
    }

    /**
     * 添加中奖商品数据到视图
     *
     * @param view 视图对象
     */
    @SuppressLint("SetTextI18n")
    public void addSelectGoods(View view) {
        TextView statusName = view.findViewById(R.id.mine_win_code_win_desc_name);
        ViewGroup vGroup = view.findViewById(R.id.mine_win_code_win_desc_good_ll);
        vGroup.removeAllViews();
        if (detailLivData.getValue().myWin == null ||
                detailLivData.getValue().myWin.isEmpty()) {
            if (detailLivData.getValue().record.isEmpty() &&
                    detailLivData.getValue().myWin.isEmpty()) {
                //未参与
                statusName.setText("很抱歉,本期你未参与");
            } else {
                //已参与(参与了本期)
                statusName.setText("很遗憾,你未中奖");
            }
            View childView = View.inflate(view.getContext(), R.layout.incl_open_win_not_data, null);
            LinearLayout rootV = childView.findViewById(R.id.mine_open_win_not_data_ll);
            TextView desc = childView.findViewById(R.id.mine_open_win_not_data);
            TextView but = childView.findViewById(R.id.mine_open_win_not_data_but);
            but.setVisibility(View.VISIBLE);
            desc.setText("抽奖多的人\n当然容易免单呀");
            but.setOnClickListener(v -> {
                ToastUtil.show(view.getContext(), "参与更多抽奖>>>");
            });
            //添加未中奖视图
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            vGroup.addView(childView, lp);
            return;
        }
        statusName.setText("恭喜你,获得大奖");
        for (int i = 0; i < detailLivData.getValue().myWin.size(); i++) {
            HistoryPeopleLotteryDetailResp.WinerDTO item = detailLivData.getValue().myWin.get(i);
            View childView = View.inflate(view.getContext(), R.layout.incl_mine_winning_record_list_good, null);
            //更新视图
            TextView seal = childView.findViewById(R.id.mine_win_item_seal);
            ImageView icon = childView.findViewById(R.id.mine_win_item_good_icon);
            TextView name = childView.findViewById(R.id.mine_win_item_good_name);
            TextView picre = childView.findViewById(R.id.mine_win_item_good_pic);
            TextView code = childView.findViewById(R.id.mine_win_item_snap_number);
            TextView goTo = childView.findViewById(R.id.mine_win_item_goto);
            //开始绑定数据
            seal.setVisibility(View.VISIBLE);
            if (WinTypes.Alike.type.equals(item.winType)) {
                seal.setText(WinTypes.Alike.name);
            } else if (WinTypes.Equal.type.equals(item.winType)) {
                seal.setText(WinTypes.Equal.name);
            } else {
                seal.setVisibility(View.GONE);
                seal.setText(WinTypes.None.name);
            }
            name.setText(item.goods.title);
            picre.setText("" + item.goods.price);
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatUrlPrefix(item.goods.image), icon);
            code.setText(Html.fromHtml(
                    TextWinUtils.drawOldText(detailLivData.getValue().code, item.code)));
            goTo.setText("去领奖");
            goTo.setOnClickListener((v) -> {
                ToastUtil.show(view.getContext(), "前往去领奖页面");
            });
            //添加视图
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = ConvertUtils.dp2px(10);
            vGroup.addView(childView, lp);
        }
    }

    /**
     * 添加参与商品到容器
     *
     * @param view      视图对象
     * @param isInitAdd 是否初始添加，如果是则只添加3个默认，T:初始加载，F:加载更多
     */
    public void addAddToGoods(View view, boolean isInitAdd) {
        ViewGroup vGroup = view.findViewById(R.id.mine_win_code_win_add_good_ll);
        ViewGroup moreLoadView = view.findViewById(R.id.mine_win_code_win_add_good_more);
        List<HistoryPeopleLotteryDetailResp.WinerDTO> currentAddRecord = new ArrayList<>();
        int initLoadSize = 4;
        if (isInitAdd) {
            if (detailLivData.getValue().record != null &&
                    detailLivData.getValue().record.size() > initLoadSize) {
                for (int i = 0; i < initLoadSize; i++) {
                    currentAddRecord.add(detailLivData.getValue().record.get(i));
                }
                moreLoadView.setVisibility(View.VISIBLE);
                moreLoadView.setOnClickListener(v->{
                    addAddToGoods(view,false);
                });
            } else {
                if (detailLivData.getValue().record != null) {
                    currentAddRecord.addAll(detailLivData.getValue().record);
                }
                moreLoadView.setVisibility(View.GONE);
            }
            vGroup.removeAllViews();
        } else {
            if (detailLivData.getValue().record != null) {
                for (int i = initLoadSize; i < detailLivData.getValue().record.size(); i++) {
                    currentAddRecord.add(detailLivData.getValue().record.get(i));
                }
            }
            moreLoadView.setVisibility(View.GONE);
        }
        if (detailLivData.getValue().record == null ||
                detailLivData.getValue().record.isEmpty()) {
            return;
        }
        for (int i = 0; i < currentAddRecord.size(); i++) {
            HistoryPeopleLotteryDetailResp.WinerDTO item = currentAddRecord.get(i);
            View childView = View.inflate(view.getContext(), R.layout.incl_mine_winning_record_list_good, null);
            //更新视图
            TextView seal = childView.findViewById(R.id.mine_win_item_seal);
            ImageView icon = childView.findViewById(R.id.mine_win_item_good_icon);
            TextView name = childView.findViewById(R.id.mine_win_item_good_name);
            TextView picre = childView.findViewById(R.id.mine_win_item_good_pic);
            TextView code = childView.findViewById(R.id.mine_win_item_snap_number);
            TextView goTo = childView.findViewById(R.id.mine_win_item_goto);
            //开始绑定数据
            if (WinTypes.Alike.type.equals(item.winType)) {
                seal.setText(WinTypes.Alike.name);
            } else if (WinTypes.Equal.type.equals(item.winType)) {
                seal.setText(WinTypes.Equal.name);
            } else {
                seal.setVisibility(View.GONE);
                seal.setText(WinTypes.None.name);
            }
            name.setText(item.goods.title);
            picre.setText("" + item.goods.price);
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatUrlPrefix(item.goods.image), icon);
            code.setText(Html.fromHtml(
                    TextWinUtils.drawOldText(detailLivData.getValue().code, item.code)));
            goTo.setText("继续抽奖");
            goTo.setOnClickListener((v) -> {
                ToastUtil.show(view.getContext(), "去往继续抽奖页面");
            });
            //添加视图
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = ConvertUtils.dp2px(10);
            vGroup.addView(childView, lp);
        }
    }

    /**
     * 添加中奖名单到容器
     *
     * @param view      视图对象
     * @param isInitAdd 是否初始添加，如果是则只添加3个默认，T:初始加载，F:加载更多
     */
    public void addSelectToNames(View view,boolean isInitAdd) {
        ViewGroup vGroup = view.findViewById(R.id.mine_win_code_sele_good_ll);
        ViewGroup moreLoadView = view.findViewById(R.id.mine_win_code_win_select_more);
        List<HistoryPeopleLotteryDetailResp.WinerDTO> currentAddRecord = new ArrayList<>();
        int initLoadSize = 2;
        if (isInitAdd) {
            if (detailLivData.getValue().winer != null &&
                    detailLivData.getValue().winer.size() > initLoadSize) {
                for (int i = 0; i < initLoadSize; i++) {
                    currentAddRecord.add(detailLivData.getValue().winer.get(i));
                }
                moreLoadView.setVisibility(View.VISIBLE);
                moreLoadView.setOnClickListener(v->{
                    addSelectToNames(view,false);
                });
            } else {
                if (detailLivData.getValue().record != null) {
                    currentAddRecord.addAll(detailLivData.getValue().record);
                }
                moreLoadView.setVisibility(View.GONE);
            }
            vGroup.removeAllViews();
        } else {
            if (detailLivData.getValue().winer != null) {
                for (int i = initLoadSize; i < detailLivData.getValue().winer.size(); i++) {
                    currentAddRecord.add(detailLivData.getValue().winer.get(i));
                }
            }
            moreLoadView.setVisibility(View.GONE);
        }
        if (detailLivData.getValue().winer == null) {
            return;
        }
        for (HistoryPeopleLotteryDetailResp.WinerDTO item : currentAddRecord) {
            View childView = View.inflate(view.getContext(), R.layout.incl_mine_win_code_sele_item, null);
            TextView seal = childView.findViewById(R.id.main_win_code_sele_item_seal);
            ImageView headImg = childView.findViewById(R.id.main_win_code_sele_item_icon);
            TextView userName = childView.findViewById(R.id.main_win_code_sele_item_name);
            TextView winCode = childView.findViewById(R.id.main_win_code_sele_item_code);
            ImageView goodIcon = childView.findViewById(R.id.main_win_code_sele_good_icon);
            TextView goodName = childView.findViewById(R.id.main_win_code_sele_good_name);
            TextView goodPice = childView.findViewById(R.id.main_win_code_sele_good_pic);
            TextView goTo = childView.findViewById(R.id.main_win_code_sele_good_goto);
            //开始绑定数据
            if (WinTypes.Alike.type.equals(item.winType)) {
                seal.setText(WinTypes.Alike.name);
            } else if (WinTypes.Equal.type.equals(item.winType)) {
                seal.setText(WinTypes.Equal.name);
            } else {
                seal.setText(WinTypes.None.name);
            }
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatUrlPrefix(item.avatar), headImg);
            userName.setText(item.userName);
            winCode.setText(Html.fromHtml(
                    TextWinUtils.drawOldText(detailLivData.getValue().code, item.code)));
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatUrlPrefix(item.goods.image), goodIcon);
            goodName.setText(item.goods.title);
            goodPice.setText("" + item.goods.price);
            goTo.setOnClickListener((v) -> {
                ToastUtil.show(view.getContext(), "去往立即抢购");
            });
            //添加视图
            vGroup.addView(childView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }
}
