package com.module.lottery.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.main.entitys.resps.ExitDialogRecommendGoodsResp;
import com.donews.main.utils.ExitInterceptUtils;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.module.lottery.utils.ClickDoubleUtil;
import com.module.lottery.utils.ImageUtils;
import com.module_lottery.R;
import com.module_lottery.databinding.NoDrawDialogLayoutBinding;

//抽奖页返回拦截dialog
public class ReturnInterceptDialog extends BaseDialog<NoDrawDialogLayoutBinding> {
    public static int TYPE_1 = 1;
    public static int TYPE_2 = 2;
    private Context mContext;
    private int limitNumber = 1;
    private int mType = -1;// 1 表示登录 2 表示未登录

    public ReturnInterceptDialog(Context context, int type) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.mContext = context;
        this.mType = type;
    }

    @Override
    public int setLayout() {
        return R.layout.no_draw_dialog_layout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mDataBinding.closure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        initView();
    }

    // 1 表示未登录 2 表示登录未抽奖
    private void initView() {
        if (mType == TYPE_1) {
            //登录时
            LinearLayout.LayoutParams rootLayout= (LinearLayout.LayoutParams)  mDataBinding.returnRootLayout.getLayoutParams();
            rootLayout.height=getContext().getResources().getDimensionPixelOffset(R.dimen.lottery_constant_328);
            mDataBinding.returnRootLayout.setLayoutParams(rootLayout);
            mDataBinding.title.setText(getContext().getResources().getString(R.string.return_intercept_title));
            mDataBinding.hintTitle.setText(getContext().getResources().getString(R.string.return_intercept_hint));
            mDataBinding.hint.setVisibility(View.GONE);
            mDataBinding.withdrawHint.setText(getContext().getResources().getString(R.string.return_intercept_withdraw));
            mDataBinding.jumpButton.setText(getContext().getResources().getString(R.string.return_intercept_button));
            LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) mDataBinding.withdrawHintLayout.getLayoutParams();
            layoutParams.bottomMargin=getContext().getResources().getDimensionPixelOffset(R.dimen.lottery_constant_15);
            mDataBinding.withdrawHintLayout.setLayoutParams(layoutParams);

        } else if (mType == TYPE_2) {
            //未登录时
            mDataBinding.title.setText(getContext().getResources().getString(R.string.return_intercept_title));
            mDataBinding.hintTitle.setText(getContext().getResources().getString(R.string.return_intercept_hint_no));
            mDataBinding.hint.setVisibility(View.VISIBLE);
            mDataBinding.withdrawHint.setText(getContext().getResources().getString(R.string.return_intercept_withdraw_no));
            mDataBinding.jumpButton.setText(getContext().getResources().getString(R.string.return_intercept_button_no));
        }

        mDataBinding.jumpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //登录时
                if (mType == TYPE_1) {
                    AnalysisUtils.onEventEx(v.getContext(), Dot.Btn_LotteryNow);
                    if (ClickDoubleUtil.isFastClick()) {
                        if (mOnFinishListener != null) {
                            mOnFinishListener.onDismiss();
                            mOnFinishListener.onDismissAd();
                        }
                    }
                }
                //未登录时
                if (mType == TYPE_2) {
                    ARouter.getInstance()
                            .build(RouterActivityPath.User.PAGER_LOGIN)
                            .navigation();
                }
            }
        });

        //手
        mDataBinding.maskingHand.setImageAssetsFolder("images");
        mDataBinding.maskingHand.setAnimation("lottery_finger.json");
        mDataBinding.maskingHand.loop(true);
        mDataBinding.maskingHand.playAnimation();

    }

    @Override
    public float setSize() {
        return 0.9f;
    }


    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        return true;
    }

    OnFinishListener mOnFinishListener;


    public void setFinishListener(OnFinishListener l) {
        mOnFinishListener = l;
    }

    public interface OnFinishListener {
        /**
         * 此时可以关闭Activity了
         */
        void onDismiss();

        void onDismissAd();

    }
}