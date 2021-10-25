package com.donews.front.adapter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.donews.front.GiftFragment;
import com.donews.front.NorFragment;
import com.donews.front.bean.LotteryCategoryBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentAdapter extends FragmentStateAdapter {

    private final List<LotteryCategoryBean.categoryBean> list = new ArrayList<>();
    private final Map<String, Fragment> mFragmentMap = new HashMap<>();

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<LotteryCategoryBean.categoryBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public FragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    private Fragment mkFragment(LotteryCategoryBean.categoryBean categoryItem) {
        /*if (categoryItem == null) {
            if (mFragmentMap.get(null) == null) {
                mFragmentMap.put(null, new GiftFragment());
            }
            return mFragmentMap.get(null);
        }  else {
            if (mFragmentMap.get(categoryItem.getCategoryId()) == null) {
                mFragmentMap.put(categoryItem.getCategoryId(), new NorFragment(categoryItem));
            }
            return mFragmentMap.get(categoryItem.getCategoryId());
        }*/
        if (mFragmentMap.get(categoryItem.getCategoryId()) == null) {
            mFragmentMap.put(categoryItem.getCategoryId(), new NorFragment(categoryItem));
        }
        return mFragmentMap.get(categoryItem.getCategoryId());
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
