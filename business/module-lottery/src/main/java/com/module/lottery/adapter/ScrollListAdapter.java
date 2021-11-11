package com.module.lottery.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.module.lottery.bean.WinLotteryBean;
import com.module.lottery.dialog.ExhibitCodeStartsDialog;
import com.module.lottery.ui.LotteryActivity;
import com.module.lottery.utils.ImageUtils;
import com.module_lottery.R;
import com.module_lottery.databinding.GuesslikeItemLayoutBinding;
import com.module_lottery.databinding.ScrollItemLayoutBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ScrollListAdapter extends BaseAdapter<ScrollListAdapter.ScrollListHolder, ScrollItemLayoutBinding> {

    private List<WinLotteryBean.ListDTO> list = new ArrayList<>();

    private Context mContext;

    ScrollListAdapter(Context context) {
        this.mContext = context;
    }


    private ScrollListAdapter() {
    }

    @Override
    public int getLayout() {
        return R.layout.scroll_item_layout;
    }

    @Override
    public ScrollListHolder getViewHolder(ScrollItemLayoutBinding viewDataBinding) {
        return new ScrollListHolder(viewDataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ScrollListHolder holder, int position) {
        holder.mScrollItemLayout.text.setText(list.get(position % list.size()).getMessage() + "");
        ImageUtils.setImage(mContext, holder.mScrollItemLayout.head, list.get(position % list.size()).getAvatar() + "", 360);
    }


    @Override
    public int getItemCount() {
        return list == null || list.size() == 0 ? 0 : Integer.MAX_VALUE;
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull ScrollListHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }


    @Override
    public void onViewAttachedToWindow(@NonNull ScrollListHolder holder) {
        super.onViewAttachedToWindow(holder);
    }


    class ScrollListHolder extends RecyclerView.ViewHolder {
        ScrollItemLayoutBinding mScrollItemLayout;

        public ScrollListHolder(@NonNull ScrollItemLayoutBinding scrollItemLayout) {
            super(scrollItemLayout.getRoot());
            this.mScrollItemLayout = scrollItemLayout;
        }
    }

    public List<WinLotteryBean.ListDTO> getList() {
        return list;
    }

    public void setList(List<WinLotteryBean.ListDTO> list) {
        this.list = list;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

}
