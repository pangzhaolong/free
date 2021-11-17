package com.donews.home.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.donews.home.R;
import com.donews.home.listener.SearchListener;


public class SearchHistoryItem extends LinearLayout implements View.OnClickListener {

    private SearchListener mListener;
    private TextView mTextView;

    public SearchHistoryItem(@NonNull Context context, String text, SearchListener listener) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.home_search_history_item, this, true);

        mTextView = findViewById(R.id.home_search_history_tv);

        mTextView.setText(text);

        mListener = listener;

        this.setOnClickListener(this);
        this.setTag(text);
    }

    @Override
    public void onClick(View v) {
        mListener.onClick((String) v.getTag());
    }
}
