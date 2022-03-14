package com.donews.mine.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donews.mine.R;
import com.donews.mine.bean.resps.HistoryPeopleLotteryDetailResp;
import com.donews.mine.views.refresh.adapters.BaesLoadMoreAdapter;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/18
 * Description:
 * 我的抽奖记录 的适配器
 */
public class MineMyAddRecordAdapter extends BaesLoadMoreAdapter<HistoryPeopleLotteryDetailResp.WinerDTO, BaseViewHolder> {

    public MineMyAddRecordAdapter() {
        super(R.layout.mine_my_add_record_list_item);
    }

    public MineMyAddRecordAdapter(int layoutResId, @Nullable List<HistoryPeopleLotteryDetailResp.WinerDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, @Nullable HistoryPeopleLotteryDetailResp.WinerDTO winerDTO) {

    }
}
