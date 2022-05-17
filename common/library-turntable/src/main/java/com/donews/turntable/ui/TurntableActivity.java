package com.donews.turntable.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.dn.events.events.LotteryBackEvent;
import com.donews.base.BuildConfig;
import com.donews.base.utils.ToastUtil;
import com.donews.common.router.RouterActivityPath;
import com.donews.middle.bean.globle.TurntableBean;
import com.donews.middle.centralDeploy.TurntableSwitch;
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

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

@Route(path = RouterActivityPath.Turntable.TURNTABLE_ACTIVITY)
public class TurntableActivity extends TurntableBaseActivity<TurntableActivityLayoutBinding> implements View.OnClickListener {

    private static String TURNTABLE_BASE = BuildConfig.BASE_QBN_API;
    //获取抽奖中奖人员列表
    public static String TURNTABLE_COMMODITY = TURNTABLE_BASE + "activity/v1/turntable";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        ARouter.getInstance().inject(this);
        initTurntableView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.turntable_activity_layout;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }


    private void initTurntableView() {
        List<TurntableBean.ItemsDTO> list = TurntableSwitch.Ins().getTurntableBean().getItems();
        mDataBing.turntableView.setInitBitmap(list);
        mDataBing.turntableView.setTurntableResultListener(new TurntableView.ITurntableResultListener() {
            @Override
            public void onResult(TurntableBean.ItemsDTO dto) {
                //抽奖活动返回结果
                showDoingResultDialog(dto);
                EventBus.getDefault().post(dto);
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
                                ToastUtil.show(TurntableActivity.this, recommendBean.getId() + "'");
                                mDataBing.turntableView.startAnimator();
                            }
                        }
                    });
            this.addDisposable(disposable);
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
