package com.donews.mine.ui;

import static com.donews.common.router.RouterActivityPath.Mine.PAGER_MINE_ABOUT_ACTIVITY;
import static com.donews.common.router.RouterActivityPath.Mine.PAGER_MINE_MY_ADD_RECORD_LIST_ACTIVITY;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.AppUtils;
import com.dn.drouter.ARouteHelper;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.adapter.ScreenAutoAdapter;
import com.donews.common.base.MvvmBaseLiveDataActivity;
import com.donews.common.router.RouterActivityPath;
import com.donews.mine.BuildConfig;
import com.donews.mine.R;
import com.donews.mine.adapters.MineMyAddRecordAdapter;
import com.donews.mine.bean.resps.HistoryPeopleLotteryDetailResp;
import com.donews.mine.databinding.MineActivityAboutBinding;
import com.donews.mine.databinding.MineActivityMyAddRecordListBinding;
import com.donews.network.EasyHttp;
import com.donews.network.cache.model.CacheMode;
import com.donews.network.callback.SimpleCallBack;
import com.donews.network.exception.ApiException;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.JsonUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

/**
 * <p>
 * 我的参与记录。更多参与记录详情
 * <p>
 */
@Route(path = PAGER_MINE_MY_ADD_RECORD_LIST_ACTIVITY)
public class MyAddRecordListActivity extends MvvmBaseLiveDataActivity<MineActivityMyAddRecordListBinding, BaseLiveDataViewModel> {

    /**
     * 期数
     */
    @Autowired(name = "proid")
    int proid = 0;//期数

    MineMyAddRecordAdapter adapter;

    @Override
    protected int getLayoutId() {
        ScreenAutoAdapter.match(this, 375.0f);
        ImmersionBar.with(this)
                .statusBarColor(R.color.mine_f6f9fb)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .autoDarkModeEnable(true)
                .init();
        return R.layout.mine_activity_my_add_record_list;
    }

    @Override
    public void initView() {
        ARouter.getInstance().inject(this);
        mDataBinding.mainAboutBack.setOnClickListener((v) -> {
            finish();
        });
        mDataBinding.mineAddRecoRefesh.setEnableLoadMore(false);
        mDataBinding.mineAddRecoRefesh.setEnableRefresh(true);
        mDataBinding.mineAddRecoRefesh.setOnRefreshListener(refreshLayout -> {
            requestHistoryPeopleLootteryDetail();
        });
        mDataBinding.mineAddRecoTitle.setText(proid + "期");
        adapter = new MineMyAddRecordAdapter();
        mDataBinding.addRecordList.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.addRecordList.setAdapter(adapter);
        mDataBinding.mineAddRecoRefesh.autoRefresh();
    }

    /**
     * 个人中心，往期中奖详情(此接口需要登录)
     *
     * @return
     */
    public Disposable requestHistoryPeopleLootteryDetail() {
        Disposable disop = EasyHttp.get(BuildConfig.API_LOTTERY_URL + "v1/history-people-lottery-detail")
                .params("period", "" + proid)
                .isShowToast(false)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<HistoryPeopleLotteryDetailResp>() {
                    @Override
                    public void onError(ApiException e) {
                        adapter.setNewData(new ArrayList<>());
                    }

                    @Override
                    public void onSuccess(HistoryPeopleLotteryDetailResp queryBean) {
                        adapter.setNewData(queryBean.record);
                    }
                });
        return disop;
    }
}
