package com.donews.mine.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.mine.R;
import com.donews.mine.databinding.MineActivityWinningCodeBinding;
import com.donews.mine.viewModel.MineWinningCodeViewModel;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.gyf.immersionbar.ImmersionBar;

/**
 * 个人中心的开奖码
 */
@Route(path = RouterActivityPath.Mine.PAGER_MINE_WINNING_CODE_ACTIVITY)
public class MineWinningCodeActivity extends
        MvvmBaseLiveDataActivity<MineActivityWinningCodeBinding, MineWinningCodeViewModel> {

    @Autowired(name = "period")
    public int period;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_winning_code;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .navigationBarColor(R.color.white)
                .fitsSystemWindows(false)
                .autoDarkModeEnable(true)
                .init();
        AnalysisUtils.onEventEx(this, Dot.Page_Award); //中奖页
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void initView() {
        ARouter.getInstance().inject(this);
        Fragment f = RouterFragmentPath.User.getMineOpenWinFragment(
                period,true,false);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mine_win_frm,f);
        ft.commitAllowingStateLoss();
    }

    private void initData() {
    }
}