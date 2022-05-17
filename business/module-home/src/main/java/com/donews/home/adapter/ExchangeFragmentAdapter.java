package com.donews.home.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.donews.home.fragment.ExchangeTabFragment;
import com.donews.home.fragment.NorFragment;
import com.donews.middle.bean.home.HomeCategory2Bean;
import com.donews.middle.bean.home.HomeCategoryBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExchangeFragmentAdapter extends FragmentStateAdapter {

    private List<HomeCategory2Bean.Category2Item> list = new ArrayList<>();
    private Map<String, Fragment> mFragmentMap = new HashMap<>();

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<HomeCategory2Bean.Category2Item> list) {
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

    public ExchangeFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    private Fragment mkFragment(HomeCategory2Bean.Category2Item categoryItem) {
        /*if (categoryItem == null) {
            if (mFragmentMap.get(null) == null) {
                mFragmentMap.put(null, new TopFragment());
            }
            return mFragmentMap.get(null);
        } else {*/
        if (mFragmentMap.get(categoryItem.category_id) == null) {
            mFragmentMap.put(categoryItem.category_id, new ExchangeTabFragment(categoryItem));
        }
        return mFragmentMap.get(categoryItem.category_id);
        /*}*/
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        /*if (position == 0) {
            return mkFragment(null);
        } else {
            return mkFragment(this.list.get(position - 1));
        }*/
        return mkFragment(this.list.get(position));
    }

    @Override
    public int getItemCount() {
        /*if (this.list == null || this.list.size() == 0) {
            return 1;
        } else {
            return this.list.size() + 1;
        }*/
        return this.list.size();
    }
}
