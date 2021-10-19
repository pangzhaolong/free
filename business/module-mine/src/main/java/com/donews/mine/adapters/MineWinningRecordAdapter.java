package com.donews.mine.adapters;

import android.graphics.Paint;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donews.mine.R;
import com.donews.mine.views.SectionCornerMessageLayout;
import com.donews.mine.views.refresh.adapters.BaesLoadMoreAdapter;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/18
 * Description:
 * 参与记录 的适配器
 */
public class MineWinningRecordAdapter extends BaesLoadMoreAdapter<Object, BaseViewHolder> {

    public MineWinningRecordAdapter() {
        super(R.layout.mine_winning_record_list_item);
    }

    public MineWinningRecordAdapter(int layoutResId, @Nullable List<Object> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, @Nullable Object o) {

    }
}
