package com.dn.sdk.widget;

import androidx.fragment.app.FragmentActivity;

import com.dn.sdk.R;
import com.dn.sdk.databinding.SdkDialogNetHintBinding;
import com.donews.base.fragmentdialog.AbstractFragmentDialog;

/**
 * @author by SnowDragon
 * Date on 2021/3/30
 * Description:
 */
public class NetHintDialog extends AbstractFragmentDialog<SdkDialogNetHintBinding> {

    public static void showDialog(FragmentActivity activity, SureListener sureListener) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        NetHintDialog netHintDialog = new NetHintDialog();
        netHintDialog.setSureListener(sureListener);

        activity.getSupportFragmentManager()
                .beginTransaction()
                .add(netHintDialog, "netHintDialog")
                .commitAllowingStateLoss();
    }

    public NetHintDialog() {
        super(true, true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sdk_dialog_net_hint;
    }

    @Override
    protected void initView() {
        dataBinding.tvGiveUp.setOnClickListener(v -> disMissDialog());

        dataBinding.tvGoOn.setOnClickListener(v -> {
            if (sureListener != null) {
                sureListener.onSure();
            }

            disMissDialog();
        });

    }

    private SureListener sureListener;

    public NetHintDialog setSureListener(SureListener sureListener) {
        this.sureListener = sureListener;
        return this;

    }

    @Override
    protected boolean isUseDataBinding() {
        return true;
    }
}
