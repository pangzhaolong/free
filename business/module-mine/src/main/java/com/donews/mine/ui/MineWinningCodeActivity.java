package com.donews.mine.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.bean.globle.TurntableBean;
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
    /**
     * 来源，
     * 1：首页
     * 2：往期开奖
     * 3：个人参与记录
     */
    @Autowired(name = "from")
    public int from = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_activity_winning_code;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this)
                .statusBarColor(R.color.transparent)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(false)
                .autoDarkModeEnable(true)
                .init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void initView() {
        ARouter.getInstance().inject(this);
        Fragment f = null;
        if (from == 2) {
            //往期进入。不显示更多(往期)
            f = RouterFragmentPath.User.getMineOpenWinFragment(
                    period, false, true, false, from);
        } else {
            f = RouterFragmentPath.User.getMineOpenWinFragment(
                    period, false, true, true, from);
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mine_win_frm, f);
        ft.commitAllowingStateLoss();
    }

    private void initData() {
    }
}