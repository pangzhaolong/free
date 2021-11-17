package com.donews.home.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.donews.home.fragment.TbFragment;

import java.util.HashMap;
import java.util.Map;

public class SearchFragmentAdapter extends FragmentStateAdapter {

    private final Map<Integer, Fragment> mFragmentMap = new HashMap<>();

    public void search(String keyWord, int position) {
        Fragment fragment;
        for (int i = 0; i < 3; i++) {
            fragment = getFragment(i);
            if (fragment == null) {
                continue;
            }
            if (i == position) {
                ((TbFragment) fragment).search(keyWord);
            }
        }
    }

    public void showHistorySearch(String keyWord, int position) {
        for (int i = 0; i < 3; i++) {
            Fragment fragment = mFragmentMap.get(i);
            if (fragment == null) {
                continue;
            }
            if (i == position) {
                ((TbFragment) fragment).showHistorySearchData(keyWord);
            } /*else {
                ((TbFragment) mFragmentMap.get(0)).showHistorySearchData(keyWord);
            }*/
        }

    }

    public SearchFragmentAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    private Fragment getFragment(int position) {
        if (mFragmentMap.get(position) == null) {
            mFragmentMap.put(position, new TbFragment(position));
        }
        return mFragmentMap.get(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return getFragment(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
