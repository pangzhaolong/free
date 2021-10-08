package com.dn.sdk.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dn.sdk.R;
import com.dn.sdk.widget.AdView;

import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/11/24
 * Description:
 */
public class NewsFeedAdapter extends RecyclerView.Adapter {
    private List<AdView> viewList;

    public NewsFeedAdapter(List<AdView> viewList) {
        this.viewList = viewList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cus_render, null);
        return new NewsFeedHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsFeedHolder viewHolder = (NewsFeedHolder) holder;
        viewHolder.container.removeAllViews();
        viewHolder.container.addView(viewList.get(position).view);
    }

    @Override
    public int getItemCount() {
        return viewList.size();
    }

    public static class NewsFeedHolder extends RecyclerView.ViewHolder {
        public FrameLayout container;

        public NewsFeedHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);

        }
    }
}
