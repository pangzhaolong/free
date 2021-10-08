package com.dn.sdk.widget;

import android.annotation.SuppressLint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import androidx.fragment.app.FragmentActivity;

import com.dn.sdk.R;
import com.dn.sdk.databinding.SdkDialogTryPlayHintBinding;
import com.donews.base.fragmentdialog.AbstractFragmentDialog;

/**
 * @author by SnowDragon
 * Date on 2021/3/30
 * Description:
 */
public class TryPlayHintDialog extends AbstractFragmentDialog<SdkDialogTryPlayHintBinding> {

    public static void showDialog(FragmentActivity activity, int seconds, String award, SureListener sureListener, CancelListener cancelListener) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        TryPlayHintDialog netHintDialog = new TryPlayHintDialog();
        netHintDialog.setSeconds(seconds)
                .setAward(award)
                .setSureListener(sureListener)
                .setCancelListener(cancelListener);

        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(netHintDialog, "netHintDialog")
                .commitAllowingStateLoss();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sdk_dialog_try_play_hint;
    }

    @Override
    protected void initView() {
        dataBinding.tvGiveUp.setOnClickListener(v -> {
            if (cancelListener!=null){
                cancelListener.onCancel();
            }
            disMissDialog();
        });

        dataBinding.tvGoOn.setOnClickListener(v -> {
            if (sureListener != null) {
                sureListener.onSure();
            }

            disMissDialog();
        });

        setSpan();

    }

    @SuppressLint("DefaultLocale")
    private void setSpan() {
        if (TextUtils.isEmpty(award)) {
            award = "0.3";
        }
        String content = String.format("您试用APP还差%d秒\n即可获取%s元奖励", seconds, award);
        int index1 = content.indexOf(seconds + "");
        int index2 = content.indexOf(award);

        SpannableString span = new SpannableString(content);

        span.setSpan(new ForegroundColorSpan(0xffFF5A00), index1, index1 + String.valueOf(seconds).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(0xffFF5A00), index2, index2 + award.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        dataBinding.tvContent.setText(span);
    }

    private int seconds;

    public TryPlayHintDialog setSeconds(int seconds) {
        this.seconds = seconds;
        return this;

    }

    private String award;

    public TryPlayHintDialog setAward(String award) {
        this.award = award;
        return this;
    }

    @Override
    protected boolean isUseDataBinding() {
        return true;
    }

    private CancelListener cancelListener;

    public TryPlayHintDialog setCancelListener(CancelListener cancelListener) {
        this.cancelListener = cancelListener;
        return this;
    }

    private SureListener sureListener;

    public TryPlayHintDialog setSureListener(SureListener sureListener) {
        this.sureListener = sureListener;
        return this;

    }

}
