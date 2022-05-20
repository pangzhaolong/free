/**
 * 额外获得奖励的弹窗
 *
 * @author hegai
 * @version v1.0
 * @date 2021/12/8
 */

package com.module.lottery.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.dialog.BaseDialog;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.module.lottery.bean.RecommendBean;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.utils.ImageUtils;
import com.module.lottery.utils.RandomProbability;
import com.module_lottery.R;
import com.module_lottery.databinding.CongratulationsDialogLayoutBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//满足6个恭喜dialog
public class CongratulationsDialog extends BaseDialog<CongratulationsDialogLayoutBinding> implements DialogInterface.OnDismissListener {
    private Context mContext;
    int limitNumber = 1;
    //当前id
    int currentId = 0;
    private CongratulationsHandler mLotteryHandler = new CongratulationsHandler(this);
    private String mGoodsId;

    public CongratulationsDialog(Context context, String goods_id) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.mContext = context;
        mGoodsId = goods_id;
    }

    //保存记录当前展示的商品
    RecommendBean mRecommendBean;

    OnFinishListener mOnFinishListener;

    @Override
    public int setLayout() {
        return R.layout.congratulations_dialog_layout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestGoodsInfo(mGoodsId);

        mDataBinding.jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRecommendBean != null) {
                    dismiss();
                    ARouter.getInstance()
                            .build(RouterFragmentPath.Lottery.PAGER_LOTTERY).withString("goods_id", mRecommendBean.getList().get(currentId).getGoodsId()).withString("action", "newAction")
                            .navigation();
                }
            }
        });

        mDataBinding.another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initView();
            }
        });


        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnFinishListener != null) {
                    mOnFinishListener.onFinish();
                }
            }
        });
        mDataBinding.jsonHand.setImageAssetsFolder("images");
        mDataBinding.jsonHand.setAnimation("lottery_finger.json");
        mDataBinding.jsonHand.loop(true);
        mDataBinding.jsonHand.playAnimation();
        setOnDismissListener(this);
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mLotteryHandler.sendMessageDelayed(message, 1000);
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


    private void requestGoodsInfo(String goods_id) {
        if (mRecommendBean == null) {
            EasyHttp.get(LotteryModel.LOTTERY_RECOMMEND_CODE).cacheMode(CacheMode.NO_CACHE)
                    .params("goods_id", goods_id + "").execute(new com.donews.network.callback.SimpleCallBack<RecommendBean>() {
                @Override
                public void onError(ApiException e) {

                }

                @Override
                public void onSuccess(RecommendBean recommendBean) {
                    if (mDataBinding != null && recommendBean != null && recommendBean.getList() != null) {
                        mRecommendBean = recommendBean;
                        limitNumber = 0;
                        initView();
                    }
                }
            });
        }
    }


    private void initView() {
        if (mRecommendBean != null) {
            if (limitNumber >= mRecommendBean.getList().size()) {
                limitNumber = 0;
            }

            currentId = limitNumber;
            ImageUtils.setImage(mContext, mDataBinding.commodity, mRecommendBean.getList().get(limitNumber).getMainPic(), 5);
            mDataBinding.price.setText("¥ " + mRecommendBean.getList().get(limitNumber).getDisplayPrice());
            mDataBinding.titleName.setText(mRecommendBean.getList().get(limitNumber).getTitle());
            mDataBinding.randomNumber.setText(RandomProbability.Companion.getRandomNumber() + "%");
            limitNumber += 1;
        }
    }


    public void setFinishListener(OnFinishListener l) {
        mOnFinishListener = l;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
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
        private WeakReference<CongratulationsDialog> reference;   //

        CongratulationsHandler(CongratulationsDialog context) {
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