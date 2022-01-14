package com.module.lottery.ui;

import static com.donews.common.config.CritParameterConfig.CRIT_STATE;
import static com.module.lottery.dialog.ReturnInterceptDialog.TYPE_1;
import static com.module.lottery.dialog.ReturnInterceptDialog.TYPE_2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
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
import com.dn.events.events.LotteryStatusEvent;
import com.dn.sdk.bean.integral.IntegralBean;
import com.dn.sdk.bean.integral.ProxyIntegral;
import com.donews.base.utils.ToastUtil;
import com.donews.common.ad.business.manager.JddAdManager;
import com.donews.common.ad.business.monitor.LotteryAdCount;
import com.donews.common.bean.CritMessengerBean;
import com.donews.common.provider.IDetailProvider;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.middle.bean.RedEnvelopeUnlockBean;
import com.donews.middle.utils.CommonUtils;
import com.donews.middle.utils.CriticalModelTool;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.DateManager;
import com.donews.utilslibrary.utils.SPUtils;
import com.module.lottery.adapter.GuessAdapter;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.CritCodeBean;
import com.module.lottery.bean.GenerateCodeBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.dialog.CongratulationsDialog;
import com.module.lottery.dialog.EnvelopeStateDialog;
import com.module.lottery.dialog.ExclusiveLotteryCodeDialog;
import com.module.lottery.dialog.ExhibitCodeStartsDialog;
import com.module.lottery.dialog.GenerateCodeDialog;
import com.module.lottery.dialog.LessMaxDialog;
import com.module.lottery.dialog.LogToWeChatDialog;
import com.module.lottery.dialog.LotteryCodeStartsDialog;
import com.module.lottery.dialog.LotteryCritCodeDialog;
import com.module.lottery.dialog.LotteryCritCommodityDialog;
import com.module.lottery.dialog.LotteryCritOverDialog;
import com.module.lottery.dialog.ReceiveLotteryDialog;
import com.module.lottery.dialog.ReturnInterceptDialog;
import com.module.lottery.dialog.UnlockMaxCodeDialog;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.utils.ClickDoubleUtil;
import com.module.lottery.utils.GridSpaceItemDecoration;
import com.module.lottery.utils.PlayAdUtilsTool;
import com.module.lottery.viewModel.LotteryViewModel;
import com.module_lottery.R;
import com.module_lottery.databinding.GuesslikeHeadLayoutBinding;
import com.module_lottery.databinding.LotteryMainLayoutBinding;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;

/**
 * 抽奖页面
 *
 * @author hegai
 * @version v1.0
 * @date 2021/12/8
 */
@Route(path = RouterFragmentPath.Lottery.PAGER_LOTTERY)
public class LotteryActivity extends BaseActivity<LotteryMainLayoutBinding, LotteryViewModel> {
    private static final String TAG = "LotteryActivity";
    private static final String LOTTERY_ACTIVITY = "LOTTERY_ACTIVITY";
    private static final String FIRST_SHOW = "first_show";
    private static final String LOTTERY_FINGER = "lottery_finger.json";
    private static final String LOTTERY_ROUND = "lottery_round.json";
    private static final String CRIT_ROUND = "cruel_time.json";
    private static final String CRITICAL_BT_TITLE_0 = "暴击";
    private static final String CRITICAL_BT_TITLE_1 = "抽奖";
    private static final String CRITICAL_BT_TITLE_2 = "超级幸运：中奖率X6";
    private static final String CRITICAL_BT_TITLE_3 = "观看视频，参与抽奖";
    private static final String CRITICAL_BT_TITLE_4 = "抽奖码越多，中奖概率越大";
    private static final String CRITICAL_BT_TITLE_5 = "0元";
    @Autowired(name = "goods_id")
    public String mGoodsId;
    private SharedPreferences mSharedPreferences;
    // 是否自动开始抽奖 true 进入页面自动开始 false  受中台控制
    @Autowired(name = "start_lottery")
    public boolean mStart_lottery = false;

    //特权自动抽奖 不受中台控制
    @Autowired(name = "privilege")
    public boolean privilege = false;


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
    PlayAdUtilsTool mPlayAdUtilsTool;


    /**
     * 用来处理暴击时间结束后，在暴击时间内，查看视频，看完广告，当次暴击事件失效
     */
    private boolean mCritTime;

