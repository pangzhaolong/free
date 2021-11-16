package com.donews.home.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.donews.home.fragment.TbFragment;
import com.donews.middle.bean.home.HomeCategoryBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragmentAdapter extends FragmentStateAdapter {

    private final Map<Integer, Fragment> mFragmentMap = new HashMap<>();

    public void search(String keyWord) {
        ((TbFragment) mFragmentMap.get(0)).search(keyWord);
    }

    public void showHistorySearch(String keyWord) {
        ((TbFragment) mFragmentMap.get(0)).showHistorySearchData(keyWord);
    }


    public void showDefaultLayout() {
        ((TbFragment) mFragmentMap.get(0)).showDefaultLayout();
    }

    public SearchFragmentAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    private Fragment mkFragment(int position) {
        if (mFragmentMap.get(position) == null) {
            mFragmentMap.put(position, new TbFragment());
        }
        return mFragmentMap.get(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mkFragment(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
