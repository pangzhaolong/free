package com.module.lottery.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import com.donews.base.activity.MvvmBaseLiveDataActivity;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;
import com.module.lottery.viewModel.LotteryViewModel;
import com.module.lottery.viewModel.RaidersViewModel;
import com.module_lottery.R;
import com.module_lottery.databinding.RaidersLyoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 中奖攻略
 */

public class RaidersActivity extends BaseActivity<RaidersLyoutBinding, RaidersViewModel> {
    private List<Fragment> fragments;
    private List<String> titles;

    @Override
    protected int getLayoutId() {
        return R.layout.raiders_lyout;
    }

    @Override
    public void initView() {
        titles = new ArrayList<>();
        titles.add(getBaseContext().getString(R.string.raiders_name1));
        titles.add(getBaseContext().getString(R.string.raiders_name2));
        titles.add(getBaseContext().getString(R.string.raiders_name3));
        titles.add(getBaseContext().getString(R.string.raiders_name4));
        fragments = new ArrayList<>();
        fragments.add(new Item1Fragment());
        fragments.add(new Item2Fragment());
        fragments.add(new Item3Fragment());
        fragments.add(new Item4Fragment());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(mDataBinding.aboutToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //添加默认的返回图标
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true); //设置返回键可用
         // actionBar.setDisplayShowTitleEnabled(false);
        }
        mDataBinding.raidersViewPager.setOffscreenPageLimit(3);
        //设置适配器
        mDataBinding.raidersViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的ITem
            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }

            //返回Item个数
            @Override
            public int getCount() {
                return fragments.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });


        mDataBinding.raidersTableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {

                    return;
                }
                String title = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(title);
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                spStr.setSpan(styleSpan, 0, title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == null || tab.getText() == null) {
                    return;
                }
                String title = tab.getText().toString().trim();
                SpannableString spStr = new SpannableString(title);
                StyleSpan styleSpan = new StyleSpan(Typeface.NORMAL);
                spStr.setSpan(styleSpan, 0, title.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tab.setText(spStr);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        mDataBinding.raidersTableLayout.setupWithViewPager(mDataBinding.raidersViewPager);

        mDataBinding.connectService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RaidersActivity.this, ContactCustomerActivity.class);
                RaidersActivity.this.startActivity(intent);

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }


        return true;
    }


}
