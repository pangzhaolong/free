package com.donews.lotterypage.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.donews.common.router.RouterFragmentPath;
import com.donews.lotterypage.base.BaseFragment;
import com.module_lottery.R;
import com.module_lottery.databinding.TestLayoutBinding;


@Route(path = RouterFragmentPath.HomeLottery.PAGER_LOTTERY)
public class testFrament extends Fragment {
    TestLayoutBinding mdata;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mdata = DataBindingUtil.inflate(inflater, R.layout.test_layout, container, false);
        return mdata.getRoot();
    }

}
