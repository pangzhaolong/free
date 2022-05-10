package com.module.lottery.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.utils.ToastUtil;
import com.donews.common.config.CritParameterConfig;
import com.donews.common.router.RouterFragmentPath;
import com.donews.common.views.CountdownView;
import com.donews.middle.dialog.BaseDialog;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.SPUtils;
import com.module.lottery.bean.CritCodeBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.bean.RecommendBean;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.utils.ClickDoubleUtil;
import com.module.lottery.utils.ImageUtils;
import com.module.lottery.utils.RandomProbability;
import com.module_lottery.R;
import com.module_lottery.databinding.GuesslikeHeadLayoutBinding;
import com.module_lottery.databinding.LotteryCritCommodityLayoutBinding;
import com.module_lottery.databinding.UnlockMaxCodeDialogLayoutBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class UnlockMaxCodeDialog extends BaseDialog<UnlockMaxCodeDialogLayoutBinding> implements DialogInterface.OnDismissListener {
    private UnlockMaxCodeHandler mUnlockMaxCodeHandler = new UnlockMaxCodeHandler(this);
    private String mGoodsId;
    private boolean mState = true;
    List<String> codeList = new ArrayList<>();

    public UnlockMaxCodeDialog(LotteryCodeBean mLotteryCodeBean, CritCodeBean critCodeBean, String goodsId, Context context) {

        super(context, R.style.dialogTransparent);
        this.mGoodsId = goodsId;
        if (mLotteryCodeBean != null && mLotteryCodeBean.getCodes() != null && mLotteryCodeBean.getCodes().size() > 0) {
            codeList.addAll(mLotteryCodeBean.getCodes());
        }
        if (critCodeBean != null && critCodeBean.getCode() != null && critCodeBean.getCode().size() > 0) {
            codeList.addAll(critCodeBean.getCode());
        }


    }

    @Override
    public float setDimAmount() {
        return 0.9f;
    }

    @Override
    public int setLayout() {
        return R.layout.unlock_max_code_dialog_layout;
    }

    @Override
    public float setSize() {
        return 1.0f;
    }
    //获取商品数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestNetData();
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mUnlockMaxCodeHandler.sendMessageDelayed(message, 1000);
        setOnDismissListener(this);
        initListLottery();
        initView();
    }

    //初始化抽奖码
    public void initListLottery() {
        if (codeList.size() > 0) {
            int refer = 0;
            for (int i = 0; i < mDataBinding.lotteryContainer.getChildCount(); i++) {
                LinearLayout linearLayout = (LinearLayout) mDataBinding.lotteryContainer.getChildAt(i);
                for (int j = 0; j < linearLayout.getChildCount(); j++) {
                    TextView textView = (TextView) linearLayout.getChildAt(j);
                    textView.setText(codeList.get(refer));
                    refer++;
                }
            }
        }

    }


    private void initView() {

        mDataBinding.jsonLightSource.setImageAssetsFolder("images");
        mDataBinding.jsonLightSource.setAnimation("light_source.json");
        mDataBinding.jsonLightSource.loop(true);
        mDataBinding.jsonLightSource.playAnimation();

        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mDataBinding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickDoubleUtil.isFastClick() && mState) {
                    mState = false;
                    requestCommodity(mGoodsId);
                }
            }
        });
        mDataBinding.countdownView.setCountdownViewListener(new CountdownView.ICountdownViewListener() {
            @Override
            public void onCountdownCompleted() {
                if (isShowing()) {
                    dismiss();
                }
            }

            @Override
            public void onProgressValue(long max, long value) {

            }
        });
        int startTime = SPUtils.getInformain(CritParameterConfig.CRIT_REMAINING_TIME, 0);
        mDataBinding.countdownView.start(startTime);
    }


    private void refreshPage(RecommendBean recommendBean) {
    }

    private void requestNetData() {
        this.unDisposable();
        Disposable disposable = EasyHttp.get(LotteryModel.LOTTERY_CRIT_COMMODITY).params("goods_id", mGoodsId)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<RecommendBean>() {
                    @Override
                    public void onError(ApiException e) {

                        ToastUtil.show(getContext(), e.getMessage());
                    }

                    @Override
                    public void onSuccess(RecommendBean recommendBean) {

                        if (recommendBean != null) {
                            refreshPage(recommendBean);
                        }
                    }
                });
        this.addDisposable(disposable);
    }

    private void requestCommodity(String goods_id) {
        EasyHttp.get(LotteryModel.LOTTERY_RECOMMEND_CODE).cacheMode(CacheMode.NO_CACHE)
                .params("goods_id", goods_id + "").execute(new com.donews.network.callback.SimpleCallBack<RecommendBean>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(RecommendBean recommendBean) {
                if (mDataBinding != null && recommendBean != null) {
                    //开始跳转商品
                    ARouter.getInstance()
                            .build(RouterFragmentPath.Lottery.PAGER_LOTTERY).withString("goods_id", recommendBean.getGoodsId()).withString("action", "newAction")
                            .navigation();
                    dismiss();
                }
            }
        });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mUnlockMaxCodeHandler != null) {
            mDataBinding.countdownView.pauseTimer();
            mUnlockMaxCodeHandler.removeMessages(0);
            mUnlockMaxCodeHandler.removeMessages(1);
            mUnlockMaxCodeHandler.removeCallbacksAndMessages(null);
        }
    }


    private static class UnlockMaxCodeHandler extends Handler {
        private WeakReference<UnlockMaxCodeDialog> reference;   //

        UnlockMaxCodeHandler(UnlockMaxCodeDialog context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null) {
                        reference.get().mDataBinding.closure.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }


}
