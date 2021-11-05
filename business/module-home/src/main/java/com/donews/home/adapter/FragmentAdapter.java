package com.donews.home.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.donews.home.bean.HomeBean;
import com.donews.home.fragment.NorFragment;
import com.donews.home.fragment.TopFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentAdapter extends FragmentStateAdapter {

    private List<HomeBean.CategoryItem> list = new ArrayList<>();
    private Map<String, Fragment> mFragmentMap = new HashMap<>();

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<HomeBean.CategoryItem> list) {
//        if (!equalsList(this.list, list)) {
            this.list.clear();
//        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    private boolean equalsList(List<HomeBean.CategoryItem> list1, List<HomeBean.CategoryItem> list2) {
        if ((list1 == null && list2 != null) || (list1 != null && list2 == null)) {
            return false;
        }
        if (list1.size() != list2.size()) {
            return false;
        }

        HomeBean.CategoryItem[] arr1 = list1.toArray(new HomeBean.CategoryItem[]{});

        HomeBean.CategoryItem[] arr2 = list2.toArray(new HomeBean.CategoryItem[]{});

        Arrays.sort(arr1);

        Arrays.sort(arr1);

        return Arrays.equals(arr1, arr2);
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
