package com.donews.turntable.view;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.router.RouterActivityPath;
import com.donews.turntable.base.TurntableBaseActivity;

@Route(path = RouterActivityPath.Turntable.TURNTABLE_ACTIVITY)
public class TrunMainActivity extends TurntableBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
