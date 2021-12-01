package com.module.lottery.ui;

import static com.module.lottery.dialog.ReturnInterceptDialog.TYPE_1;
import static com.module.lottery.dialog.ReturnInterceptDialog.TYPE_2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.LoginUserStatus;
import com.dn.events.events.LotteryStatusEvent;
import com.donews.base.utils.ToastUtil;
import com.donews.common.ad.business.callback.JddAdIdConfigManager;
import com.donews.common.provider.IDetailProvider;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DateManager;
import com.module.lottery.adapter.GuessAdapter;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.GenerateCodeBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.dialog.CongratulationsDialog;
import com.module.lottery.dialog.ExhibitCodeStartsDialog;
import com.module.lottery.dialog.GenerateCodeDialog;
import com.module.lottery.dialog.LessMaxDialog;
import com.module.lottery.dialog.LogToWeChatDialog;
import com.module.lottery.dialog.LotteryCodeStartsDialog;
import com.module.lottery.dialog.ReturnInterceptDialog;
import com.module.lottery.dialog.ReceiveLotteryDialog;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.utils.ClickDoubleUtil;
import com.module.lottery.utils.GridSpaceItemDecoration;
import com.module.lottery.viewModel.LotteryViewModel;
import com.module_lottery.R;
import com.module_lottery.databinding.GuesslikeHeadLayoutBinding;
import com.module_lottery.databinding.LotteryMainLayoutBinding;
import com.orhanobut.logger.Logger;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;


