package com.donews.main.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.common.router.RouterActivityPath;
import com.donews.main.R;
import com.gyf.immersionbar.BarHide;
import com.gyf.immersionbar.ImmersionBar;

@Route(path = RouterActivityPath.Rp.PAGE_RP)
public class RpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_rp_activity);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .navigationBarColor(R.color.transparent)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .init();
    }
}