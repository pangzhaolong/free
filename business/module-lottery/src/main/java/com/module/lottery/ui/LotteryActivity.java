package com.module.lottery.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.dn.events.events.LotteryStatusEvent;
import com.dn.sdk.sdk.interfaces.listener.impl.SimpleRewardVideoListener;
import com.donews.common.ad.business.loader.AdManager;
import com.donews.common.provider.IDetailProvider;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
import com.module.lottery.adapter.GuessAdapter;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.GenerateCodeBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.dialog.CongratulationsDialog;
import com.module.lottery.dialog.ExhibitCodeStartsDialog;
import com.module.lottery.dialog.GenerateCodeDialog;
import com.module.lottery.dialog.LessMaxDialog;
import com.module.lottery.dialog.LotteryCodeStartsDialog;
import com.module.lottery.dialog.NoDrawDialog;
import com.module.lottery.dialog.ReceiveLotteryDialog;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.utils.GridSpaceItemDecoration;
import com.module.lottery.viewModel.LotteryViewModel;
import com.module_lottery.R;
import com.module_lottery.databinding.GuesslikeHeadLayoutBinding;
import com.module_lottery.databinding.LotteryMainLayoutBinding;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;


@Route(path = RouterFragmentPath.Lottery.PAGER_LOTTERY)
public class LotteryActivity extends BaseActivity<LotteryMainLayoutBinding, LotteryViewModel> {
    //    @Autowired(name = "goodsListDTO")
//    SpikeBean.GoodsListDTO mGoodsListDTO;
    @Autowired(name = "goods_id")
    String mGoodsId;
    @Autowired(name = "position")
    int mPosition;
    @Autowired(name = "needLotteryEvent")
    boolean mMeedLotteryEvent;
    //    String id = "tb:655412572200";
    GuessAdapter guessAdapter;
    @Autowired(name = "action")
    String mAction;
    CommodityBean mCommodityBean;

    int mPageNumber = 1;
    boolean refresh = true;
    //抽奖码的对象用来拦截返回
    LotteryCodeBean mLotteryCodeBean;

    boolean dialogShow = false;
    @Autowired(name = RouterActivityPath.GoodsDetail.GOODS_DETAIL_PROVIDER)
    IDetailProvider detailProvider;

    @Override
    protected int getLayoutId() {
        return R.layout.lottery_main_layout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        initViewData();
        setHeaderView(mDataBinding.lotteryGridview);
        setObserveList();
        //加载抽奖商品详情信息
        lotteryInfo();
        //设置下拉，和上拉的监听
        setSmartRefresh();
    }