    @Override
    protected int getLayoutId() {
        return R.layout.lottery_main_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = this.getSharedPreferences(LOTTERY_ACTIVITY, 0);
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);
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
        mPlayAdUtilsTool = new PlayAdUtilsTool(LotteryActivity.this);
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
        if (mStart_lottery && ABSwitch.Ins().isOpenAutoLottery() || privilege) {
            ifOpenAutoLotteryAndCount();
        }
        mStart_lottery = false;
//        Message mes = new Message();
//        handler.sendMessageDelayed(mes, 2000);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            showEnvelopeStateDialog();
        }
    };


    private void showLotteryCritCodeDialog(GenerateCodeBean generateCodeBean) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LotteryCritCodeDialog lotteryCritDialog = new LotteryCritCodeDialog(LotteryActivity.this, generateCodeBean);
                lotteryCritDialog.setStateListener(new LotteryCritCodeDialog.OnStateListener() {
                    @Override
                    public void onStartCrit() {
                        if (lotteryCritDialog != null && lotteryCritDialog.isShowing()) {
                            lotteryCritDialog.dismiss();
                        }
                        luckyDrawEntrance();
                    }
                });
                lotteryCritDialog.show(LotteryActivity.this);
            }
        });
    }


    private void showLotteryCritCommodityDialog() {
        LotteryCritCommodityDialog lotteryCritDialog = new LotteryCritCommodityDialog(mGoodsId, this);
        lotteryCritDialog.setOwnerActivity(this);
        lotteryCritDialog.show(this);
    }


    private void showLotteryCritOverDialog() {
        LotteryCritOverDialog lotteryCritDialog = new LotteryCritOverDialog(mGoodsId, this);

        lotteryCritDialog.setStateListener(new LotteryCritOverDialog.OnStateListener() {
            @Override
            public void onFinish() {
                if (lotteryCritDialog != null && lotteryCritDialog.isShowing()) {
                    lotteryCritDialog.dismiss();
                }
            }

            @Override
            public void onCritJump(CritCodeBean critCodeBean) {
                //刷新页面  展示抽奖码
                lotteryInfo();
                showUnlockMaxCodeDialog(critCodeBean);
            }
        });

        lotteryCritDialog.setOwnerActivity(this);
        lotteryCritDialog.show(this);
    }


    private void showUnlockMaxCodeDialog(CritCodeBean critCodeBean) {
        UnlockMaxCodeDialog UnlockMaxCodeDialog = new UnlockMaxCodeDialog(mLotteryCodeBean, critCodeBean, mGoodsId, this);
        UnlockMaxCodeDialog.setOwnerActivity(this);
        UnlockMaxCodeDialog.show(this);

    }

    /**
     * 判断是否需要自动抽奖,并且是否大于了中台控制次数
     */
    private void ifOpenAutoLotteryAndCount() {
        if (privilege) {
            luckyDrawEntrance();
            return;
        }
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
        if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() >= 6) {
            return;
        }
        boolean logType = AppInfo.checkIsWXLogin();
        //走中台配置流程
        //0没有登录需要先登录
        if (ABSwitch.Ins().getLotteryLine() == 0 || logType) {
            if (logType) {
                startLottery();
            } else {
                showLogToWeChatDialog();
            }
        } else {
            if (ABSwitch.Ins().getLotteryLine() == 1) {
                //可以直接开始抽奖
                startLottery();
            }
        }
    }

    RotateAnimation mRotateAnimation;

    private void initViewData() {

        //设置 抽奖按钮上的tips 动画
        if (mRotateAnimation == null) {
            mRotateAnimation = new RotateAnimation(0, 8, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                    0.5f);
            mRotateAnimation.setInterpolator(new CycleInterpolator(2));
            mRotateAnimation.setRepeatMode(Animation.REVERSE);
            mRotateAnimation.setRepeatCount(Animation.INFINITE);
            mRotateAnimation.setStartOffset(2500);
            mRotateAnimation.setRepeatMode(Animation.REVERSE);
            mRotateAnimation.setDuration(400);
        }
        mDataBinding.lotteryTips.setAnimation(mRotateAnimation);
        mDataBinding.jsonAnimationRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickDoubleUtil.isFastClick()) {
                    luckyDrawEntrance();
                }
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
                if (mCommodityBean != null && mCommodityBean.getItemLink() != null) {
                    if (mCommodityBean.getItemLink().equals("")) {
                        ToastUtil.showShort(getApplicationContext(), "该商品暂不支持购买");
                    } else {
                        detailProvider.goToTaoBao(LotteryActivity.this, mCommodityBean.getItemLink());
                    }
                } else {
                    ToastUtil.showShort(getApplicationContext(), "该商品暂不支持购买");
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
        if (first_show && !privilege && !CriticalModelTool.ifCriticalStrike()) {
            mSharedPreferences.edit().putBoolean(FIRST_SHOW, false).apply();
            mDataBinding.maskingLayout.setVisibility(View.VISIBLE);
            //圆 新手引导遮罩层
            initLottie(mDataBinding.maskingButton, "lottery_round.json");
            //小手 新手引导遮罩层
            initLottie(mDataBinding.maskingHand, "lottery_finger.json");
        } else {
            mDataBinding.maskingLayout.setVisibility(View.GONE);
            mSharedPreferences.edit().putBoolean(FIRST_SHOW, false).apply();
        }
    }

    private void setLottieAnimation(boolean play) {
        if (play) {
            //设置动画
            //小手
            initLottie(mDataBinding.jsonAnimation, "lottery_finger.json");
            mDataBinding.jsonAnimation.setVisibility(View.VISIBLE);
            //圆
            if (CriticalModelTool.ifCriticalStrike()) {
                mDataBinding.tips.setText(CRITICAL_BT_TITLE_2);
                mDataBinding.label01.setText(CRITICAL_BT_TITLE_0);
                mDataBinding.jsonAnimationRound.clearAnimation();
                initLottie(mDataBinding.jsonAnimationRound, CRIT_ROUND);
            } else {
                mDataBinding.tips.setText(CRITICAL_BT_TITLE_4);
                initLottie(mDataBinding.jsonAnimationRound, LOTTERY_ROUND);
            }

        } else {
            mDataBinding.jsonAnimation.setVisibility(View.GONE);
            mDataBinding.jsonAnimationRound.pauseAnimation();
            mDataBinding.jsonAnimationRound.setProgress(0);
        }
    }

    private void initLottie(LottieAnimationView view, String json) {
        if ((view != null && !view.isAnimating())) {
            view.setImageAssetsFolder("images");
            view.clearAnimation();
            view.setAnimation(json);
            view.loop(true);
            view.playAnimation();
        }
    }

    //开始抽奖
    private void startLottery() {
        AnalysisUtils.onEventEx(this, Dot.Btn_LotteryNow);
        //判断是否打开了视频广告
        boolean isOpenAd = JddAdManager.INSTANCE.isOpenAd();
        if (isOpenAd) {
            if (CriticalModelTool.ifCriticalStrike()) {
                //当次事件有效
                mCritTime = true;
            } else {
                //当次暴击事件无效
                mCritTime = false;
            }

            //开始抽奖
            //弹框抽奖码生成dialog
            LotteryCodeStartsDialog lotteryCodeStartsDialog = new LotteryCodeStartsDialog(LotteryActivity.this);
            lotteryCodeStartsDialog.setStateListener(new LotteryCodeStartsDialog.OnStateListener() {
                @Override
                public void onLoadAd() {
                    loadAdAndStatusCallback(lotteryCodeStartsDialog);
                }
            });
            lotteryCodeStartsDialog.create();
            lotteryCodeStartsDialog.show(LotteryActivity.this);
        } else {
            //判断是否支持抽奖
            if (DateManager.getInstance().timesLimit(DateManager.LOTTERY_KEY, DateManager.NUMBER_OF_DRAWS, 24)) {
                generateLotteryCode();
            } else {
                ToastUtil.showShort(this, "今天次数已用完，明日再来");
            }
        }

    }


    /**
     * 用来加载并显示广告和接收广告状态的回调
     */

    private void loadAdAndStatusCallback(final Dialog dialog) {
        if (mPlayAdUtilsTool != null) {
            mPlayAdUtilsTool.showRewardVideo(dialog);
            mPlayAdUtilsTool.setIStateListener(new PlayAdUtilsTool.IStateListener() {
                @Override
                public void onComplete() {
                    //广告有效播放完成可以显示抽奖码的弹框
                    Logger.d(TAG + "showGenerateCodeDialog");
                    if (ABSwitch.Ins().getOpenCritModel()) {
                        //判断是否是第0次
                        int number = LotteryAdCount.INSTANCE.getCriticalModelLotteryNumber();
                        if (number == 0) {
                            Logger.d(TAG + "每轮首次抽奖回来，判断是否显示积分");
                            //判断当前是否有积分逻辑
                            if (ABSwitch.Ins().isOpenScoreModelCrit() && !CriticalModelTool.isNewUser()) {
                                CriticalModelTool.getScenesSwitch(new CriticalModelTool.IScenesSwitchListener() {
                                    @Override
                                    public void onIntegralNumber(ProxyIntegral integralBean) {
                                        if (integralBean == null) {
                                            //暴击模式抽奖次数加一
                                            addCriticalLotteryNumber();
                                            Logger.d(TAG + "普通模式次数加一 无积分任务");
                                        } else {
                                            Logger.d(TAG + "现在是积分任务模式");
                                            LotteryAdCount.INSTANCE.resetCriticalModelNumber();
                                        }

                                    }
                                });
                            } else if (CriticalModelTool.isNewUser()) {
                                addCriticalLotteryNumber();
                            }
                        } else {
                            //暴击模式抽奖次数加一
                            Logger.d(TAG + "普通模式次数加一 未开启积分情况下");
                            addCriticalLotteryNumber();
                        }
                    }
                    generateLotteryCode();
                }
            });
        }
    }

    private void addCriticalLotteryNumber() {
        boolean logType = AppInfo.checkIsWXLogin();
        if (SPUtils.getInformain(CRIT_STATE, 0) == 0 && logType && !mCritTime && ABSwitch.Ins().getOpenCritModel() && DateManager.getInstance().isAllowCritical()) {
            LotteryAdCount.INSTANCE.putCriticalModelLotteryNumber();
        }
    }

    //显示专属抽奖码弹框
    private void showExclusiveCodeDialog() {
        ExclusiveLotteryCodeDialog exclusiveLotteryCodeDialog = new ExclusiveLotteryCodeDialog(LotteryActivity.this, mGoodsId);
        exclusiveLotteryCodeDialog.setFinishListener(new ExclusiveLotteryCodeDialog.OnFinishListener() {
            @Override
            public void onLoginSuccessful(GenerateCodeBean generateCode, boolean efficient) {
                if (efficient && generateCode != null) {
                    showExhibitCodeDialog(generateCode);
                }
            }


            @Override
            public void onLoginUploadSuccessful() {
                //加载抽奖商品详情信息
                //刷新
                lotteryInfo();
            }

            @Override
            public void closure() {
                if (exclusiveLotteryCodeDialog != null && exclusiveLotteryCodeDialog.isShowing()) {
                    exclusiveLotteryCodeDialog.dismiss();
                }
            }
        });
        exclusiveLotteryCodeDialog.show();
    }


