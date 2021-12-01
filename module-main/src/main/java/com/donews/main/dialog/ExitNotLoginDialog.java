package com.donews.main.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.VibrateUtils;
import com.dn.drouter.ARouteHelper;
import com.dn.events.events.LoginUserStatus;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterActivityPath;
import com.donews.main.R;
import com.donews.main.databinding.MainInterceptDialogLayoutBinding;
import com.donews.main.utils.ClickDoubleUtil;
import com.donews.main.utils.ExitInterceptUtils;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;

/**
 * 未登录的弹窗拦截
 */
public class ExitNotLoginDialog extends BaseDialog<MainInterceptDialogLayoutBinding> implements DialogInterface.OnDismissListener, View.OnClickListener {

    /**
     * 创建弹窗
     *
     * @param context
     * @return
     */
    public static ExitNotLoginDialog newInstance(Activity context) {
        return new ExitNotLoginDialog(context);
    }

    public static int TYPE_1 = 1;
    public static int TYPE_2 = 2;
    private Context mContext;
    private int limitNumber = 1;
    private int mType = -1;// 1 表示登录 2 表示未登录
    private LotteryHandler mLotteryHandler;
    private long fastVibrateTime = 0;

    public ExitNotLoginDialog(Activity context) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.mContext = context;
        mLotteryHandler = new LotteryHandler(this);
        this.mType = 2;
    }

    public ExitNotLoginDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        mLotteryHandler = new LotteryHandler(this);
        this.mType = 2;
    }

    @Override
    public int setLayout() {
        return R.layout.main_intercept_dialog_layout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding.closure.setOnClickListener(v -> dismiss());
        mDataBinding.tvNextLk.setOnClickListener(v -> {
            if (v.getVisibility() == View.VISIBLE) {
                ExitInterceptUtils.exitApp((AppCompatActivity) mContext);
                mOnFinishListener.onDismiss();
            }
        });

        initView();
        EventBus.getDefault().register(this);
        setOnDismissListener(this);
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mLotteryHandler.sendMessageDelayed(message, 1000);
    }


    @Subscribe
    public void WeChatLoginEvent(LoginUserStatus loginUserStatus) {
        if (loginUserStatus.getStatus() == 1 && AppInfo.checkIsWXLogin()) {
            immediatelySnappedUp();
            dismiss();
        }
    }


    //立即抢购
    private void immediatelySnappedUp() {
        AnalysisUtils.onEventEx(mContext, Dot.Btn_LotteryNow);
        if (ClickDoubleUtil.isFastClick()) {
            if (mOnFinishListener != null) {
                mOnFinishListener.onDismiss();
                mOnFinishListener.onDismissAd();
            }
        }

    }
    private boolean isSendCloseEvent = true;
    // 1 表示未登录 2 表示登录未抽奖
    private void initView() {
        if (mType == TYPE_1) {
            //登录时
            LinearLayout.LayoutParams rootLayout = (LinearLayout.LayoutParams) mDataBinding.returnRootLayout.getLayoutParams();
            rootLayout.height = getContext().getResources().getDimensionPixelOffset(R.dimen.lottery_constant_328);
            mDataBinding.returnRootLayout.setLayoutParams(rootLayout);
            mDataBinding.title.setText(getContext().getResources().getString(R.string.return_intercept_title));
            mDataBinding.hintTitle.setText(getContext().getResources().getString(R.string.return_intercept_hint));
            mDataBinding.hint.setVisibility(View.GONE);
            mDataBinding.withdrawHint.setText(getContext().getResources().getString(R.string.return_intercept_withdraw));
            mDataBinding.jumpButton.setText(getContext().getResources().getString(R.string.return_intercept_button));
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mDataBinding.withdrawHintLayout.getLayoutParams();
            layoutParams.bottomMargin = getContext().getResources().getDimensionPixelOffset(R.dimen.lottery_constant_15);
            mDataBinding.withdrawHintLayout.setLayoutParams(layoutParams);
            mDataBinding.protocolLayout.setVisibility(View.GONE);
        } else if (mType == TYPE_2) {
            //未登录时
            boolean protocol = getSharedPreferences().getBoolean("protocol", false) ||
                    ABSwitch.Ins().isOpenAutoAgreeProtocol();
            mDataBinding.checkBox.setChecked(protocol);
//            mDataBinding.title.setText(getContext().getResources().getString(R.string.return_intercept_title));
//            mDataBinding.hintTitle.setText(getContext().getResources().getString(R.string.return_intercept_hint_no));
//            mDataBinding.hint.setVisibility(View.VISIBLE);
            mDataBinding.withdrawHint.setText(getContext().getResources().getString(R.string.return_intercept_withdraw_no));
            mDataBinding.jumpButton.setText(getContext().getResources().getString(R.string.return_intercept_button_no));
            mDataBinding.protocolLayout.setVisibility(View.VISIBLE);
            mDataBinding.userProtocol.setOnClickListener(this);
            mDataBinding.privacyProtocol.setOnClickListener(this);
        }
        mDataBinding.jumpButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                //登录时
                if (mType == TYPE_1) {
                    immediatelySnappedUp();
                }
                //未登录时
                if (mType == TYPE_2) {
                    //判断是否同意了隐私协议
                    if (mDataBinding.checkBox.isChecked()) {
                        //存储状态
                        isSendCloseEvent = false;
                        AnalysisUtils.onEventEx(mContext, Dot.But_Home_Exit_Not_Login_Continue);
                        getEditor().putBoolean("protocol", true).commit();
                        RouterActivityPath.LoginProvider.getLoginProvider()
                                .loginWX(null, "未登陆退出弹窗");
                    } else {
                        //檢查是否勾选协议
                        if (System.currentTimeMillis() - fastVibrateTime > 1500) {
                            fastVibrateTime = System.currentTimeMillis();
                            VibrateUtils.vibrate(100); //震动50毫秒
                        }
                        ToastUtil.showShort(mContext, "请先同意相关协议");

                        mDataBinding.protocolLayout.clearAnimation();
                        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_not_select);
                        mDataBinding.protocolLayout.startAnimation(anim);
                    }
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(isSendCloseEvent){
            AnalysisUtils.onEventEx(mContext, Dot.But_Home_Exit_Not_Login_Close);
        }
        if (mLotteryHandler != null) {
            mLotteryHandler.removeMessages(0);
            mLotteryHandler.removeMessages(1);
            mLotteryHandler.removeCallbacksAndMessages(null);
            mLotteryHandler = null;
        }
        EventBus.getDefault().unregister(this);
        mOnFinishListener.onDismiss();
    }

    @Override
    public void onClick(View v) {
        //用户协议
        if (v.getId() == R.id.user_protocol) {
            Bundle bundle = new Bundle();
            bundle.putString("url",
                    "http://ad-static-xg.tagtic.cn/wangzhuan/file/9e5f7a06cbf80a2186e3e34a70f0c360.html");
            bundle.putString("title", "用户协议");
            ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
        }

        //隐私政策
        if (v.getId() == R.id.privacy_protocol) {

            Bundle bundle = new Bundle();
            bundle.putString("url",
                    "http://ad-static-xg.tagtic.cn/wangzhuan/file/b7f18dcb857e80eab353cfb99c3f042e.html");
            bundle.putString("title", "隐私政策");
            ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
        }
    }

    public interface OnFinishListener {
        /**
         * 此时可以关闭Activity了
         */
        void onDismiss();

        void onDismissAd();

    }

    private static class LotteryHandler extends Handler {
        private WeakReference<ExitNotLoginDialog> reference;   //

        LotteryHandler(ExitNotLoginDialog context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null) {
                        reference.get().mDataBinding.closure.setVisibility(View.VISIBLE);
                        reference.get().mDataBinding.tvNextLk.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }
}