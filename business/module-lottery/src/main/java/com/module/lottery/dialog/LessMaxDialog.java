/**
 * 额外获得奖励的弹窗
 *
 * @author hegai
 * @version v1.0
 * @date 2021/12/8
 */

package com.module.lottery.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.donews.middle.dialog.BaseDialog;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.UrlUtils;
import com.module.lottery.bean.CommodityBean;
import com.module.lottery.bean.LotteryCodeBean;
import com.module.lottery.viewModel.LotteryViewModel;
import com.module_lottery.R;
import com.module_lottery.databinding.LessMaxDialogLayoutBinding;

import java.lang.ref.WeakReference;

//抽奖码小于6个
public class LessMaxDialog extends BaseDialog<LessMaxDialogLayoutBinding> {
    private Context mContext;
    private LotteryCodeBean mLotteryCodeBean;

    //
//     mDataBinding.jsonAnimation.setImageAssetsFolder("images");
//            mDataBinding.jsonAnimation.setAnimation("illustrate.json");
//            mDataBinding.jsonAnimation.loop(true);
//            mDataBinding.jsonAnimation.playAnimation();
    private LotteryHandler mLotteryHandler = new LotteryHandler(this);

    private OnFinishListener mOnFinishListener;
    private boolean isSendCloseEvent = true;
    private LotteryViewModel lotteryViewModel;
    private CommodityBean mCommodityBean;

    public LessMaxDialog(Context context, LotteryCodeBean lotteryCodeBean) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        mLotteryCodeBean = lotteryCodeBean;
        this.mContext = context;
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mLotteryHandler.sendMessageDelayed(message, 1000);
    }


    @Override
    public int setLayout() {
        return R.layout.less_max_dialog_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mContext instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) mContext;
            lotteryViewModel = new ViewModelProvider(activity, new ViewModelProvider.NewInstanceFactory()).get(LotteryViewModel.class);
        }
        if (lotteryViewModel != null) {
            mCommodityBean = lotteryViewModel.getMutableLiveData().getValue();
        }
        initView();
    }

    @Override
    public float setSize() {
        return 0.86f;
    }

    @SuppressLint("RestrictedApi")
    void initView() {
        if (mLotteryCodeBean != null) {
            mDataBinding.number.setText((6 - mLotteryCodeBean.getCodes().size()) + "");
            mDataBinding.jsonHand.setImageAssetsFolder("images");
            mDataBinding.jsonHand.setAnimation("lottery_finger.json");
            mDataBinding.jsonHand.loop(true);
            mDataBinding.jsonHand.playAnimation();
        }
        setOnDismissListener((d) -> {
            if (isSendCloseEvent) {
                AnalysisUtils.onEventEx(mContext, Dot.Lottery_Increase_ChancesDialog_Close);
            }
        });
        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mDataBinding.lotteryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSendCloseEvent = false;
                AnalysisUtils.onEventEx(mContext, Dot.Lottery_Increase_ChancesDialog_Continue);
                if (mOnFinishListener != null) {
                    mOnFinishListener.onFinishAd();
                    dismiss();
                }
            }
        });
        setDataIconAndText();
    }

    private static void setImage(Context context, ImageView view, String src, int roundingRadius) {
        src = UrlUtils.formatUrlPrefix(src);
        RoundedCorners roundedCorners = new RoundedCorners(roundingRadius);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(context).load(src).apply(options).into(view);
    }

    private void setDataIconAndText() {
        if (mCommodityBean != null && mLotteryCodeBean != null) {
            setImage(mContext, mDataBinding.mainImage, mCommodityBean.getMainPic(), 50);
            setImage(mContext, mDataBinding.secondary, mCommodityBean.getMainPic(), 360);
            mDataBinding.number.setText("" + (6 - mLotteryCodeBean.getCodes().size()));
            mDataBinding.lable.setText(mCommodityBean.getTitle());
        }
        if (mLotteryCodeBean != null) {
            for (int i = 0; i < mLotteryCodeBean.getCodes().size(); i++) {
                int code = mLotteryCodeBean.getCodes().size() - 1;
                if (code < 0) {
                    code = 0;
                }
                if (i == code) {
                    //最后一个
                    if (mDataBinding.iconList.getChildAt(i) instanceof RelativeLayout && code != 5) {
                        RelativeLayout relativeLayout= (RelativeLayout) mDataBinding.iconList.getChildAt(i);
                        ImageView imageView= (ImageView) relativeLayout.getChildAt(0);
                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.item_select_halo_icon));
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                        layoutParams.width = (int) mContext.getResources().getDimension(R.dimen.lottery_constant_23);
                        layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.lottery_constant_23);
                        imageView.setLayoutParams(layoutParams);
                    }
                    if (mDataBinding.textList.getChildAt(i) instanceof TextView) {
                        ((TextView) (mDataBinding.textList.getChildAt(i))).setText("获得抢购码: " + mLotteryCodeBean.getCodes().get(i));
                        ((TextView) (mDataBinding.textList.getChildAt(i))).setTextColor(Color.parseColor("#F5562A"));
                        ((TextView) (mDataBinding.textList.getChildAt(i))).setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimensionPixelSize(R.dimen.lottery_constant_16));
                    }
                } else {
                    if (mDataBinding.iconList.getChildAt(i) instanceof RelativeLayout) {
                        RelativeLayout relativeLayout= (RelativeLayout) mDataBinding.iconList.getChildAt(i);
                        ImageView imageView= (ImageView) relativeLayout.getChildAt(0);
                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.item_select_icon));
                    }
                    if (mDataBinding.textList.getChildAt(i) instanceof TextView) {
                        ((TextView) (mDataBinding.textList.getChildAt(i))).setText("抢购吗: " + mLotteryCodeBean.getCodes().get(i));
                        ((TextView) (mDataBinding.textList.getChildAt(i))).setTextColor(Color.parseColor("#F5562A"));
                    }
                }


            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {

        return true;
    }


    public void setFinishListener(OnFinishListener l) {
        mOnFinishListener = l;
    }

    public interface OnFinishListener {
        /**
         * 此时可以关闭Activity了
         */
        void onFinish();

        void onFinishAd();
    }


    private static class LotteryHandler extends Handler {
        private WeakReference<LessMaxDialog> reference;   //

        LotteryHandler(LessMaxDialog context) {
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