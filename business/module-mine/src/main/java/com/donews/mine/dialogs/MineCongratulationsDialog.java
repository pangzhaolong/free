/**
 * 额外获得奖励的弹窗
 *
 * @author hegai
 * @version v1.0
 * @date 2021/12/8
 */

package com.donews.mine.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.dn.sdk.listener.interstitial.SimpleInterstitialFullListener;
import com.donews.base.BuildConfig;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.centralDeploy.ABSwitch;
import com.donews.middle.adutils.InterstitialFullAd;
import com.donews.mine.R;
import com.donews.mine.databinding.MineCongratulationsDialogLayoutBinding;
import com.donews.mine.dialogs.bean.RecommendBean;
import com.donews.mine.utils.ImageUtils;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.network.request.GetRequest;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;

import java.lang.ref.WeakReference;
import java.util.Random;

/**
 * 提现成功的弹窗
 */
public class MineCongratulationsDialog extends BaseDialog<MineCongratulationsDialogLayoutBinding> implements DialogInterface.OnDismissListener {
    private Context mContext;
    private String number;
    private CongratulationsHandler mLotteryHandler = new CongratulationsHandler(this);

    public MineCongratulationsDialog(Context context, String number) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.mContext = context;
        this.number = number;
    }

    //保存记录当前展示的商品
    RecommendBean mRecommendBean;

    OnFinishListener mOnFinishListener;

    @Override
    public int setLayout() {
        return R.layout.mine_congratulations_dialog_layout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestGoodsInfo("", false);

        mDataBinding.jumpButton.setOnClickListener(v -> {
            AnalysisUtils.onEventEx(v.getContext(), Dot.Btn_LotteryNow);
            if (mRecommendBean != null) {
                dismiss();
                ARouter.getInstance()
                        .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                        .withString("goods_id", mRecommendBean.getGoodsId())
                        .withBoolean("start_lottery", ABSwitch.Ins().isOpenAutoLottery())
                        .navigation();
            }
        });

        mDataBinding.closure.setOnClickListener(v -> {
            /*InterstitialAd.INSTANCE.showAd((Activity) mContext, new SimpleInterstitialListener() {
                @Override
                public void onAdShow() {
                    super.onAdShow();
                }
            });*/
            InterstitialFullAd.INSTANCE.showAd((Activity) mContext, new SimpleInterstitialFullListener());

            if (mOnFinishListener != null) {
                mOnFinishListener.onFinish();
            }
            dismiss();
        });
        mDataBinding.jsonHand.setImageAssetsFolder("images");
        mDataBinding.jsonHand.setAnimation("lottery_finger.json");
        mDataBinding.jsonHand.loop(true);
        mDataBinding.jsonHand.playAnimation();
        startAnimation();
        setOnDismissListener(this);
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mLotteryHandler.sendMessageDelayed(message, 1000);
        mDataBinding.randomNumber.setText(number);
    }


    private void startAnimation() {
        int number = new Random().nextInt(2);
        int pd = new Random().nextInt(500) + 500;
        if (number == 0) {
            initAnimation(mDataBinding.ivLh1, 0);
            initAnimation(mDataBinding.ivLh2, pd);
        } else {
            initAnimation(mDataBinding.ivLh1, pd);
            initAnimation(mDataBinding.ivLh2, 0);
        }
    }


    private void initAnimation(ImageView view, int time) {
        if (view == null) {
            return;
        }

        mLotteryHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setAnimation(view);
            }
        }, time);
    }


    private void setAnimation(ImageView view) {
        if (view == null) {
            return;
        }
        view.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(
                mContext,
                R.anim.anim_yh_in
        );
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim.setRepeatCount(2);
        view.startAnimation(anim);
        view.setVisibility(View.VISIBLE);
    }


    @Override
    public float setSize() {
        return 1.0f;
    }

    //绑定商品数据
    private void bindGoodsInfo() {
        if (mDataBinding != null && mRecommendBean != null) {
            ImageUtils.setImage(mContext, mDataBinding.commodity, mRecommendBean.getMainPic(), 5);
            mDataBinding.price.setText("¥ " + mRecommendBean.getDisplayPrice());
            mDataBinding.originalPrice.setText("¥ " + mRecommendBean.getOriginalPrice());
            mDataBinding.hint.setText("累计 " + mRecommendBean.getTotalPeople() + " 人参与抢购");
            mDataBinding.titleName.setText(mRecommendBean.getTitle());
            mDataBinding.originalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private void requestGoodsInfo(String goods_id, boolean isCacheRequest) {
        String mRecommendJsonKey = "mRecommendJsonKey";
        //缓存请求
        GetRequest baseRequest = EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/recommend-lottery-goods")
                .cacheMode(CacheMode.NO_CACHE)
                .params("goods_id", goods_id + "");
        SimpleCallBack<RecommendBean> callBack = new com.donews.network.callback.SimpleCallBack<RecommendBean>() {
            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onSuccess(RecommendBean recommendBean) {
                if (recommendBean != null) {
                    if (isCacheRequest) {
                        //缓存等待下一次使用
                        SPUtils.getInstance().put(mRecommendJsonKey, GsonUtils.toJson(recommendBean));
                    } else {
                        mRecommendBean = recommendBean;
                        bindGoodsInfo();
                        requestGoodsInfo("", true);
                    }
                }
            }
        };
        if (isCacheRequest) {
            //缓存请求
            baseRequest.execute(callBack);
        } else {
            String json = SPUtils.getInstance().getString(mRecommendJsonKey, "");
            if (json != null && json.length() > 0) {
                try {
                    mRecommendBean = GsonUtils.fromJson(json, RecommendBean.class);
                    bindGoodsInfo();
                    //重新请求一次缓存
                    requestGoodsInfo("", true);
                } catch (Exception e) {
                    mRecommendBean = null;
                    baseRequest.execute(callBack);
                    e.printStackTrace();
                }
            } else {
                baseRequest.execute(callBack);
            }
        }
    }


    public void setFinishListener(OnFinishListener l) {
        mOnFinishListener = l;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mDataBinding.ivLh1.clearAnimation();
        mDataBinding.ivLh2.clearAnimation();
        mLotteryHandler.removeMessages(0);
        mLotteryHandler.removeMessages(1);
        mLotteryHandler.removeCallbacksAndMessages(null);
        mLotteryHandler = null;

    }

    public interface OnFinishListener {
        /**
         * 此时可以关闭Activity了
         */
        void onFinish();
    }

    private static class CongratulationsHandler extends Handler {
        private WeakReference<MineCongratulationsDialog> reference;   //

        CongratulationsHandler(MineCongratulationsDialog context) {
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