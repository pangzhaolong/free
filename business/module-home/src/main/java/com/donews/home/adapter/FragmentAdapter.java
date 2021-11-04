package com.donews.home.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.donews.home.bean.HomeBean;
import com.donews.home.fragment.NorFragment;
import com.donews.home.fragment.TopFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentAdapter extends FragmentStateAdapter {

    private List<HomeBean.CategoryItem> list = new ArrayList<>();
    private Map<String, Fragment> mFragmentMap = new HashMap<>();

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<HomeBean.CategoryItem> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        if (this.list != null) {
            this.list.clear();
            this.list = null;
        }

        if (mFragmentMap != null) {
            mFragmentMap.clear();
            mFragmentMap = null;
        }
    }

    public FragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    private Fragment mkFragment(HomeBean.CategoryItem categoryItem) {
        if (categoryItem == null) {
            if (mFragmentMap.get(null) == null) {
                mFragmentMap.put(null, new TopFragment());
            }
            return mFragmentMap.get(null);
        } else {
            if (mFragmentMap.get(categoryItem.getCid()) == null) {
                mFragmentMap.put(categoryItem.getCid(), new NorFragment(categoryItem));
            }
            return mFragmentMap.get(categoryItem.getCid());
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return mkFragment(null);
        } else {
            return mkFragment(this.list.get(position - 1));
        }
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
