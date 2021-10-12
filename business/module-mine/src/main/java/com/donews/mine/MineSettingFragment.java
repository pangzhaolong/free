package com.donews.mine;

import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.base.fragment.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterFragmentPath;
import com.donews.mine.databinding.MineSettingFragmentBinding;
import com.donews.mine.viewModel.SettingFragmentViewModel;


/**
 * 作者： created by lcl<br>
 *  * 日期： 2021/10/10 9:13<br>
 *  * 版本：V1.0<br>
 */
@Route(path = RouterFragmentPath.User.PAGER_USER_SETTING)
public class MineSettingFragment extends MvvmLazyLiveDataFragment<MineSettingFragmentBinding, SettingFragmentViewModel> {


    @Override
    protected void onFragmentFirstVisible() {
        super.onFragmentFirstVisible();
        initView();
        mViewModel.lifecycleOwner = this;

    }

    @Override
    public int getLayoutId() {
        return R.layout.mine_setting_fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        bindViewText();
        onRefresh();
    }


    private void onRefresh(){
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void initView() {
        mViewModel.setDataBinDing(mDataBinding, getBaseActivity());
    }

    //绑定视图和文字
    private void bindViewText() {
        //列表数据绑定
        int itemViewCount = 0;
        for (int i = 0; i < mDataBinding.setListLayout.getChildCount(); i++) {
            View item = mDataBinding.setListLayout.getChildAt(i);
            if(item.getTag() == null || item.getTag().toString().isEmpty()){
                continue;
            }
            TextView tvTitle = item.findViewById(R.id.tv_name);
            TextView tvDesc = item.findViewById(R.id.tv_right_desc);
            tvTitle.setText(mViewModel.getItemTitleName(itemViewCount));
            tvDesc.setText(mViewModel.getItemDescText(itemViewCount));
            mViewModel.addItemClick(item,itemViewCount);
            itemViewCount++;
        }
    }
}