@Route(path = RouterFragmentPath.Lottery.PAGER_LOTTERY)
public class LotteryActivity extends BaseActivity<LotteryMainLayoutBinding, LotteryViewModel> {
    private static final String LOTTERY_ACTIVITY = "LOTTERY_ACTIVITY";
    private static final String FIRST_SHOW = "first_show";
    @Autowired(name = "goods_id")
    public String mGoodsId;
    private SharedPreferences mSharedPreferences;
    // 是否自动开始抽奖 true 进入页面自动开始 false
    @Autowired(name = "start_lottery")
    public boolean mStart_lottery = false;
    @Autowired(name = "position")
    int mPosition;
    @Autowired(name = "needLotteryEvent")
    public boolean mMeedLotteryEvent;
    //    String id = "tb:655412572200";
    public GuessAdapter guessAdapter;
    @Autowired(name = "action")
    public String mAction;
    private CommodityBean mCommodityBean;
    private int mPageNumber = 1;
    private boolean refresh = true;
    //抽奖码的对象用来拦截返回
    private LotteryCodeBean mLotteryCodeBean;
    private boolean dialogShow = false;
    @Autowired(name = RouterActivityPath.GoodsDetail.GOODS_DETAIL_PROVIDER)
    public IDetailProvider detailProvider;
    private LogToWeChatDialog mLogToWeChatDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.lottery_main_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = this.getSharedPreferences(LOTTERY_ACTIVITY, 0);
        ARouter.getInstance().inject(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        guessAdapter = new GuessAdapter(LotteryActivity.this);
        guessAdapter.getLayout(R.layout.guesslike_item_layout);
        mDataBinding.lotteryGridview.setLayoutManager(gridLayoutManager);
        mDataBinding.lotteryGridview.setAdapter(guessAdapter);
        mDataBinding.lotteryGridview.addItemDecoration(new GridSpaceItemDecoration(2));
        mDataBinding.returnIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnIntercept();
            }
        });
        mDataBinding.maskingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBinding.maskingLayout.setVisibility(View.GONE);
            }
        });
        initViewData();
        setHeaderView(mDataBinding.lotteryGridview);
        setObserveList();
        //加载抽奖商品详情信息
        lotteryInfo();
        //设置下拉，和上拉的监听
        setSmartRefresh();
        //自动开始抽奖
        if (mStart_lottery && ABSwitch.Ins().isOpenAutoLottery()) {
            ifOpenAutoLotteryAndCount();
        }
        mStart_lottery = false;
    }

    /**
     * 判断是否需要自动抽奖,并且是否大于了中台控制次数
     */
    private void ifOpenAutoLotteryAndCount() {
        if (ABSwitch.Ins().isOpenAutoLottery()) {
            //获取配置的最低次数
            int configurationCount = ABSwitch.Ins().getOpenAutoLotteryCount();
            //获取已抽奖的次数
            int frequency = DateManager.getInstance().getLotteryCount(DateManager.LOTTERY_COUNT);
            if (frequency >= configurationCount) {
                luckyDrawEntrance();
            }
        }
    }


    /**
     * 未登录时
     * 点击抽奖页面0元按钮 弹框点击登录回调后
     */
    private void ifDialogAutomaticDraw() {
        //读取中台配置
        if (ABSwitch.Ins().isOpenAutoLottery()) {
            luckyDrawEntrance();
        }
    }


    /**
     * 未登录时 的返回拦截
     * 点击抽奖页面0元按钮 弹框点击登录回调后
     */
    private void ifInterceptDialogAutomaticDraw() {
        //读取中台配置
        if (ABSwitch.Ins().isOpenAutoLottery()) {
            luckyDrawEntrance();
        }
    }



    public void luckyDrawEntrance() {
        boolean logType = AppInfo.checkIsWXLogin();
        if (logType) {
            if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() >= 6) {
                return;
            }
            startLottery();
        } else {
            showLogToWeChatDialog();

        }
    }


    private void initViewData() {
        mDataBinding.jsonAnimationRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                luckyDrawEntrance();
            }
        });
        mDataBinding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享
                RouterActivityPath.Mine
                        .goShareWxChatDialogDefaultH5(LotteryActivity.this);
            }
        });
        mDataBinding.panicBuying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalysisUtils.onEventEx(LotteryActivity.this, Dot.Btn_Buy);
                if (mCommodityBean != null) {
                    detailProvider.goToTaoBao(LotteryActivity.this, mCommodityBean.getItemLink());
                }

            }
        });
        //当抽奖码大于等于6时显示等待开奖
        if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() >= 5) {
            setLottieAnimation(false);
        } else {
            setLottieAnimation(true);
        }
        boolean first_show = mSharedPreferences.getBoolean(FIRST_SHOW, true);
        if (first_show) {
            mSharedPreferences.edit().putBoolean(FIRST_SHOW, false).commit();
            mDataBinding.maskingLayout.setVisibility(View.VISIBLE);
            //圆 新手引导遮罩层
            initLottie(mDataBinding.maskingButton, "lottery_round.json");
            //小手 新手引导遮罩层
            initLottie(mDataBinding.maskingHand, "lottery_finger.json");
        } else {
            mDataBinding.maskingLayout.setVisibility(View.GONE);
        }
    }

    private void setLottieAnimation(boolean play) {
        if (play) {
            //设置动画
            //小手
            initLottie(mDataBinding.jsonAnimation, "lottery_finger.json");
            mDataBinding.jsonAnimation.setVisibility(View.VISIBLE);
            //圆
            initLottie(mDataBinding.jsonAnimationRound, "lottery_round.json");

        } else {
            mDataBinding.jsonAnimation.setVisibility(View.GONE);
            mDataBinding.jsonAnimationRound.pauseAnimation();
            mDataBinding.jsonAnimationRound.setProgress(0);

        }
    }

    private void initLottie(LottieAnimationView view, String json) {
        if (view != null && !view.isAnimating()) {
            view.setImageAssetsFolder("images");
            view.setAnimation(json);
            view.loop(true);
            view.playAnimation();
        }
    }


    //开始抽奖
    private void startLottery() {
        AnalysisUtils.onEventEx(this, Dot.Btn_LotteryNow);
        //判断是否打开了视频广告
        boolean isOpenAd = JddAdIdConfigManager.INSTANCE.getConfig().getOpenAd();
        if (isOpenAd) {
            //开始抽奖
            //弹框抽奖码生成dialog
            LotteryCodeStartsDialog lotteryCodeStartsDialog = new LotteryCodeStartsDialog(LotteryActivity.this);
            lotteryCodeStartsDialog.setStateListener(new LotteryCodeStartsDialog.OnStateListener() {
                @Override
                public void onFinish() {
                    try {
                        if (!LotteryActivity.this.isFinishing() && lotteryCodeStartsDialog != null && lotteryCodeStartsDialog.isShowing()) {
                            lotteryCodeStartsDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onJumpAdFinish() {
                    //弹起生成抽奖码的dialog
                    showGenerateCodeDialog();
                }
            });
            lotteryCodeStartsDialog.create();
            lotteryCodeStartsDialog.show();
        } else {
            //判断是否支持抽奖
            if (DateManager.getInstance().timesLimit(DateManager.LOTTERY_KEY, DateManager.NUMBER_OF_DRAWS, 24)) {
                showGenerateCodeDialog();
            } else {
                ToastUtil.showShort(this, "今天次数已用完，明日再来");
            }
        }

    }


    private void showLogToWeChatDialog() {
        if (ClickDoubleUtil.Companion.isFastClick()) {
            if (mLogToWeChatDialog == null) {
                mLogToWeChatDialog = new LogToWeChatDialog(LotteryActivity.this);
                mLogToWeChatDialog.setFinishListener(new LogToWeChatDialog.OnFinishListener() {
                    @Override
                    public void onDismiss() {
                        if (mLogToWeChatDialog != null && mLogToWeChatDialog.isShowing() && !LotteryActivity.this.isFinishing()) {
                            mLogToWeChatDialog.dismiss();
                        }
                        mLogToWeChatDialog = null;
                    }

                    @Override
                    public void onToWeChat() {
                        RouterActivityPath.LoginProvider.getLoginProvider()
                                .loginWX(null, "抽奖页面");
                    }

                    @Override
                    public void onWeChatReturn() {
                        //读取中台控制自动开始抽奖
                        if (ABSwitch.Ins().isOpenAutoLotteryAfterLoginWx()) {
                            luckyDrawEntrance();
                        }

                    }
                });
                mLogToWeChatDialog.show();
                if (guessAdapter != null && guessAdapter.getCommodityBean() != null) {
                    mLogToWeChatDialog.refreshCoverIcon(guessAdapter.getCommodityBean().getMainPic());
                }
            }
        }
    }

    //生成抽奖码的Dialog
    private void showGenerateCodeDialog() {
        GenerateCodeDialog generateCodeDialog = new GenerateCodeDialog(LotteryActivity.this, mGoodsId);
        generateCodeDialog.setStateListener(new GenerateCodeDialog.OnStateListener() {
            @Override
            public void onFinish() {
                generateCodeDialog.dismiss();
            }

            @Override
            public void onJump(GenerateCodeBean generateCodeBean) {
                //不跳转广告 展示生成的随机抽奖码
                generateCodeDialog.dismiss();

                if (generateCodeBean == null) {
                    Toast.makeText(LotteryActivity.this, "生成抽奖码失败", Toast.LENGTH_SHORT).show();
                    return;
                }

                //刷新页面
                lotteryInfo();
                showExhibitCodeDialog(generateCodeBean);
            }
        });
        generateCodeDialog.create();
        generateCodeDialog.show();

    }


    private void showLessMaxDialog() {
        LessMaxDialog lessMaxDialog = new LessMaxDialog(LotteryActivity.this, mLotteryCodeBean);
        lessMaxDialog.setFinishListener(new LessMaxDialog.OnFinishListener() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onFinishAd() {
                luckyDrawEntrance();
            }
        });
        lessMaxDialog.show();
    }

    //提示还差多少个抽奖码Dialog
    private void showReceiveLotteryDialog(boolean ifQuit) {
        if (mLotteryCodeBean != null) {
            ReceiveLotteryDialog receiveLottery = new ReceiveLotteryDialog(LotteryActivity.this, mLotteryCodeBean, ifQuit);
            receiveLottery.setStateListener(new ReceiveLotteryDialog.OnStateListener() {
                @Override
                public void onFinish() {
                }

                @Override
                public void onJumpAd() {


                }

                @Override
                public void onLottery() {
                    //继续抽奖
                    startLottery();
                }
            });
            receiveLottery.create();
            receiveLottery.show();
        }
    }


    //满足6个时弹起恭喜你 dialog
    private void showCongratulationsDialog() {
        CongratulationsDialog mNoDrawDialog = new CongratulationsDialog(LotteryActivity.this);
        mNoDrawDialog.setFinishListener(new CongratulationsDialog.OnFinishListener() {
            @Override
            public void onFinish() {
                mNoDrawDialog.dismiss();
            }
        });
        mNoDrawDialog.create();
        mNoDrawDialog.show();
    }

    private void finishReturn() {
        if (!mMeedLotteryEvent) {
            return;
        }
        if (mLotteryCodeBean == null || mLotteryCodeBean.getCodes() == null) {
            return;
        }

        EventBus.getDefault().post(new LotteryStatusEvent(mPosition, mGoodsId, mLotteryCodeBean.getCodes()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //展示生成的抽奖码
    private void showExhibitCodeDialog(GenerateCodeBean generateCodeBean) {
        if (generateCodeBean == null) {
            Toast.makeText(LotteryActivity.this, "生成抽奖码失败", Toast.LENGTH_SHORT).show();
            return;
        }
        ExhibitCodeStartsDialog exhibitCodeStartsDialog = new ExhibitCodeStartsDialog(LotteryActivity.this, mGoodsId, generateCodeBean);
        exhibitCodeStartsDialog.setStateListener(new ExhibitCodeStartsDialog.OnStateListener() {
            @Override
            public void onFinish() {
                exhibitCodeStartsDialog.dismiss();
                //判断抽奖码的个数 跳到对于的dialog
                //已经满足6个了
                if (generateCodeBean != null) {
                    if (generateCodeBean.getRemain() == 0) {
                        //抽奖码满足跳转到恭喜dialog
                        showCongratulationsDialog();
                    } else {
                        //显示立刻领取的dialog
                        showReceiveLotteryDialog(false);
                    }
                }
            }

            @Override
            public void onJumpAd() {
            }

            @Override
            public void onLottery() {
                //继续抽奖
                startLottery();
            }
        });
        exhibitCodeStartsDialog.create();
        exhibitCodeStartsDialog.show();

    }


    private void setSmartRefresh() {
        //下拉刷新
        mDataBinding.mailSmRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageNumber = 1;
                refresh = true;
                lotteryInfo();
            }
        });
        //上拉加载
        mDataBinding.mailSmRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageNumber++;
                refresh = false;
                youMayAlsoLike(mPageNumber, false);
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        ARouter.getInstance().inject(this);
        if (mAction != null && mAction.equals("newAction")) {
            clearState();
            //加载抽奖商品详情信息
            lotteryInfo();
        }
    }

    public void setObserveList() {
        //观察商品数据顶部
        mViewModel.getMutableLiveData().observe(this, CommodityBean -> {
            mDataBinding.mailSmRefresh.finishRefresh();
            mDataBinding.mailSmRefresh.setEnableLoadMore(true);
            // 获取数据
            if (CommodityBean == null) {
                return;
            }
            //刷新登录弹框
            refreshLogToWeChatDialog(CommodityBean.getMainPic());
            guessAdapter.setCommodityBean(CommodityBean);
            guessAdapter.notifyDataSetChanged();
            mCommodityBean = CommodityBean;
            //查询 猜你喜欢的数据
            //猜你喜欢
            youMayAlsoLike(mPageNumber, true);
            requestParticipateNumber();
            //抽奖码
            requestLotteryCode(CommodityBean.getPeriod());
            //请求该商品参与的人数用来viewPager 滚动
            winLotteryInfo();

        });
        //观察可能喜欢的数据
        mViewModel.getmGuessLike().observe(this, LotteryBean -> {
            mDataBinding.mailSmRefresh.finishLoadMore(true);
            // 获取数据
            if (LotteryBean == null) {
                return;
            }
            //获取数据刷新列表
            if (LotteryBean.getList() != null) {
                if (refresh) {
                    guessAdapter.setGuessLikeData(LotteryBean.getList());
                    guessAdapter.notifyDataSetChanged();
                } else {
                    guessAdapter.addGuessLikeData(LotteryBean.getList());
                    guessAdapter.notifyDataSetChanged();
                }
            }
        });


        mViewModel.getmParticipateBean().observe(this, MaylikeBean -> {
            // 获取数据
            if (MaylikeBean == null) {
                return;
            }
            //获取数据刷新列表
            if (MaylikeBean.getList() != null) {
                CommodityBean commodityBean = guessAdapter.getCommodityBean();
                commodityBean.setParticipateBean(MaylikeBean);
                guessAdapter.notifyDataSetChanged();
            }
        });
        //抽奖码
        mViewModel.getmLotteryCodeBean().observe(this, MaylikeBean -> {
            // 获取数据
            if (MaylikeBean == null) {
                return;
            }
            mLotteryCodeBean = MaylikeBean;
            //设置底部
            setButtonValue(mLotteryCodeBean);
            //获取数据刷新列表
            if (MaylikeBean.getCodes() != null) {
                CommodityBean commodityBean = guessAdapter.getCommodityBean();
                commodityBean.setLotteryCodeBean(MaylikeBean);
                guessAdapter.notifyDataSetChanged();
            }
        });
//观察商品中奖人数集合
        mViewModel.getWinLotteryBean().observe(this, winLotteryBean -> {
            if (winLotteryBean != null) {
                guessAdapter.setScrollListData(winLotteryBean);
            }
        });


    }


    //设置底部按钮的文案显示
    private void setButtonValue(LotteryCodeBean lotteryCodeBean) {
        mDataBinding.tips.setVisibility(View.VISIBLE);
        //当抽奖码==0 显示0元抽奖
        if (lotteryCodeBean != null && lotteryCodeBean.getCodes().size() == 0) {
            mDataBinding.label02.setVisibility(View.VISIBLE);
            mDataBinding.label01.setText(getResources().getString(R.string.zero_dollar_draw));
            mDataBinding.label02.setText("抽奖");
            mDataBinding.tips.setText("观看视频，参与抽奖");
            setLottieAnimation(true);
        }
        //当抽奖码小于6大于1的话 显示继续抽奖
        if (lotteryCodeBean != null && lotteryCodeBean.getCodes().size() > 0 && lotteryCodeBean.getCodes().size() < 6) {
            mDataBinding.label02.setVisibility(View.VISIBLE);
            mDataBinding.label01.setText(getResources().getString(R.string.continue_value));
            mDataBinding.label02.setText(getResources().getString(R.string.lottery_value));
            mDataBinding.tips.setText("抽奖码越多，中奖概率越大");
            setLottieAnimation(true);
        }
        //当抽奖码大于等于6时显示等待开奖
        if (lotteryCodeBean != null && lotteryCodeBean.getCodes().size() >= 6) {
            mDataBinding.label01.setText(getResources().getString(R.string.wait_dollar_draw));
            mDataBinding.label02.setVisibility(View.GONE);
            mDataBinding.label02.setText("");
            mDataBinding.tips.setText("明日10:00点开奖");
            //
            setLottieAnimation(false);

        }
        final LottieAnimationView mirror = mDataBinding.jsonAnimationRound;
        if (mirror != null) {
            mirror.addLottieOnCompositionLoadedListener(new LottieOnCompositionLoadedListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onCompositionLoaded(LottieComposition composition) {
                    if (composition != null) {
                        setAnimationRoundColor(lotteryCodeBean, mirror);
                    }
                }
            });
        }
    }

    /**
     * 获取商品信息后刷新登录弹框的图片
     */
    private void refreshLogToWeChatDialog(String url) {
        if (mLogToWeChatDialog != null && mLogToWeChatDialog.isShowing()) {
            mLogToWeChatDialog.refreshCoverIcon(url);
        }
    }

    //设置底部按钮颜色
    @SuppressLint("RestrictedApi")
    private void setAnimationRoundColor(LotteryCodeBean lotteryCodeBean, LottieAnimationView mirror) {
        if (lotteryCodeBean == null || mirror == null) {
            return;
        }
        //过滤所有的keypath
        List<KeyPath> list = mirror.resolveKeyPath(
                new KeyPath("**"));
        //通过匹配关键字的深度，来过滤需要改变颜色的keypath
        for (KeyPath path : list) {
            if (path.keysToString().indexOf("btn_inner") != -1) {
                mirror.addValueCallback(path, LottieProperty.COLOR, new SimpleLottieValueCallback<Integer>() {
                    @Override
                    public Integer getValue(LottieFrameInfo<Integer> frameInfo) {
                        if (lotteryCodeBean != null && lotteryCodeBean.getCodes().size() >= 6) {
                            return getResources().getColor(R.color.policec_lick);
                        }
                        return getResources().getColor(R.color.policec_lick_red);
                    }

                });

            }
        }

    }


    @Override
    public void initView() {

    }


    //加载抽奖商品详情信息
    public void lotteryInfo() {
        Map<String, String> params = BaseParams.getMap();
        params.put("goods_id", mGoodsId);
        mViewModel.getNetLotteryData(LotteryModel.LOTTERY_GUESS_INFO, params);
    }


    //加载猜你喜欢的数据
    public void youMayAlsoLike(int pageNumber, boolean refresh) {
        Map<String, String> params = BaseParams.getBaseParams();
        params.put("page_id", pageNumber + "");
        params.put("goods_id", mGoodsId);
        mViewModel.getGuessLikeData(LotteryModel.LOTTERY_GUESS_LIKE, params);
    }


    //请求抽奖累计人数
    public void requestParticipateNumber() {
        Map<String, String> params = BaseParams.getMap();
        params.put("goods_id", mGoodsId);
        mViewModel.getParticipateNumberData(LotteryModel.LOTTERY_PARTICIPATE_NUM, params);
    }

    //请求抽奖码
    public void requestLotteryCode(int numberPeriods) {
        Map<String, String> params = BaseParams.getMap();
        params.put("goods_id", mGoodsId);
        params.put("period", numberPeriods + "");
        mViewModel.getLotteryCodeData(LotteryModel.LOTTERY_LOTTERY_CODE, params);
    }

    //设置列表的顶部
    private void setHeaderView(RecyclerView view) {
        GuesslikeHeadLayoutBinding guesslikeHeadLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.guesslike_head_layout, view, false);
        guessAdapter.setHeaderView(guesslikeHeadLayoutBinding);
    }


    //加载抽奖商品详情中奖列表人员信息
    public void winLotteryInfo() {
        Map<String, String> params = BaseParams.getMap();
        params.put("goods_id", mGoodsId);
        mViewModel.getWinLotteryList(LotteryModel.LOTTERY_WIN_LOTTERY, params);
    }


    private void clearState() {
        mPageNumber = 1;
        refresh = true;
        mLotteryCodeBean = null;
    }


    private void showReturnDialog(int type) {
        dialogShow = true;
        ReturnInterceptDialog mReturnInterceptDialog = new ReturnInterceptDialog(LotteryActivity.this, type);
        mReturnInterceptDialog.setFinishListener(new ReturnInterceptDialog.OnFinishListener() {
            @Override
            public void onDismiss() {
                mReturnInterceptDialog.dismiss();
            }

            @Override
            public void onDismissAd() {
                //根据中台控制 读取返回拦截的配置
                if (ABSwitch.Ins().isOpenAutoLotteryAfterLoginWxAtExitDialog()) {
                    luckyDrawEntrance();
                }
            }
        });
        mReturnInterceptDialog.create();
        mReturnInterceptDialog.show();
        return;
    }


    private void returnIntercept() {
        boolean logType = AppInfo.checkIsWXLogin();
        if (!logType && !dialogShow) {
            //未登录
            //判断抽奖码的数量显示对应的dialog
            showReturnDialog(TYPE_2);
            dialogShow = true;
            return;
        } else {
            if (mLotteryCodeBean == null || dialogShow) {
                finishReturn();
                finish();
                return;
            }
            //当抽奖码小于6个 登录有抽奖
            if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() < 6 && mLotteryCodeBean.getCodes().size() > 0) {
                dialogShow = true;
                //显示立刻领取的dialog
                showLessMaxDialog();
                return;
            }
            if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() == 0) {
                //登录未抽奖
                showReturnDialog(TYPE_1);
                return;
            }

            //当抽奖码大于等于
            if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() >= 6) {
                finishReturn();
                finish();
                return;
            }
        }


    }


    @SuppressLint("ResourceType")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnIntercept();
        }
        return true;
    }

}