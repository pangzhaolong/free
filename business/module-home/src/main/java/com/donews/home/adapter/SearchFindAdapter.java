package com.donews.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donews.home.R;

import java.util.ArrayList;
import java.util.List;

public class SearchFindAdapter extends RecyclerView.Adapter<SearchFindAdapter.FindViewHolder> {

    private final Context mContext;
    private final List<String> mGoodsList = new ArrayList<>();

    public SearchFindAdapter(Context context) {
        mContext = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<String> list) {
        mGoodsList.clear();
        mGoodsList.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public FindViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_fragment_search_find_item, parent, false);
        final FindViewHolder holder = new FindViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FindViewHolder holder, int position) {
        if (mGoodsList.size() <= 0) {
            return;
        }
        holder.desTv.setText(mGoodsList.get(position));
    }


    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    public static class FindViewHolder extends RecyclerView.ViewHolder {

        private final TextView desTv;

        public FindViewHolder(@NonNull View itemView) {
            super(itemView);

            desTv = itemView.findViewById(R.id.home_search_find_item_tv);
        }
    }
}
