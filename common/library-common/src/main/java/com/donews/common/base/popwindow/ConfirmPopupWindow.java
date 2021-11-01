package com.donews.common.base.popwindow;


import androidx.fragment.app.FragmentActivity;

import com.donews.base.R;


public class ConfirmPopupWindow extends AbstractConfirmPopupWindow {


    public ConfirmPopupWindow(FragmentActivity context) {
        super(context);
    }

    @Override
    public void onResume() {

    }

    @Override
    protected int layoutId() {
        return R.layout.base_popupwindow_confirm;
    }

    @Override
    protected void initContentView() {

    }


}
