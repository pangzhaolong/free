/**
 * 额外获得奖励的弹窗
 *
 * @author hegai
 * @version v1.0
 * @date 2021/12/8
 */

package com.module.lottery.dialog;

import static com.donews.middle.utils.CommonUtils.LOTTERY_FINGER;

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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.VibrateUtils;
import com.donews.base.utils.ToastUtil;
import com.dn.drouter.ARouteHelper;
import com.dn.events.events.LoginUserStatus;
import com.donews.common.router.RouterActivityPath;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.AppInfo;
import com.module.lottery.utils.ClickDoubleUtil;
import com.module_lottery.R;
import com.module_lottery.databinding.InterceptDialogLayoutBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;

//抽奖页返回拦截dialog
public class ReturnInterceptDialog extends BaseDialog<InterceptDialogLayoutBinding> implements DialogInterface.OnDismissListener, View.OnClickListener {
    //未登录
    public static int TYPE_1 = 1;
    //登录未抽奖
    public static int TYPE_2 = 2;
    private Context mContext;
    private int limitNumber = 1;
    private LotteryHandler mLotteryHandler;
    private long fastVibrateTime = 0;

    public ReturnInterceptDialog(Context context, int dialogType) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.mContext = context;
        mLotteryHandler = new LotteryHandler(this);
        this.limitNumber = dialogType;
    }

    @Override
    public int setLayout() {
        return R.layout.intercept_dialog_layout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mDataBinding.interceptClosure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
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
        isSendCloseEvent = false;
        AnalysisUtils.onEventEx(mContext, Dot.Btn_LotteryNow);
        AnalysisUtils.onEventEx(mContext, Dot.Lottery_Login_Dialog_Continue);
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
        if (limitNumber == TYPE_1) {
            boolean protocol = getSharedPreferences().getBoolean("protocol", false) ||
                    ABSwitch.Ins().isOpenAutoAgreeProtocol();
            mDataBinding.checkBox.setChecked(protocol);
            mDataBinding.hintTitle.setText(getContext().getResources().getString(R.string.return_intercept_hint_no));
            mDataBinding.jumpButton.setText(getContext().getResources().getString(R.string.return_intercept_button_no));
            mDataBinding.protocolLayout.setVisibility(View.VISIBLE);
            mDataBinding.userProtocol.setOnClickListener(this);
            mDataBinding.privacyProtocol.setOnClickListener(this);
            mDataBinding.protocolLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDataBinding.checkBox.setChecked(!mDataBinding.checkBox.isChecked());
                }
            });
            mDataBinding.jumpButton.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("MissingPermission")
                @Override
                public void onClick(View v) {

                    //判断是否同意了隐私协议
                    if (mDataBinding.checkBox.isChecked()) {
                        //存储状态
                        isSendCloseEvent = false;
                        AnalysisUtils.onEventEx(mContext, Dot.Lottery_Not_Login_Dialog_Continue);
                        getEditor().putBoolean("protocol", true).commit();
                        RouterActivityPath.LoginProvider.getLoginProvider()
                                .loginWX(null, "抽奖页退出拦截弹窗");
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
        } else {
            mDataBinding.topIcon.setVisibility(View.GONE);
            mDataBinding.protocolLayout.setVisibility(View.GONE);
            mDataBinding.hint.setText("参与抽奖，立即获得");
            mDataBinding.hintTitle.setText("最高");
            mDataBinding.jumpButton.setText("立即抢购");
            mDataBinding.jumpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnFinishListener != null) {
                        mOnFinishListener.onDismissAd();
                        mOnFinishListener.onDismiss();
                    }
                }
            });
        }


        //手
        mDataBinding.maskingHand.setImageAssetsFolder("images");
        mDataBinding.maskingHand.setAnimation(LOTTERY_FINGER);
        mDataBinding.maskingHand.loop(true);
        mDataBinding.maskingHand.playAnimation();

    }

    @Override
    public float setSize() {
        return 1.0f;
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
        if (isSendCloseEvent) {
//            if(TYPE_2 == mType) {
//                //未登录关闭
//                AnalysisUtils.onEventEx(mContext, Dot.Lottery_Not_Login_Dialog_Close);
//            }else{
//                //已登录关闭
//                AnalysisUtils.onEventEx(mContext, Dot.Lottery_Login_Dialog_Close);
//            }
        }
        if (mLotteryHandler != null) {
            mLotteryHandler.removeMessages(0);
            mLotteryHandler.removeMessages(1);
            mLotteryHandler.removeCallbacksAndMessages(null);
            mLotteryHandler = null;
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        //用户协议
        if (v.getId() == R.id.user_protocol) {
            Bundle bundle = new Bundle();
            bundle.putString("url",
                    "http://ad-static-xg.tagtic.cn/wangzhuan/file/e0175957f8bb037da313fa23caae5944.html");
            bundle.putString("title", "用户协议");
            ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
        }

        //隐私政策
        if (v.getId() == R.id.privacy_protocol) {

            Bundle bundle = new Bundle();
            bundle.putString("url",
                    "http://ad-static-xg.tagtic.cn/wangzhuan/file/6970b84a455591b81911894a4ff95ade.html");
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
        private WeakReference<ReturnInterceptDialog> reference;   //

        LotteryHandler(ReturnInterceptDialog context) {
            reference = new WeakReference(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (reference.get() != null) {
                        reference.get().mDataBinding.interceptClosure.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }


}