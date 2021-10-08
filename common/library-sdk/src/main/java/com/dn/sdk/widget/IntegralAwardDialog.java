package com.dn.sdk.widget;

import android.annotation.SuppressLint;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.dn.sdk.R;
import com.dn.sdk.api.AdSdkHttp;
import com.dn.sdk.bean.IntegralAwardBean;
import com.dn.sdk.bean.IntegralBean;
import com.dn.sdk.dialog.BaseFragmentDialog;
import com.dn.sdk.utils.SdkLogUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.base.UtilsConfig;

/**
 * @author by SnowDragon
 * Date on 2021/4/12
 * Description:
 */
public class IntegralAwardDialog extends BaseFragmentDialog {

    private String award;


    public static void sendReward(FragmentActivity activity, String pkName, String award) {
        AdSdkHttp adSdkHttp = new AdSdkHttp();
        adSdkHttp.giveOutIntervalAward(pkName, new SimpleCallBack<IntegralAwardBean>() {
            @Override
            public void onError(ApiException e) {
                SdkLogUtils.i(SdkLogUtils.TAG, "--onError : " + e.getMessage());
            }

            @Override
            public void onSuccess(IntegralAwardBean integralAwardBean) {
                //刷新积分对象
                showIntegralDialog(activity, award);

                //刷新积分列表
                adSdkHttp.getIntegralList();
            }
        });
    }

    public static void showIntegralDialog(FragmentActivity activity, String award) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        IntegralAwardDialog dialog = new IntegralAwardDialog();
        dialog.setBackgroundDim(false)
                .setAward(award);
        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(dialog, "integralAdDialog")
                .commitAllowingStateLoss();


    }

    @Override
    protected int getLayoutId() {
        return R.layout.sdk_integral_award_dialog;
    }


    @SuppressLint("DefaultLocale")
    @Override
    protected void initView() {
        TextView tvAward = $(R.id.tv_award);
        String awardDes = "+50金币";
        float f = 0.01f;
        try {
            f = Float.parseFloat(award) * 10000;
            awardDes = String.format("%.0f",f);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvAward.setText(String.format("+%s金币", awardDes));

        tvAward.postDelayed(() -> {
            disMissDialog();
        }, 2500);

    }

    public IntegralAwardDialog setAward(String award) {
        this.award = award;
        return this;
    }

    @Override
    public IntegralAwardDialog setBackgroundDim(boolean backgroundDim) {
        super.setBackgroundDim(backgroundDim);
        return this;
    }

}
