package com.donews.home;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterFragmentPath;
import com.donews.home.adapter.ExchangeFragmentAdapter;
import com.donews.home.databinding.ExchanageFragmentBinding;
import com.donews.home.viewModel.ExchangeViewModel;
import com.donews.middle.bean.home.HomeCategoryBean;
import com.donews.middle.cache.GoodsCache;
import com.donews.middle.front.FrontConfigManager;
import com.donews.middle.views.ExchanageTabItem;
import com.donews.middle.views.TaskView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * @author lcl
 * Date on 2022/5/12
 * Description:
 * 首页 -> 兑换Fragment
 */
@Route(path = RouterFragmentPath.Home.PAGER_EXCHANGE_FRAGMENT)
public class ExchangeFragment extends MvvmLazyLiveDataFragment<ExchanageFragmentBinding, ExchangeViewModel> {

    private HomeCategoryBean mHomeBean;
    // Tab的Fragment
    private ExchangeFragmentAdapter mFragmentAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.exchanage_fragment;
    }

    @Override
    protected void onFragmentFirstVisible() {
        initDataBinding();
        // 运营位
        if (FrontConfigManager.Ins().getConfigBean().getTask()) {
            mDataBinding.taskGroupLlNew.setVisibility(View.VISIBLE);
            mDataBinding.taskGroupLlNew.refreshYyw(TaskView.Place_Front);
        } else {
            mDataBinding.taskGroupLlNew.setVisibility(View.GONE);
        }
        mFragmentAdapter = new ExchangeFragmentAdapter(this);
        mDataBinding.homeCategoryVp2.setAdapter(mFragmentAdapter);
        // Tab相关列表
        mDataBinding.homeCategoryTl.setTabMode(TabLayout.MODE_SCROLLABLE);
        TabLayoutMediator tab = new TabLayoutMediator(mDataBinding.homeCategoryTl, mDataBinding.homeCategoryVp2, (tab1, position) -> {
            /*if (position == 0) {
                if (tab1.getCustomView() == null) {
                    tab1.setCustomView(new TabItem(mContext));
                }
                TabItem tabItem = (TabItem) tab1.getCustomView();
                assert tabItem != null;
                tabItem.setTitle("精品");
            } else {*/

            if (tab1.getCustomView() == null) {
                tab1.setCustomView(new ExchanageTabItem(getActivity()));
            }
            ExchanageTabItem tabItem = (ExchanageTabItem) tab1.getCustomView();
            assert tabItem != null;
            if (mHomeBean == null || mHomeBean.getList() == null) {
                return;
            }
            tabItem.setTitle(mHomeBean.getList().get(position).getCname());
            /*}*/
        });
        tab.attach();
        // tab选择的监听
        mDataBinding.homeCategoryTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ExchanageTabItem tabItem = (ExchanageTabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.selected();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ExchanageTabItem tabItem = (ExchanageTabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.unSelected();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                ExchanageTabItem tabItem = (ExchanageTabItem) tab.getCustomView();
                assert tabItem != null;
                tabItem.selected();
            }
        });

        //初始化数据
        loadCategory();
    }

    //获取搜索左侧的显示文字
    public String getSearchLeftText(){
        return "?";
    }

    //搜索框左侧的点击
    public void getSearchLeftClick(){
    }

    //搜索框的点击操作
    public void getSearchInputEditClick(){
    }

    //搜索框右侧按钮点击
    public void getSearchRightClick(){
    }

    //初始化DataBinding
    private void initDataBinding() {
        mDataBinding.setThiz(this);
    }

    //加载Tab页面
    private void loadCategory() {
        mHomeBean = GoodsCache.readGoodsBean(HomeCategoryBean.class, "exchanage_category");
        if (mHomeBean != null && mHomeBean.getList() != null && mFragmentAdapter != null) {
            mFragmentAdapter.refreshData(mHomeBean.getList());
        }
        mViewModel.getHomeCategoryBean().observe(this, homeBean -> {
            if (homeBean == null) {
                return;
            }
            mHomeBean = homeBean;
            // 处理正常的逻辑。
            if (mFragmentAdapter != null) {
                mFragmentAdapter.refreshData(mHomeBean.getList());
            }
            GoodsCache.saveGoodsBean(mHomeBean, "exchanage_category");
        });
    }
}
