package com.donews.home.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.donews.home.R;
import com.donews.home.listener.SearchListener;
import com.donews.middle.bean.home.SearchSugBean;

import java.util.ArrayList;
import java.util.List;

public class SearchSugAdapter extends RecyclerView.Adapter<SearchSugAdapter.SugViewHolder> implements View.OnClickListener {

    private final List<SearchSugBean.SugItem> mGoodsList = new ArrayList<>();

    private SearchListener mListener;

    public SearchSugAdapter(SearchListener listener) {
        mListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void refreshData(List<SearchSugBean.SugItem> list) {
        mGoodsList.clear();
        mGoodsList.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_search_sug_item, parent, false);
        final SugViewHolder holder = new SugViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SugViewHolder holder, int position) {
        if (mGoodsList.size() <= 0) {
            return;
        }

        holder.itemView.setOnClickListener(this::onClick);
        holder.itemView.setTag(position);
        holder.desTv.setText(mGoodsList.get(position).getKw());
    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();

        String info = mGoodsList.get(position).getKw();
        mListener.onClick(info);
    }

    public static class SugViewHolder extends RecyclerView.ViewHolder {

        private final TextView desTv;

        public SugViewHolder(@NonNull View itemView) {
            super(itemView);

            desTv = itemView.findViewById(R.id.home_search_sug_item_tv);
        }
    }
}
