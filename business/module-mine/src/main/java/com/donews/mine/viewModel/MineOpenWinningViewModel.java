package com.donews.mine.viewModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ConvertUtils;
import com.dn.drouter.ARouteHelper;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.bean.front.AwardBean;
import com.donews.middle.bean.front.WinningRotationBean;
import com.donews.mine.R;
import com.donews.mine.bean.CSBean;
import com.donews.mine.bean.emus.WinTypes;
import com.donews.mine.bean.resps.HistoryPeopleLotteryDetailResp;
import com.donews.mine.bean.resps.RecommendGoodsResp;
import com.donews.mine.databinding.MineFragmentWinningCodeBinding;
import com.donews.mine.model.MineModel;
import com.donews.mine.utils.TextWinUtils;
import com.donews.mine.views.CSSView;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
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

    /**
     * 去往开奖页面
     *
     * @param needLotteryEvent 是否自动抽奖
     * @param position         关联的下标(无)
     * @param goods_id
     * @param from             来源
     *                         1：首页
     *                         2：往期开奖
     *                         3：个人参与记录
     */
    public static void goLotteryPage(
            boolean needLotteryEvent, int position, String goods_id, int from) {
        Postcard posta = ARouter.getInstance()
                .build(RouterFragmentPath.Lottery.PAGER_LOTTERY);
        if (position >= 0) {
            posta.withInt("position", position);
        }
        posta.withBoolean("needLotteryEvent", needLotteryEvent)
                .withString("goods_id", goods_id)
                .navigation();
    }

    public Context mContext;
    private MineFragmentWinningCodeBinding viewDataBinding;

    /**
     * 是否自动期数,作为是否为开奖页面的判断
     */
    public boolean isAutoPeriod = false;

    /**
     * 是否为主页加载
     */
    public boolean isMainLoad = false;

    /**
     * 是否为主页加载
     * 1：首页
     * 2：往期记录>开奖详情
     * 3：参与记录>开奖详情
     */
    public int from = -1;

    /**
     * 推荐列表
     */
    public MutableLiveData<List<RecommendGoodsResp.ListDTO>> recommendLivData = new MutableLiveData<>();

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
    /**
     * 滚动通知数据
     * null = 获取错误
     */
    public MutableLiveData<AwardBean> awardLiveData = new MutableLiveData<>(null);

    //城市区块的高度
    private int cidyLlHei = 0;
    //计时器的Handler
    private Handler timerHandler = new Handler();
    //计时器的任务
    private Runnable titmeRunTask = null;

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
        if (titmeRunTask != null) {
            timerHandler.removeCallbacks(titmeRunTask);
        }
    }

    /**
     * 加载滚动通知列表
     */
    public void loadAwardData() {
        mModel.getAwardList(awardLiveData);
    }

    /**
     * 获取推荐列表
     *
     * @param limit 需要加载的数量
     */
    public void loadRecommendData(int limit) {
        mModel.requestRecommendGoodsList(recommendLivData, limit);
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
            DateFormat dfD = new SimpleDateFormat("yyyyMMdd");
            //获取当前期的开奖时间
            long currentPeriodOpenTime = df.parse(openWinPeriod.getValue() + 1 + " 10:00:00").getTime();
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
                if (!isAutoPeriod && !isMainLoad) {
                    try {
                        //服务器是否已经到达当天
                        String isCurrDayDate = dfD.format(new Date(serviceTime * 1000));
                        if (openWinPeriod.getValue() == null ||
                                openWinPeriod.getValue() != mModel.dateTheDay(Integer.parseInt(isCurrDayDate))) {
                            //表示还未开奖。但是不需要显示计时，可能是个人记录查看参与记录
                            openWinCountdown.postValue(-2);
                            return; //不是自动加载。并且不是不是从首页进来的。是参与记录页面进来的
                        }
                    } catch (Exception e) {
                        //如果出错。则进行倒计时处理。所以这里不处理(已产品确认)
                    }
                }
                if (AppInfo.checkIsWXLogin()) {
                    //无论如何都加载详情数据(倒计时阶段。需要加载一次详情数据)
                    loadData(openWinPeriod.getValue(), true);
                } else {
                    //关闭刷新状态
                    viewDataBinding.mainWinCodeRefresh.finishRefresh();
                }
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
                //创建任务
                if (titmeRunTask == null) {
                    titmeRunTask = () -> {
                        if (openWinCountdown.getValue() == null) {
                            openWinCountdown.postValue(openWinCountdown.getValue());
                        } else {
                            if (openWinCountdown.getValue() <= 0) {
                                openWinCountdown.postValue(0);
                                timerHandler.removeCallbacks(titmeRunTask);
                                return;
                            }
                            //递减
                            openWinCountdown.postValue(openWinCountdown.getValue() - 1);
                            //不满足结束条件，接续添加
                            timerHandler.postDelayed(titmeRunTask, 1000);
                        }
                    };
                }
                cancelNotOpenWinCountDownTimer();
                //添加计时器，开始计时
                timerHandler.postDelayed(titmeRunTask, 1000);
            } else {
                cancelNotOpenWinCountDownTimer();
                openWinCountdown.postValue(0);
            }
        } catch (Exception e) {
            cancelNotOpenWinCountDownTimer();
            e.printStackTrace();
        }
    }

    //-----------------------------以下是视图代理更新相关---------------------------------

    /**
     * 更新倒计时的UI，未开奖时候的倒计时UI
     *
     * @param hh  小时(十位)
     * @param mm  分钟(十位)
     * @param ss  秒(十位)
     * @param hh1 小时(个位)
     * @param mm1 分钟(个位)
     * @param ss1 秒(个位)
     */
    public void updateCountDownUI(TextView hh, TextView mm, TextView ss, TextView hh1, TextView mm1, TextView ss1) {
        Integer oldBaseTime = openWinCountdown.getValue();
        int timeHH = 0;
        int timeMM = 0;
        int timeSS = 0;
        if (oldBaseTime == null) {
            hh.setText("0");
            mm.setText("0");
            ss.setText("0");
            hh1.setText("0");
            mm1.setText("0");
            ss1.setText("0");
            return;
        }
        timeHH = oldBaseTime / (60 * 60);
        timeMM = (oldBaseTime - (timeHH * 60 * 60)) / (60);
        timeSS = (oldBaseTime - (timeHH * 60 * 60) - (timeMM * 60));
        if (timeHH < 10) {
            hh.setText("0");
            hh1.setText("" + timeHH);
        } else {
            hh.setText("" + (timeHH / 10));
            hh1.setText("" + timeHH % 10);
        }
        if (timeMM < 10) {
            mm.setText("0");
            mm1.setText("" + timeMM);
        } else {
            mm.setText("" + (timeMM / 10));
            mm1.setText("" + (timeMM % 10));
        }
        if (timeSS < 10) {
            ss.setText("0");
            ss1.setText("" + timeSS);
        } else {
            ss.setText("" + (timeSS / 10));
            ss1.setText("" + (timeSS % 10));
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
     *
     * @param view     顶部区域的HeadView
     * @param loginBut 页面上的登录按钮
     */
    public void uiPosResetBuild(View view, LinearLayout loginBut) {
        //内容视图的根布局
        LinearLayout contentRootView = view.findViewById(R.id.mine_win_code_win_connect_layout);
        //中奖信息(开奖)模块视图
        ViewGroup openWinView = view.findViewById(R.id.mine_win_code_win_desc_layout);
        //我参加的视图模块
        LinearLayout myAddWinView = view.findViewById(R.id.mine_win_code_win_add_layout);
        //中奖模块列表
        LinearLayout winningView = view.findViewById(R.id.mine_win_code_sele_layout);
        if (detailLivData.getValue() == null) {
            return;
        }
        contentRootView.removeAllViews();
        if (!AppInfo.checkIsWXLogin()) {
            //只显示中奖模块
            loginBut.setOnClickListener(v -> {
                ARouter.getInstance()
                        .build(RouterActivityPath.User.PAGER_LOGIN)
                        .navigation();
            });
            //首页的开奖页面，登录按钮是否显示怎么显示(等待产品确认)
            if (!isAutoPeriod) {
                loginBut.setVisibility(View.VISIBLE);
            }
            winningView.setVisibility(View.VISIBLE);
            openWinView.setVisibility(View.GONE);
            myAddWinView.setVisibility(View.GONE);
            contentRootView.addView(winningView);
            contentRootView.addView(openWinView);
            contentRootView.addView(myAddWinView);
            return; //未登录
        }
        //已登录状态
        loginBut.setVisibility(View.GONE);
        if ((detailLivData.getValue().record == null || detailLivData.getValue().record.isEmpty()) &&
                (detailLivData.getValue().myWin == null || detailLivData.getValue().myWin.isEmpty())) {
            //未参与(显示中奖模块、我的参与模块)
            winningView.setVisibility(View.VISIBLE);
            myAddWinView.setVisibility(View.VISIBLE);
            openWinView.setVisibility(View.GONE);
            contentRootView.addView(winningView);
            contentRootView.addView(myAddWinView);
            contentRootView.addView(openWinView);
        } else {
            //已参与(显示所有，顺序为：我的中奖 -> 我的参与 -> )
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
        ImageView statusIcon = view.findViewById(R.id.mine_win_code_wzj_l_icon);
        TextView statusName = view.findViewById(R.id.mine_win_code_wzj_l_title);
        ViewGroup windVp = view.findViewById(R.id.mine_win_code_win_desc_layout); // 整个中奖模块区域的视图
        ViewGroup vGroup = view.findViewById(R.id.mine_win_code_win_desc_good_ll); //中奖模块中奖的Item的视图
        ViewGroup meZjVp = view.findViewById(R.id.mine_win_code_me_zl_rl); // 已中奖的容器
        ViewGroup meWZjVp = view.findViewById(R.id.mine_win_code_me_wzl_rl); // 未中奖的容器
        ViewGroup meWZjNotViewAddVp = view.findViewById(R.id.mine_win_code_wzj_child_ll); // 未中奖动态视图加入的容器
        vGroup.removeAllViews();
        meWZjNotViewAddVp.removeAllViews();
        windVp.setVisibility(View.VISIBLE);
        meZjVp.setVisibility(View.GONE);
        meWZjVp.setVisibility(View.GONE);
        if (detailLivData.getValue().myWin == null ||
                detailLivData.getValue().myWin.isEmpty()) {
            if (detailLivData.getValue().record.isEmpty() &&
                    detailLivData.getValue().myWin.isEmpty()) {
                //未参与，不显示中奖模块
                windVp.setVisibility(View.VISIBLE);
                statusIcon.setVisibility(View.GONE);
                return;
            }
            meZjVp.setVisibility(View.GONE);
            meWZjVp.setVisibility(View.VISIBLE);
            statusIcon.setVisibility(View.VISIBLE);
            statusIcon.setImageResource(R.drawable.mine_win_code_not_sele_icon);
            //已参与(参与了本期,但是未中奖)
            statusName.setTextColor(Color.BLACK);
            statusName.setText("很遗憾,你本次未中奖");
            //添加未中奖的容器
            View childView = getNotDataWindView(false, "", "坚持抽奖,总会中的");
            meWZjNotViewAddVp.addView(childView);
            return;
        }
        meZjVp.setVisibility(View.VISIBLE);
        meWZjVp.setVisibility(View.GONE);
        statusIcon.setVisibility(View.VISIBLE);
        statusIcon.setImageResource(R.drawable.mine_win_code_sele_icon);
        for (int i = 0; i < detailLivData.getValue().myWin.size(); i++) {
            HistoryPeopleLotteryDetailResp.WinerDTO item = detailLivData.getValue().myWin.get(i);
            View childView = View.inflate(view.getContext(), R.layout.incl_mine_winning_good_item, null);
            //标题区域
            ImageView uIcon = childView.findViewById(R.id.main_win_code_sele_item_icon);
            TextView uName = childView.findViewById(R.id.main_win_code_sele_item_name);
            TextView windCode = childView.findViewById(R.id.main_win_code_sele_item_code);
            //更新视图
            TextView seal = childView.findViewById(R.id.mine_win_item_seal);
            ImageView icon = childView.findViewById(R.id.mine_win_item_good_icon);
            TextView name = childView.findViewById(R.id.mine_win_item_good_name);
            TextView picre = childView.findViewById(R.id.mine_win_item_good_pic);
            TextView code = childView.findViewById(R.id.mine_win_item_snap_number);
            TextView goTo = childView.findViewById(R.id.mine_win_item_goto);
            //开始绑定数据
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatHeadUrlPrefix(item.avatar), uIcon);
            uName.setText(item.userName);
            windCode.setText(Html.fromHtml(TextWinUtils.drawOldText(
                    detailLivData.getValue().code, item.code
            )));
//            seal.setVisibility(View.VISIBLE);
//            if (WinTypes.Alike.type.equals(item.winType)) {
//                seal.setText(WinTypes.Alike.name);
//            } else if (WinTypes.Equal.type.equals(item.winType)) {
//                seal.setText(WinTypes.Equal.name);
//            } else {
//                seal.setText(WinTypes.None.name);
//            }
            name.setText(item.goods.title);
            picre.setText("¥" + item.goods.price);
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatUrlPrefix(item.goods.image), icon);
            code.setText(Html.fromHtml(
                    TextWinUtils.drawOldText(detailLivData.getValue().code, item.code)));
            goTo.setText("去领奖");
            childView.setOnClickListener((v) -> {
                Bundle bundle = new Bundle();
                bundle.putString("url",
                        "https://recharge-web.xg.tagtic.cn/jdd/index.html#/customer");
                bundle.putString("title", "客服");
                ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
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
        TextView nameTvNum = view.findViewById(R.id.mine_win_code_add_num);
        List<HistoryPeopleLotteryDetailResp.WinerDTO> currentAddRecord = new ArrayList<>();

        int initLoadSize = 2;
        if (isInitAdd) {
            if (detailLivData.getValue().record != null &&
                    detailLivData.getValue().record.size() > initLoadSize) {
                for (int i = 0; i < initLoadSize; i++) {
                    currentAddRecord.add(detailLivData.getValue().record.get(i));
                }
                moreLoadView.setVisibility(View.VISIBLE);
                moreLoadView.setOnClickListener(v -> {
                    addAddToGoods(view, false);
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
        //更新标题跟无数据视图
        if (detailLivData.getValue().record.isEmpty()) {
            //未参与
            nameTvNum.setText("0");
            View childView = getNotDataWindView(
                    true, "立即抽奖", "你本期未参与抽奖");
            vGroup.addView(childView);
        } else {
            //已参与(参与了本期)
            nameTvNum.setText("" + detailLivData.getValue().record.size());
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
            TextView more = childView.findViewById(R.id.mine_win_item_snap_number_more);
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
            picre.setText("¥" + item.goods.price);
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatUrlPrefix(item.goods.image), icon);
            code.setText(Html.fromHtml(
                    TextWinUtils.drawOldText(detailLivData.getValue().code, item.code)));
            goTo.setText("继续抽奖");
            code.setMaxLines(1); //默认一行
            if (item.code.size() > 3) {
                more.setVisibility(View.VISIBLE);
            } else {
                more.setVisibility(View.GONE);
            }
            more.setOnClickListener(v -> {
                more.setVisibility(View.GONE);
                code.setMaxLines(3);
            });
            childView.setOnClickListener((v) -> {
                ARouter.getInstance()
                        .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                        .withString("goods_id", item.goods.id)
                        .navigation();
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
    public void addSelectToNames(View view, boolean isInitAdd) {
        ViewGroup vGroup = view.findViewById(R.id.mine_win_code_sele_good_ll);
        ViewGroup moreLoadView = view.findViewById(R.id.mine_win_code_win_select_more);
        TextView nameTvNum = view.findViewById(R.id.mine_win_code_sele_num);
        List<HistoryPeopleLotteryDetailResp.WinerDTO> currentAddRecord = new ArrayList<>();
        int initLoadSize = 2;
        if (isInitAdd) {
            if (detailLivData.getValue().winer != null &&
                    detailLivData.getValue().winer.size() > initLoadSize) {
                for (int i = 0; i < initLoadSize; i++) {
                    currentAddRecord.add(detailLivData.getValue().winer.get(i));
                }
                moreLoadView.setVisibility(View.VISIBLE);
                moreLoadView.setOnClickListener(v -> {
                    addSelectToNames(view, false);
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
            //更新数量
            nameTvNum.setText("0");
            return;
        }
        //更新数量
        nameTvNum.setText("" + detailLivData.getValue().winer.size());
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
//            if (WinTypes.Alike.type.equals(item.winType)) {
//                seal.setText(WinTypes.Alike.name);
//            } else if (WinTypes.Equal.type.equals(item.winType)) {
//                seal.setText(WinTypes.Equal.name);
//            } else {
//                seal.setText(WinTypes.None.name);
//            }
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatHeadUrlPrefix(item.avatar), headImg);
            userName.setText(item.userName);
            winCode.setText(Html.fromHtml(
                    TextWinUtils.drawOldText(detailLivData.getValue().code, item.code)));
            GlideUtils.loadImageView(view.getContext(), UrlUtils.formatUrlPrefix(item.goods.image), goodIcon);
            goodName.setText(item.goods.title);
            goodPice.setText("" + item.goods.price);
            goTo.setText("试试手气");
            childView.setOnClickListener((v) -> {
                ARouter.getInstance()
                        .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                        .withString("goods_id", item.goods.id)
                        .navigation();
            });
            //添加视图
            vGroup.addView(childView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * 获取没有数据的视图
     *
     * @param isShowBut 是否显示按钮
     * @param butText   按钮的显示文字
     * @param descText  描述文本
     * @return
     */
    private View getNotDataWindView(
            boolean isShowBut, String butText, String descText) {
        View childView = View.inflate(mContext, R.layout.incl_open_win_not_data, null);
        LinearLayout rootV = childView.findViewById(R.id.mine_open_win_not_data_ll);
        TextView desc = childView.findViewById(R.id.mine_open_win_not_data);
        TextView but = childView.findViewById(R.id.mine_open_win_not_data_but);
        desc.setText(descText);
        but.setVisibility(View.GONE);
        if (isShowBut) {
            but.setVisibility(View.VISIBLE);
            if (butText != null && !butText.isEmpty()) {
                but.setText(butText);
            }
            childView.setOnClickListener(v -> {
                //去往首页
                ARouter.getInstance().build(RouterActivityPath.Main.PAGER_MAIN)
                        .withInt("position", 1)
                        .navigation();
            });
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        childView.setLayoutParams(lp);
        return childView;
    }
}
