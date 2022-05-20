package com.donews.turntable.ui;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.LotteryBackEvent;
import com.dn.integral.jdd.integral.ProxyIntegral;
import com.donews.base.BuildConfig;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterActivityPath;
import com.donews.middle.bean.globle.TurntableBean;
import com.donews.middle.centralDeploy.ABSwitch;
import com.donews.middle.centralDeploy.TurntableSwitch;
import com.donews.middle.utils.CriticalModelTool;
import com.donews.middle.utils.PlayAdUtilsTool;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.turntable.R;
import com.donews.turntable.base.TurntableBaseActivity;
import com.donews.turntable.bean.RewardedBean;
import com.donews.turntable.bean.TurntablePrize;
import com.donews.turntable.databinding.TurntableActivityLayoutBinding;
import com.donews.turntable.dialog.DoingResultDialog;
import com.donews.turntable.dialog.RuleDialog;
import com.donews.turntable.utils.ClickDoubleUtil;
import com.donews.turntable.view.TurntableView;
import com.donews.yfsdk.monitor.LotteryAdCheck;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

@Route(path = RouterActivityPath.Turntable.TURNTABLE_ACTIVITY)
public class TurntableActivity extends TurntableBaseActivity<TurntableActivityLayoutBinding> implements View.OnClickListener {
    PlayAdUtilsTool mPlayAdUtilsTool;
    private static String TURNTABLE_BASE = BuildConfig.BASE_QBN_API;
    //获取抽奖中奖人员列表
    public static String TURNTABLE_COMMODITY = TURNTABLE_BASE + "activity/v1/turntable";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayAdUtilsTool = PlayAdUtilsTool.getInstance();
        ARouter.getInstance().inject(this);
        initTurntableView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.turntable_activity_layout;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initTurntableView() {
        List<TurntableBean.ItemsDTO> list = TurntableSwitch.Ins().getTurntableBean().getItems();
        mDataBing.turntableView.setInitBitmap(list);
        mDataBing.turntableView.setTurntableResultListener(new TurntableView.ITurntableResultListener() {
            @Override
            public void onResult(TurntableBean.ItemsDTO itemsDTO) {
                //抽奖活动返回结果
                showDoingResultDialog(itemsDTO);
                EventBus.getDefault().post(itemsDTO);
            }
        });
        mDataBing.turntableLotteryButton.setOnClickListener(this);
        mDataBing.turntableDrawAgain.setOnClickListener(this);
    }

    private void startLottery() {
        if (ClickDoubleUtil.isFastClick()) {
            //先获取配置奖励
            this.unDisposable();
            Disposable disposable = EasyHttp.post(TURNTABLE_COMMODITY)
                    .cacheMode(CacheMode.NO_CACHE)
                    .execute(new SimpleCallBack<RewardedBean>() {
                        @Override
                        public void onError(ApiException e) {
                            ToastUtil.show(TurntableActivity.this, e.getMessage());
                        }

                        @Override
                        public void onSuccess(RewardedBean recommendBean) {
                            if (recommendBean != null) {
                                loadAdAndStatusCallback(recommendBean );
                            }
                        }
                    });
            this.addDisposable(disposable);
        }
    }

    /**
     * 用来加载并显示广告和接收广告状态的回调
     */

    private void loadAdAndStatusCallback(RewardedBean recommendBean  ) {
        if (mPlayAdUtilsTool != null) {
            mPlayAdUtilsTool.showRewardVideo(this);
            mPlayAdUtilsTool.setIStateListener(new PlayAdUtilsTool.IStateListener() {
                @Override
                public void onComplete() {
                    ToastUtil.show(TurntableActivity.this, recommendBean.getCoin() + "");
                    //根据规则生成度数
                    mDataBing.turntableView.startAnimator(recommendBean);
                }
                @Override
                public void onFinish() {

                }
                @Override
                public void onError(int code, @Nullable String errorMsg) {

                }
            });
        }
    }



    //点击tips
    public void clickTips(View view) {
        if (ClickDoubleUtil.isFastClick()) {
            RuleDialog ruleDialog = new RuleDialog(TurntableActivity.this);
            ruleDialog.show(TurntableActivity.this);
        }
    }


    private void showDoingResultDialog(TurntableBean.ItemsDTO itemsDTO) {
        DoingResultDialog dialog = new DoingResultDialog(TurntableActivity.this, itemsDTO);
        dialog.setStateListener(new DoingResultDialog.OnStateListener() {
            @Override
            public void onOK() {

            }
        });
        dialog.show(TurntableActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.turntable_draw_again) {
            startLottery();
        }
        if (v.getId() == R.id.turntable_lottery_button) {
            startLottery();
        }
    }
}
