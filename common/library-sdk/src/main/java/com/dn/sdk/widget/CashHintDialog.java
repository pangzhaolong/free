package com.dn.sdk.widget;


import com.dn.drouter.ARouteHelper;
import com.dn.sdk.R;
import com.dn.sdk.databinding.SdkCashHintDialogBinding;
import com.donews.base.fragmentdialog.AbstractFragmentDialog;
import com.donews.common.router.RouterActivityPath;
import com.jakewharton.rxbinding4.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2021/3/30 15:06<br>
 * 版本：V1.0<br>
 */
public class CashHintDialog extends AbstractFragmentDialog<SdkCashHintDialogBinding> {
    private CompositeDisposable composite;
    private String titleName;
    private String okName;

    public CashHintDialog(String titleName, String okName) {
        super(false, false);
        this.okName = okName;
        this.titleName = titleName;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sdk_cash_hint_dialog;
    }

    @Override
    protected void initView() {
        composite = new CompositeDisposable();

        composite.add(RxView.clicks(dataBinding.ok)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(unit -> {
                    ARouteHelper.build(RouterActivityPath.ClassPath.METHOD_FRAGMENT_SET_POSITION).invoke(0);
                    disMissDialog();
                }));
        dataBinding.setOkName(okName);
        dataBinding.setTitleName(titleName);
    }

    @Override
    protected boolean isUseDataBinding() {
        return true;
    }


}
