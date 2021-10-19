package com.donews.mine.adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donews.mine.R;
import com.donews.mine.views.refresh.adapters.BaesLoadMoreAdapter;
import com.donews.mine.views.SectionCornerMessageLayout;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/18
 * Description:
 *  个人中心 -> 精选推荐 适配器
 */
public class MineFragmentAdapter extends BaesLoadMoreAdapter<Object, BaseViewHolder> {

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder ho = super.onCreateViewHolder(parent, viewType);
        SectionCornerMessageLayout corenrLayout  = ho.itemView.findViewById(R.id.mine_me_list_icon_layout);
        if(corenrLayout != null){
            corenrLayout.setCornerRadius(ConvertUtils.dp2px(10));
            corenrLayout.setTopCornerMode();
        }
        return ho;
    }

    public MineFragmentAdapter() {
        super(R.layout.mine_fragment_list_item);
    }

    public MineFragmentAdapter(int layoutResId, @Nullable List<Object> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, @Nullable Object o) {
    }
}
