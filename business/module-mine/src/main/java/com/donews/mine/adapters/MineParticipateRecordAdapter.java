package com.donews.mine.adapters;

import android.graphics.Paint;
import android.view.ViewGroup;
import android.widget.TextView;

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
 * 参与记录 的适配器
 */
public class MineParticipateRecordAdapter extends BaesLoadMoreAdapter<Object, BaseViewHolder> {

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder ho = super.onCreateViewHolder(parent, viewType);
        SectionCornerMessageLayout corenrLayout = ho.itemView.findViewById(R.id.mine_par_reco_list_icon_layout);
        if (corenrLayout != null) {
            corenrLayout.setCornerRadius(ConvertUtils.dp2px(10));
            corenrLayout.setTopCornerMode();
            //添加删除线
            TextView oldTv = ho.getView(R.id.mine_par_reco_list_count_old);
            oldTv.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        }
        return ho;
    }

    public MineParticipateRecordAdapter() {
        super(R.layout.mine_participate_record_list_item);
    }

    public MineParticipateRecordAdapter(int layoutResId, @Nullable List<Object> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, @Nullable Object o) {

    }
}
