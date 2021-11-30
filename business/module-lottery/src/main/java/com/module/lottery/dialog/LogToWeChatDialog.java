package com.module.lottery.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.VibrateUtils;
import com.dn.drouter.ARouteHelper;
import com.dn.events.events.LoginUserStatus;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterActivityPath;
import com.donews.main.BuildConfig;
import com.donews.main.dialog.BaseDialog;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.utilslibrary.utils.AppInfo;
import com.module.lottery.utils.ImageUtils;
import com.module_lottery.R;
import com.module_lottery.databinding.LogToWechatLayoutBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;

//抽奖页返回拦截dialog
public class LogToWeChatDialog extends BaseDialog<LogToWechatLayoutBinding> implements DialogInterface.OnDismissListener, View.OnClickListener {

    private String FATHER_URL = BuildConfig.API_LOTTERY_URL;
    String RECENT_FREE = FATHER_URL + "v1/get-now-time";
    private Context mContext;
    private int limitNumber = 1;
    private LotteryHandler mLotteryHandler;
    private long fastVibrateTime = 0;
    OnFinishListener mOnFinishListener;

    public LogToWeChatDialog(Context context) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.mContext = context;
        mLotteryHandler = new LotteryHandler(this);
    }

    @Override
    public int setLayout() {
        return R.layout.log_to_wechat_layout;
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
        mDataBinding.protocolLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDataBinding.checkBox.setChecked(!mDataBinding.checkBox.isChecked());
            }
        });

        EventBus.getDefault().register(this);
        initView();
        setOnDismissListener(this);
        //延迟一秒出现关闭按钮
        Message message = new Message();
        message.what = 1;
        mLotteryHandler.sendMessageDelayed(message, 1000);
    }


    public void refreshCoverIcon(String url) {
        if (url != null) {
            ImageUtils.setImage(mContext, mDataBinding.coverIcon, url, 1);
        }
    }


    @Subscribe
    public void WeChatLoginEvent(LoginUserStatus loginUserStatus) {
        if (loginUserStatus.getStatus() == 1 && AppInfo.checkIsWXLogin()) {
            if(mOnFinishListener!=null){
                mOnFinishListener.onWeChatReturn();
                mOnFinishListener.onDismiss();
            }
        }
    }


    // 1 表示未登录 2 表示登录未抽奖
    private void initView() {
        //未登录时
        mDataBinding.userProtocol.setOnClickListener(this);
        mDataBinding.privacyProtocol.setOnClickListener(this);
        boolean protocol = getSharedPreferences().getBoolean("Free", false) ||
                ABSwitch.Ins().isOpenAutoAgreeProtocol();
        mDataBinding.checkBox.setChecked(protocol);
        mDataBinding.jumpButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                //判断是否同意了隐私协议
                if (mDataBinding.checkBox.isChecked()) {
                    //存储状态
                    getEditor().putBoolean("Free", true).commit();
                    RouterActivityPath.LoginProvider.getLoginProvider()
                            .loginWX(null);
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


    public void setFinishListener(OnFinishListener l) {
        mOnFinishListener = l;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mLotteryHandler != null) {
            mLotteryHandler.removeMessages(0);
            mLotteryHandler.removeMessages(1);
            mLotteryHandler.removeCallbacksAndMessages(null);
            mLotteryHandler = null;
        }
        EventBus.getDefault().unregister(this);
        if (mOnFinishListener != null) {
            mOnFinishListener.onDismiss();
        }
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

        void onToWeChat();

        void onWeChatReturn();

    }

    private static class LotteryHandler extends Handler {
        private WeakReference<LogToWeChatDialog> reference;   //

        LotteryHandler(LogToWeChatDialog context) {
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