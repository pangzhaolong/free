package com.donews.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.donews.home.R;
import com.donews.middle.bean.home.HomeGridCategoryBean;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class GridAdapter extends BaseAdapter {

    private final Context mContext;
    private List<HomeGridCategoryBean> mSpelCategoryList = new ArrayList<>();

    public GridAdapter(Context context) {
        mContext = context;
    }

    public void refreshData(List<HomeGridCategoryBean> list) {
        mSpelCategoryList.clear();
        mSpelCategoryList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSpelCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mSpelCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.home_fragment_top_grid_item, null);
            holder = new ItemViewHolder();
            holder.logoIv = convertView.findViewById(R.id.home_top_grid_item_iv);
            holder.logoTv = convertView.findViewById(R.id.home_top_grid_item_tv);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }

        Glide.with(mContext).load(UrlUtils.formatUrlPrefix(mSpelCategoryList.get(position).getImg())).into(holder.logoIv);
        holder.logoTv.setText(mSpelCategoryList.get(position).getTitle());

        return convertView;
    }

    public static class ItemViewHolder {
        ImageView logoIv;
        TextView logoTv;
    }
}





















