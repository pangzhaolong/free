package com.donews.home.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.donews.home.fragment.CategoryFragment;

public class FragmentCategoryAdapter extends FragmentStateAdapter {

    public FragmentCategoryAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        return new CategoryFragment(position);
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
