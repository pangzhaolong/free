package com.donews.lotterypage.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.doing.spike.bean.SpikeBean;
import com.donews.base.viewmodel.BaseLiveDataViewModel;
import com.donews.common.base.MvvmLazyLiveDataFragment;
import com.donews.common.router.RouterFragmentPath;
import com.donews.front.model.FrontModel;
import com.donews.lotterypage.Adapter.ContentAdapter;
import com.donews.lotterypage.R;
import com.donews.lotterypage.base.BaseFragment;
import com.donews.lotterypage.base.LotteryPageBean;
import com.donews.lotterypage.databinding.LotteryPageLayoutBinding;
import com.donews.lotterypage.databinding.TestLayoutBinding;
import com.donews.lotterypage.viewmodel.LotteryPageViewModel;


public class testFrament extends MvvmLazyLiveDataFragment<LotteryPageLayoutBinding, LotteryPageViewModel> {
    TestLayoutBinding mdata;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mdata = DataBindingUtil.inflate(inflater, R.layout.test_layout, container, false);


        mViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<LotteryPageBean>() {
            @Override
            public void onChanged(LotteryPageBean lotteryPageBean) {

            }
        });


        return mdata.getRoot();
    }

    @Override
    public int getLayoutId() {
        return 0;
    }

}
