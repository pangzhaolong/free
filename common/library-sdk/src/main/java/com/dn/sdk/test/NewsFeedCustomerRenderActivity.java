package com.dn.sdk.test;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dn.sdk.AdLoadManager;
import com.dn.sdk.R;
import com.dn.sdk.constant.AdIdConfig;
import com.dn.sdk.databinding.ActivityNewsFeedBinding;
import com.dn.sdk.listener.IAdNewsFeedListener;
import com.dn.sdk.widget.AdView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/11/24
 * Description:
 */
public class NewsFeedCustomerRenderActivity extends AppCompatActivity {
    List<AdView> viewList = new ArrayList<>();
    NewsFeedAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNewsFeedBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_news_feed);

        AdLoadManager.getInstance()
                .loadNewsFeedCustomerRender(this, AdIdConfig.NEWS_FEED_CUSTOM_RENDER_ID, 5,
                        new IAdNewsFeedListener() {
                            @Override
                            public void success(List<AdView> dataList) {
                                viewList.clear();
                                viewList.addAll(dataList);
                                if (adapter != null) {
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onError(String errorMsg) {

                            }
                        });

        adapter = new NewsFeedAdapter(viewList);

        RecyclerView mRecycleView = binding.recycleView;
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(adapter);

    }
}
