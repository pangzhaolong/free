package com.donews.home.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.donews.home.bean.HomeBean;
import com.donews.home.fragment.NorFragment;
import com.donews.home.fragment.TbFragment;
import com.donews.home.fragment.TopFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchFragmentAdapter extends FragmentStateAdapter {

    private final List<HomeBean.CategoryItem> list = new ArrayList<>();
    private final Map<Integer, Fragment> mFragmentMap = new HashMap<>();

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<HomeBean.CategoryItem> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

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
        if (this.list == null || this.list.size() == 0) {
            return 1;
        } else {
            return this.list.size() + 1;
        }
    }
}
