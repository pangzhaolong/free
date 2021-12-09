package com.donews.main.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.DoubleRpEvent;
import com.dn.sdk.listener.IAdRewardVideoListener;
import com.donews.base.utils.ToastUtil;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.ad.cache.AdVideoCacheUtils;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.main.R;
import com.donews.main.databinding.MainRpActivityBinding;
import com.donews.utilslibrary.utils.SoundHelp;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

@Route(path = RouterActivityPath.Rp.PAGE_RP)
public class RpActivity extends MvvmBaseLiveDataActivity<MainRpActivityBinding, BaseLiveDataViewModel> {

    private Context mContext;

    private Animation mScaleAnimation;

    @Autowired
    int type;
    @Autowired
    float score;
    @Autowired
    String restId;
    @Autowired
    String preId;


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;

        ARouter.getInstance().inject(this);

        SoundHelp.newInstance().init(this);
        SoundHelp.newInstance().onStart();

        mDataBinding.mainRpDlgCashTv.setText(String.format("%.2f", score));
        if (type == 0) {
            mDataBinding.mainRpTypeTv.setText("现金红包");
        }
        mDataBinding.mainRpGiveUp.setOnClickListener(v -> {
            SoundHelp.newInstance().onStart();
            finish();
        });
        mDataBinding.mainRpDouble.setOnClickListener(v -> {
            doubleRp();
        });
        if (mScaleAnimation == null) {
            mScaleAnimation = new ScaleAnimation(1.15f, 0.9f, 1.15f, 0.9f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            mScaleAnimation.setInterpolator(new LinearInterpolator());
            mScaleAnimation.setRepeatMode(Animation.REVERSE);
            mScaleAnimation.setRepeatCount(Animation.INFINITE);
            mScaleAnimation.setDuration(700);
            mDataBinding.mainRpDouble.startAnimation(mScaleAnimation);
        }
    }

    @Override
    protected int getLayoutId() {
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
                .init();
        return R.layout.main_rp_activity;
    }

    @Override
    public void initView() {
    }

    private void doubleRp() {

        IAdRewardVideoListener listener = new IAdRewardVideoListener() {
            @Override
            public void onAdStartLoad() {

            }

            @Override
            public void onAdStatus(int code, @Nullable Object any) {

            }

            @Override
            public void onAdLoad() {

            }

            @Override
            public void onAdShow() {

            }

            @Override
            public void onAdVideoClick() {

            }

            @Override
            public void onRewardVerify(boolean result) {
                if (result) {
                    // 完整观看视频
                    EventBus.getDefault().post(new DoubleRpEvent(1, score, restId == null ? "" : restId, preId == null ? "" : preId));
                    finish();
                }
            }

            @Override
            public void onAdClose() {

            }

            @Override
            public void onVideoCached() {

            }

            @Override
            public void onVideoComplete() {

            }

            @Override
            public void onAdError(int code, @Nullable String errorMsg) {
                ToastUtil.showShort(mContext, "视频加载失败，点击翻倍领取重试");
            }
        };

        AdVideoCacheUtils.INSTANCE.showRewardVideo(listener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDataBinding.mainRpDouble.getAnimation() != null) {
            mDataBinding.mainRpDouble.clearAnimation();
        }

        if (mScaleAnimation != null) {
            mScaleAnimation.cancel();
            mScaleAnimation = null;
        }

        SoundHelp.newInstance().onRelease();
    }
}