//    boolean logType = AppInfo.checkIsWXLogin();
//                    if (ABSwitch.Ins().getLotteryLine() == 0 && logType) {
//
//    } else {
//        //显示专属抽奖码弹框
//        showExclusiveCodeDialog();
//
//    }
//


    private void showLogToWeChatDialog() {
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
                            .loginWX(null, "抽奖页>抽奖按钮>未登录弹窗");
                }

                @Override
                public void onWeChatReturn() {
                    //登录成功 刷新页面
                    clearState();
                    //加载抽奖商品详情信息
                    lotteryInfo();
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


    //生成抽奖码
    private void generateLotteryCode() {
        if (mCritTime) {
            //弹起暴击模式的抽奖码
            showLotteryCritOverDialog();
        } else {
            showGenerateCodeDialog();
        }

    }


    //生成抽奖码的Dialog
    private void showGenerateCodeDialog() {
        try {
            GenerateCodeDialog generateCodeDialog = new GenerateCodeDialog(LotteryActivity.this, mGoodsId);
            generateCodeDialog.setStateListener(new GenerateCodeDialog.OnStateListener() {
                @Override
                public void onFinish() {
                    privilege = false;
                    if (generateCodeDialog != null && !LotteryActivity.this.isFinishing()) {
                        generateCodeDialog.dismiss();
                    }
                }

                @Override
                public void onJump(GenerateCodeBean generateCodeBean) {
                    if (generateCodeBean == null) {
                        privilege = false;
                        AnalysisUtils.onEventEx(LotteryActivity.this, Dot.PAY_FAIL);
                        Toast.makeText(LotteryActivity.this, "生成抽奖码失败", Toast.LENGTH_SHORT).show();
                        //不跳转广告 展示生成的随机抽奖码
                        if (generateCodeDialog != null && !LotteryActivity.this.isFinishing()) {
                            generateCodeDialog.dismiss();
                        }
                        return;
                    }
                    //刷新页面  展示抽奖码
                    lotteryInfo();
                    //判断是否在进行积分任务
                    if (ABSwitch.Ins().getOpenCritModel()) {
                        //暴击次数判断
                        if (DateManager.getInstance().isAllowCritical()) {
                            taskJudgment(generateCodeBean, generateCodeDialog);
                        } else {
                            //不满足了走普通流程
                            ordinaryTask(generateCodeBean, generateCodeDialog);
                        }
                    } else {
                        //未开启走普通流程
                        ordinaryTask(generateCodeBean, generateCodeDialog);
                    }
                }

                @Override
                public void onExclusiveBulletFrame() {
                    if (generateCodeDialog != null && !LotteryActivity.this.isFinishing()) {
                        generateCodeDialog.dismiss();

                    }
                    showExclusiveCodeDialog();
                }

            });
            generateCodeDialog.create();
            if (!LotteryActivity.this.isFinishing()) {
                generateCodeDialog.show(LotteryActivity.this);
            }
        } catch (Exception e) {
            Logger.e("" + e.getMessage());
        }
    }


    /**
     * 判断是否进行暴击弹框判断
     */
    private void taskJudgment(GenerateCodeBean generateCodeBean, GenerateCodeDialog generateCodeDialog) {

        //判断当前是否有积分逻辑
        if (ABSwitch.Ins().isOpenScoreModelCrit() && !CriticalModelTool.isNewUser()) {
            //开启了下载积分模式
            CriticalModelTool.getScenesSwitch(new CriticalModelTool.IScenesSwitchListener() {
                @Override
                public void onIntegralNumber(ProxyIntegral integralBean) {
                    int number = LotteryAdCount.INSTANCE.getCriticalModelLotteryNumber();
                    //未开启
                    //不跳转广告 展示生成的随机抽奖码
                    if (generateCodeDialog != null && !LotteryActivity.this.isFinishing()) {
                        generateCodeDialog.dismiss();
                    }
                    if (integralBean != null && number == 0) {
                        //存在积分任务(走下载逻辑)
                        showExhibitCodeDialog(generateCodeBean);
                    } else {
                        ordinaryTask(generateCodeBean, generateCodeDialog);
                    }
                }
            });
        } else {
            //未开启
            //不跳转广告 展示生成的随机抽奖码
            if (generateCodeDialog != null && !LotteryActivity.this.isFinishing()) {
                generateCodeDialog.dismiss();
            }
            ordinaryTask(generateCodeBean, generateCodeDialog);
        }
    }


    //进行普通任务
    private void ordinaryTask(GenerateCodeBean generateCodeBean, GenerateCodeDialog generateCodeDialog) {
        //不跳转广告 展示生成的随机抽奖码
        if (generateCodeDialog != null && !LotteryActivity.this.isFinishing()) {
            generateCodeDialog.dismiss();
        }
        //判断是否需要弹起 超幸运弹框
        if (CriticalModelTool.ifCoincide()) {
            //通知开始暴击模式
            startCriticalMoment();
            //弹起超幸运弹框
            if (generateCodeBean.getRemain() == 0) {
                //满了6个后先弹出之前的弹框
                showExhibitCodeDialog(generateCodeBean);
            } else {
                //普通的暴击抽奖
                showLotteryCritCodeDialog(generateCodeBean);
            }
        } else {
            showExhibitCodeDialog(generateCodeBean);
        }

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
        lessMaxDialog.show(LotteryActivity.this);
    }

    //提示还差多少个抽奖码Dialog
    private void showReceiveLotteryDialog(boolean ifQuit) {
        if (mLotteryCodeBean != null) {
            ReceiveLotteryDialog receiveLottery = new ReceiveLotteryDialog(LotteryActivity.this, mLotteryCodeBean,
                    ifQuit);
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
            receiveLottery.show(LotteryActivity.this);
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
        mNoDrawDialog.show(LotteryActivity.this);
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
        EventBus.getDefault().unregister(this);
        mDataBinding.lotteryTips.clearAnimation();
        mCritTime = false;
        if (mPlayAdUtilsTool != null) {
            mPlayAdUtilsTool.setIStateListener(null);
            mPlayAdUtilsTool = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //
    @Override
    protected void onPause() {
        super.onPause();
    }

    //展示生成的抽奖码
    private void showExhibitCodeDialog(GenerateCodeBean generateCodeBean) {
        if (generateCodeBean == null) {
            privilege = false;
            Toast.makeText(LotteryActivity.this, "生成抽奖码失败", Toast.LENGTH_SHORT).show();
            return;
        }
        ExhibitCodeStartsDialog exhibitCodeStartsDialog = new ExhibitCodeStartsDialog(LotteryActivity.this, mGoodsId,
                generateCodeBean);
        exhibitCodeStartsDialog.setOwnerActivity(LotteryActivity.this);
        exhibitCodeStartsDialog.setStateListener(new ExhibitCodeStartsDialog.OnStateListener() {
            @Override
            public void onFinish() {
                exhibitCodeStartsDialog.dismiss();
                //判断抽奖码的个数 跳到对于的dialog
                //已经满足6个了
                if (generateCodeBean != null) {
                    if (generateCodeBean.getRemain() == 0) {
                        if (mCritTime) {
                            showLotteryCritCommodityDialog();
                        } else {
                            //抽奖码满足跳转到恭喜dialog
                            showCongratulationsDialog();
                        }
                    } else {
                        //显示立刻领取的dialog
                        showReceiveLotteryDialog(false);
                    }
                }
            }

            @Override
            public void onLottery() {
                //继续抽奖
                startLottery();
            }

            @Override
            public void onChangeState() {
                mCritTime = true;
            }

            @Override
            public void onStartCritMode(GenerateCodeBean generateCodeBean, ProxyIntegral integralBean) {
                if (ClickDoubleUtil.isFastClick()) {
                    CommonUtils.startCritService(LotteryActivity.this, integralBean);
                }
            }
        });
        try {
            exhibitCodeStartsDialog.create();
            if (!this.isFinishing() && !this.isDestroyed()) {
                if (privilege) {
                    Toast.makeText(LotteryActivity.this, "红包已解锁", Toast.LENGTH_SHORT).show();
                    //通知首页更新
                    EventBus.getDefault().post(new RedEnvelopeUnlockBean(200));
                    privilege = false;
                }
                exhibitCodeStartsDialog.show(LotteryActivity.this);
            }
        } catch (Exception ignored) {
        }
    }


    /**
     * 开启暴击时刻统一入口
     */
    private void startCriticalMoment() {
        CommonUtils.startCrit();
    }


    private void setSmartRefresh() {
        //下拉刷新
        mDataBinding.mailSmRefresh.setOnRefreshListener(refreshLayout -> {
            mPageNumber = 1;
            refresh = true;
            lotteryInfo();
        });
        //上拉加载
        mDataBinding.mailSmRefresh.setOnLoadMoreListener(refreshLayout -> {
            mPageNumber++;
            refresh = false;
            youMayAlsoLike(mPageNumber, false);
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        privilege = false;
        mCritTime = false;
        setIntent(intent);
        ARouter.getInstance().inject(this);
        if (mAction != null && mAction.equals("newAction")) {
            clearState();
            //加载抽奖商品详情信息
            lotteryInfo();
        }
    }


    private void refreshCoupon(CommodityBean commodityBean) {
        if (commodityBean.getItemLink() != null) {
            if (commodityBean.getItemLink().equals("")) {
                mDataBinding.buyCopywritingIcon.setVisibility(View.VISIBLE);
                mDataBinding.buyCopywriting.setText("购买");
                mDataBinding.couponLayout.setVisibility(View.GONE);
            } else if (commodityBean.getCoupon_price() > 0) {
                mDataBinding.buyCopywriting.setText("领券购");
                mDataBinding.couponLayout.setVisibility(View.VISIBLE);
                mDataBinding.discountedPrice.setText(commodityBean.getCoupon_price() + "");
                mDataBinding.buyCopywritingIcon.setVisibility(View.GONE);

            } else {
                mDataBinding.buyCopywritingIcon.setVisibility(View.VISIBLE);
                mDataBinding.buyCopywriting.setText("购买");
                mDataBinding.couponLayout.setVisibility(View.GONE);
            }
        } else {
            mDataBinding.buyCopywritingIcon.setVisibility(View.VISIBLE);
            mDataBinding.buyCopywriting.setText("购买");
            mDataBinding.couponLayout.setVisibility(View.GONE);
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
            //刷新优惠券按钮展示
            refreshCoupon(CommodityBean);
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
                } else {
                    guessAdapter.addGuessLikeData(LotteryBean.getList());
                }
                guessAdapter.notifyDataSetChanged();
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
            //设置底部3+
            setButtonValue(mLotteryCodeBean);
            if (mLotteryCodeBean.getCodes() != null && mLotteryCodeBean.getCodes().size() >= 6) {
                ToastUtil.showShort(getApplicationContext(), "“已获得最大中奖率，换个商品吧”");
            }
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


    @Subscribe
    public void UnlockEvent(CritMessengerBean critMessenger) {
        //暴击结束
        if (critMessenger != null && critMessenger.mStatus == 300 || critMessenger.mStatus == 200) {
            if (mLotteryCodeBean != null) {
                mDataBinding.jsonAnimationRound.pauseAnimation();
                mDataBinding.jsonAnimationRound.clearAnimation();
                setButtonValue(mLotteryCodeBean);
            }
        }
    }


    //设置底部按钮的文案显示
    private void setButtonValue(LotteryCodeBean lotteryCodeBean) {
        mDataBinding.tips.setVisibility(View.VISIBLE);
        //当抽奖码==0 显示0元抽奖
        if (lotteryCodeBean != null && lotteryCodeBean.getCodes().size() == 0) {
            mDataBinding.label02.setVisibility(View.VISIBLE);
            if (CriticalModelTool.ifCriticalStrike()) {
                mDataBinding.label01.setText(CRITICAL_BT_TITLE_0);
                mDataBinding.label02.setText(CRITICAL_BT_TITLE_1);
                mDataBinding.tips.setText(CRITICAL_BT_TITLE_2);
            } else {
                mDataBinding.label01.setText(CRITICAL_BT_TITLE_5);
                mDataBinding.label02.setText(CRITICAL_BT_TITLE_1);
                mDataBinding.tips.setText(CRITICAL_BT_TITLE_3);
            }
            setLottieAnimation(true);
        } else
            //当抽奖码小于6大于1的话 显示继续抽奖
            if (lotteryCodeBean != null && lotteryCodeBean.getCodes().size() > 0 && lotteryCodeBean.getCodes().size() < 6) {
                mDataBinding.label02.setVisibility(View.VISIBLE);
                if (CriticalModelTool.ifCriticalStrike()) {
                    mDataBinding.label01.setText(CRITICAL_BT_TITLE_0);
                    mDataBinding.label02.setText(CRITICAL_BT_TITLE_1);
                    mDataBinding.tips.setText(CRITICAL_BT_TITLE_2);
                } else {
                    mDataBinding.label01.setText(getResources().getString(R.string.continue_value));
                    mDataBinding.label02.setText(getResources().getString(R.string.lottery_value));
                    mDataBinding.tips.setText(CRITICAL_BT_TITLE_4);
                }
                setLottieAnimation(true);
            } else
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
        GuesslikeHeadLayoutBinding guesslikeHeadLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.guesslike_head_layout, view, false);
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
                if (mReturnInterceptDialog.isShowing()) {
                    mReturnInterceptDialog.dismiss();
                }
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
        mReturnInterceptDialog.show(LotteryActivity.this);
        return;
    }


    private void showEnvelopeStateDialog() {
        dialogShow = true;
        EnvelopeStateDialog envelopeStateDialog = new EnvelopeStateDialog(LotteryActivity.this);
        envelopeStateDialog.setFinishListener(new EnvelopeStateDialog.OnFinishListener() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onFinishAd() {

            }
        });
        envelopeStateDialog.create();
        envelopeStateDialog.show(LotteryActivity.this);
        return;
    }


    private void returnIntercept() {
        boolean logType = AppInfo.checkIsWXLogin();
        if (!logType && !dialogShow) {
            //未登录
            //判断抽奖码的数量显示对应的dialog
            showReturnDialog(TYPE_1);
            dialogShow = true;
            return;
        } else {
            if (CriticalModelTool.ifCriticalStrike()) {
                finishReturn();
                finish();
                return;
            }
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
                if (CommonUtils.isAllRpOpened()) {
                    //首页红包开完
                    showEnvelopeStateDialog();
                }else{
                    showReturnDialog(TYPE_2);
                }
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