package com.donews.mine.adapters;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.BaseLoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donews.base.utils.ToastUtil;
import com.donews.mine.R;
import com.donews.mine.bean.resps.WithdrawRecordResp;
import com.donews.mine.views.refresh.adapters.BaesLoadMoreAdapter;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/18
 * Description:
 * 提现记录
 */
public class MineWithdrawalRecordAdapter extends BaesLoadMoreAdapter<WithdrawRecordResp.RecordListDTO, BaseViewHolder> {

    public MineWithdrawalRecordAdapter() {
        super(R.layout.mine_activity_withdrawal_record_list_item);
    }

    public MineWithdrawalRecordAdapter(int layoutResId, @Nullable List<WithdrawRecordResp.RecordListDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, @Nullable WithdrawRecordResp.RecordListDTO item) {
        baseViewHolder.setText(R.id.mine_withdraw_item_title, item.comment)
                .setText(R.id.mine_withdraw_item_time, item.time);
        TextView tv = baseViewHolder.getView(R.id.mine_withdraw_item_num);
        if (item.score >= 0) {
            tv.setTextColor(getContext().getResources().getColor(R.color.text_red_1));
            baseViewHolder.setText(R.id.mine_withdraw_item_num, "+" + item.score + item.unit);
        } else {
            tv.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
            baseViewHolder.setText(R.id.mine_withdraw_item_num, "" + item.score + item.unit);
        }
    }

    @NonNull
    @Override
    public BaseLoadMoreModule addLoadMoreModule(@NonNull BaseQuickAdapter<?, ?> baseQuickAdapter) {
        return new BaseLoadMoreModule(baseQuickAdapter);
    }
}
