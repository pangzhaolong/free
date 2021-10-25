package com.donews.mine.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.donews.mine.R;
import com.donews.mine.bean.CSBean;

import java.util.ArrayList;
import java.util.List;

public class CSSView extends LinearLayout {

    private List<CSBean> mCsBeanList = new ArrayList<>();

    private CCSItemView[] mCcsItemViews = new CCSItemView[7];

    public CSSView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mCsBeanList.add(new CSBean("北京", "20.02"));
        mCsBeanList.add(new CSBean("上海", "20.02"));
        mCsBeanList.add(new CSBean("广州", "20.02"));
        mCsBeanList.add(new CSBean("杭州", "20.02"));
        mCsBeanList.add(new CSBean("武汉", "20.02"));
        mCsBeanList.add(new CSBean("成都", "20.02"));
        mCsBeanList.add(new CSBean("郑州", "20.02"));

        LayoutInflater.from(context).inflate(R.layout.mine_city_car_speed, this, true);
        mCcsItemViews[0] = findViewById(R.id.mine_css_item1);
        mCcsItemViews[1] = findViewById(R.id.mine_css_item2);
        mCcsItemViews[2] = findViewById(R.id.mine_css_item3);
        mCcsItemViews[3] = findViewById(R.id.mine_css_item4);
        mCcsItemViews[4] = findViewById(R.id.mine_css_item5);
        mCcsItemViews[5] = findViewById(R.id.mine_css_item6);
        mCcsItemViews[6] = findViewById(R.id.mine_css_item7);
    }

    public void refreshData(List<CSBean> list) {
        mCsBeanList.clear();
        mCsBeanList.addAll(list);
        if (mCsBeanList.size() != 7) {
            return;
        }

        for (int i = 0; i < 7; i++) {
            mCcsItemViews[i].setCityAndSpeed(mCsBeanList.get(i));
        }
    }

    public void refreshData() {
        refreshData(mCsBeanList);
    }

}
