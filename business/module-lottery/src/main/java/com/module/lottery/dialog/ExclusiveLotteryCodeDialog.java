/**
 * 额外获得奖励的弹窗
 *
 * @author hegai
 * @version v1.0
 * @date 2021/12/8
 */

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
import com.donews.common.views.CountdownView;
import com.donews.main.BuildConfig;
import com.donews.main.dialog.BaseDialog;
import com.donews.middle.abswitch.ABSwitch;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.utils.AppInfo;
import com.module.lottery.bean.GenerateCodeBean;
import com.module.lottery.model.LotteryModel;
import com.module.lottery.ui.BaseParams;
import com.module.lottery.utils.LotteryAnimationUtils;
import com.module_lottery.R;
import com.module_lottery.databinding.ExclusiveLotteryCodeLayoutBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Random;

import io.reactivex.disposables.Disposable;

//抽奖页返回拦截dialog
public class ExclusiveLotteryCodeDialog extends BaseDialog<ExclusiveLotteryCodeLayoutBinding> implements DialogInterface.OnDismissListener, View.OnClickListener {

    private String FATHER_URL = BuildConfig.API_LOTTERY_URL;
    String RECENT_FREE = FATHER_URL + "v1/get-now-time";
    private Context mContext;
    private int limitNumber = 1;
    private ExclusiveHandler mExclusiveHandler;
    private long fastVibrateTime = 0;
    private OnFinishListener mOnFinishListener;
    private String mGoodsId;
    private String valueCode;

    public ExclusiveLotteryCodeDialog(Context context, String goodsId) {
        super(context, R.style.dialogTransparent);//内容样式在这里引入
        this.mContext = context;
        this.mGoodsId = goodsId;
        mExclusiveHandler = new ExclusiveHandler(this);
    }

    @Override
    public int setLayout() {
        return R.layout.exclusive_lottery_code_layout;
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
        mExclusiveHandler.sendMessageDelayed(message, 1000);
    }


    @Subscribe
    public void WeChatLoginEvent(LoginUserStatus loginUserStatus) {
        if (loginUserStatus.getStatus() == 1 && AppInfo.checkIsWXLogin()) {
            if (mOnFinishListener != null) {
                //判断抽奖码是否失效
                Boolean isComplete = mDataBinding.countdownView.isComplete();
                mDataBinding.countdownView.pauseTimer();
                if (isComplete) {
                    ToastUtil.showShort(getContext(), "抽奖码失效");
                    //抽奖码获取失败刷新页面
                    mOnFinishListener.onLoginUploadSuccessful();
                    //关闭dialog
                    mOnFinishListener.closure();
                }else{
                    //奖抽奖码传给服务器
                    requestGoodsInfo(valueCode);
                }
            }
        }
    }


    //查询专属抽奖码信息
    public void requestGoodsInfo(String code) {
        Map<String, String> params = BaseParams.getMap();
        params.put("goods_id", mGoodsId);
        params.put("cus_code", code);
        JSONObject json = new JSONObject(params);
        unDisposable();
        Disposable disposable = EasyHttp.post(LotteryModel.LOTTERY_GENERATE_CODE)
                .cacheMode(CacheMode.NO_CACHE)
                .upJson(json.toString())
                .execute(new SimpleCallBack<GenerateCodeBean>() {
                    @Override
                    public void onError(ApiException e) {
                        if (mOnFinishListener != null) {
                            //抽奖码获取失败刷新页面
                            mOnFinishListener.onLoginUploadSuccessful();
                            //关闭dialog
                            mOnFinishListener.closure();
                        }
                    }

                    @Override
                    public void onSuccess(GenerateCodeBean generateCode) {
                        if (generateCode != null) {
                            //抽奖统计
                            if (mOnFinishListener != null) {
                                //关闭dialog
                                mOnFinishListener.closure();
                                //跳转到抽奖进度条的dialog
                                mOnFinishListener.onLoginSuccessful(generateCode, true);
                                //登录成功刷新页面
                                mOnFinishListener.onLoginUploadSuccessful();
                            }
                        }
                    }
                });
        addDisposable(disposable);
    }


    /**
     * 生成随机抽奖码
     *
     * @param count 随机数个数
     * @return
     */
    public static String randomCode(int count) {
        StringBuffer sb = new StringBuffer();
        String str = "0123456789";
        Random r = new Random();
        for (int i = 0; i < count; i++) {
            int num = r.nextInt(str.length());
            sb.append(str.charAt(num));
//            str = str.replace((str.charAt(num)+""), "");
        }
        return sb.toString();
    }


    // 1 表示未登录 2 表示登录未抽奖
    private void initView() {
        //未登录时
        mDataBinding.userProtocol.setOnClickListener(this);
        mDataBinding.privacyProtocol.setOnClickListener(this);
        mDataBinding.countdownView.setCountdownViewListener(new CountdownView.ICountdownViewListener() {

            @Override
            public void onProgressValue(long max, long value) {

            }

            @Override
            public void onCountdownCompleted() {
                //倒计时完成
                mDataBinding.label01.setText("已失效");
                mDataBinding.countdownView.setVisibility(View.GONE);
                mDataBinding.label02.setVisibility(View.GONE);
            }
        });
        boolean protocol = getSharedPreferences().getBoolean("Free", false) ||
                ABSwitch.Ins().isOpenAutoAgreeProtocol();
        mDataBinding.checkBox.setChecked(protocol);
        mDataBinding.jumpButton.setAnimation(LotteryAnimationUtils.setScaleAnimation(1000));
        mDataBinding.jumpButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                //判断是否同意了隐私协议
                if (mDataBinding.checkBox.isChecked()) {
                    //存储状态
                    getEditor().putBoolean("Free", true).commit();
                    RouterActivityPath.LoginProvider.getLoginProvider()
                            .loginWX(null, "抽奖页返回拦截弹窗");
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
        valueCode = randomCode(7);
        mDataBinding.lotteryCode.setText(valueCode);
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
        if (mExclusiveHandler != null) {
            mExclusiveHandler.removeMessages(0);
            mExclusiveHandler.removeMessages(1);
            mExclusiveHandler.removeCallbacksAndMessages(null);
            mExclusiveHandler = null;
        }
        EventBus.getDefault().unregister(this);
        mDataBinding.jumpButton.clearAnimation();
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
        //登录成功  efficient 抽奖码是否有效(大于6个后就无效)
        public void onLoginSuccessful(GenerateCodeBean generateCode, boolean efficient);


        //登录抽奖码上传成功
        public void onLoginUploadSuccessful();

        //关闭
        public void closure();

    }

    private static class ExclusiveHandler extends Handler {
        private WeakReference<ExclusiveLotteryCodeDialog> reference;   //

        ExclusiveHandler(ExclusiveLotteryCodeDialog context) {
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
                case 2:
                    break;
            }
        }
    }


}