    public void luckyDrawEntrance() {
        boolean logType = AppInfo.checkIsWXLogin();
        if (logType) {
            if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() >= 6) {
                return;
            }
            startLottery();
        } else {
            ARouter.getInstance()
                    .build(RouterActivityPath.User.PAGER_LOGIN)
                    .navigation();
        }
    }


    private void initViewData() {
        mDataBinding.lotteryButton.setOnClickListener(new View.OnClickListener() {
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
            mDataBinding.jsonAnimation.setVisibility(View.GONE);
            mDataBinding.jsonAnimationHalo.pauseAnimation();
            mDataBinding.jsonAnimationHalo.setProgress(0);
        } else {
            //手
            mDataBinding.jsonAnimation.setImageAssetsFolder("images");
            mDataBinding.jsonAnimation.setAnimation("lottery_finger.json");
            mDataBinding.jsonAnimation.loop(true);
            mDataBinding.jsonAnimation.playAnimation();

//圆
            mDataBinding.jsonAnimationHalo.setImageAssetsFolder("images");
            mDataBinding.jsonAnimationHalo.setAnimation("data.json");
            mDataBinding.jsonAnimationHalo.loop(true);
            mDataBinding.jsonAnimationHalo.playAnimation();


        }

    }


    //开始抽奖
    private void startLottery() {
        AnalysisUtils.onEventEx(this, Dot.Btn_LotteryNow);
        //开始抽奖
        //弹框抽奖码生成dialog
        LotteryCodeStartsDialog lotteryCodeStartsDialog = new LotteryCodeStartsDialog(LotteryActivity.this);
        lotteryCodeStartsDialog.setStateListener(new LotteryCodeStartsDialog.OnStateListener() {
            @Override
            public void onFinish() {
                try {
                    if (lotteryCodeStartsDialog != null) {
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


    //提示还差多少个抽奖码Dialog
    private void showReceiveLotteryDialog() {
        ReceiveLotteryDialog receiveLottery = new ReceiveLotteryDialog(LotteryActivity.this, mLotteryCodeBean);
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
        if (mLotteryCodeBean == null) {
            return;
        }
        ExhibitCodeStartsDialog exhibitCodeStartsDialog = new ExhibitCodeStartsDialog(LotteryActivity.this, mGoodsId, mLotteryCodeBean, generateCodeBean);
        exhibitCodeStartsDialog.setStateListener(new ExhibitCodeStartsDialog.OnStateListener() {
            @Override
            public void onFinish() {
                exhibitCodeStartsDialog.dismiss();
                //判断抽奖码的个数 跳到对于的dialog
                //已经满足6个了
                if (mLotteryCodeBean != null) {
                    if (mLotteryCodeBean.getCodes().size() >= 6) {
                        //抽奖码满足跳转到恭喜dialog
                        showCongratulationsDialog();
                    } else {
                        //显示立刻领取的dialog
                        showReceiveLotteryDialog();
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
            guessAdapter.setCommodityBean(CommodityBean);
            guessAdapter.notifyDataSetChanged();
            mCommodityBean = CommodityBean;
            //查询 猜你喜欢的数据
            //猜你喜欢
            youMayAlsoLike(mPageNumber, true);
            requestParticipateNumber();
            //抽奖码
            requestLotteryCode(CommodityBean.getPeriod());


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

    }


    //设置底部按钮的文案显示
    private void setButtonValue(LotteryCodeBean lotteryCodeBean) {
        //判断用户是否登录，未登录走登录流程

        mDataBinding.tips.setVisibility(View.VISIBLE);
        //当抽奖码==0 显示0元抽奖
        if (lotteryCodeBean != null && lotteryCodeBean.getCodes().size() == 0) {
            mDataBinding.label02.setVisibility(View.VISIBLE);
            mDataBinding.label01.setText(getResources().getString(R.string.zero_dollar_draw));
            mDataBinding.label02.setText(getResources().getString(R.string.lottery_value));
            mDataBinding.tips.setText("观看视频，参与抽奖");
        }


        //当抽奖码小于6大于1的话 显示继续抽奖
        if (lotteryCodeBean != null && lotteryCodeBean.getCodes().size() > 0 && lotteryCodeBean.getCodes().size() < 6) {
            mDataBinding.label02.setVisibility(View.VISIBLE);
            mDataBinding.label01.setText(getResources().getString(R.string.continue_value));
            mDataBinding.label02.setText(getResources().getString(R.string.lottery_value));
            mDataBinding.tips.setText("抽奖码越多，中奖概率越大");

        }
        //当抽奖码大于等于6时显示等待开奖
        if (lotteryCodeBean != null && lotteryCodeBean.getCodes().size() >= 6) {
            mDataBinding.label01.setText(getResources().getString(R.string.wait_dollar_draw));
            mDataBinding.label02.setVisibility(View.GONE);
            mDataBinding.label02.setText("");
            mDataBinding.tips.setText("明日10:00点开奖");
            //
            mDataBinding.jsonAnimation.setVisibility(View.GONE);
            mDataBinding.jsonAnimationHalo.pauseAnimation();
            mDataBinding.jsonAnimationHalo.setProgress(0);


            mDataBinding.jsonAnimationHalo.setColorFilter(Color.BLUE);

            LottieAnimationView mirror = mDataBinding.jsonAnimationHalo;
            mirror.addLottieOnCompositionLoadedListener(new LottieOnCompositionLoadedListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onCompositionLoaded(LottieComposition composition) {
//过滤所有的keypath
                    List<KeyPath> list = mirror.resolveKeyPath(
                            new KeyPath("**"));
                    //通过匹配关键字的深度，来过滤需要改变颜色的keypath
                    for (KeyPath path : list) {
                        Log.d("mirror", path.keysToString());

//通过匹配关键字的深度对深度为1 和2 的填充色进行修改
//                        if (path.matches("btn_q", 2)||path.matches("btn_q", 1) ) {

                        mirror.addValueCallback(path, LottieProperty.COLOR, new SimpleLottieValueCallback<Integer>() {
                            @Override
                            public Integer getValue(LottieFrameInfo<Integer> frameInfo) {
                                return getResources().getColor(R.color.policec_lick);
                            }
                        });
//                        }
                    }


                }
            });

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


    private void clearState() {
        mPageNumber = 1;
        refresh = true;
        mLotteryCodeBean = null;
    }

    private void returnIntercept() {
        if (mLotteryCodeBean == null || dialogShow) {
            finishReturn();
            finish();
            return;
        }
        //判断抽奖码的数量显示对应的dialog
        if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() == 0) {
            dialogShow = true;
            NoDrawDialog mNoDrawDialog = new NoDrawDialog(LotteryActivity.this);
            mNoDrawDialog.setFinishListener(new NoDrawDialog.OnFinishListener() {
                @Override
                public void onFinish() {
                    finish();
                }
            });
            mNoDrawDialog.create();
            mNoDrawDialog.show();
            return;
        }

        //当抽奖码小于6个
        if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() < 6) {
            dialogShow = true;
            LessMaxDialog mLessMaxDialog = new LessMaxDialog(LotteryActivity.this, mLotteryCodeBean);
            mLessMaxDialog.setFinishListener(new LessMaxDialog.OnFinishListener() {

                @Override
                public void onDismiss() {

                }

                @Override
                public void onAgain() {
                    mLessMaxDialog.dismiss();
                    startLottery();
                }
            });
            mLessMaxDialog.create();
            mLessMaxDialog.show();
            return;
        }
        //当抽奖码大于等于
        if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes().size() >= 6) {
            finishReturn();
            finish();
            return;
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