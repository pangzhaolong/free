package com.donews.mine.adapters;

import android.graphics.Paint;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donews.base.utils.ToastUtil;
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

        baseViewHolder.setText(R.id.mine_win_item_snap_number,reDrawNumberText(
                "0123456",new String[]{"0883999","1828996"}
        ));
        baseViewHolder.itemView.findViewById(R.id.mine_win_item_goto)
                .setOnClickListener((v) -> {
                    ToastUtil.show(baseViewHolder.itemView.getContext(), "中奖几率->理解抢购");
                });
    }

    /**
     * 重新绘制开奖码的文字,要讲指定的文字重点着色
     * @param winNumber 本期中奖的号码
     * @param showNumberString 显示的文字对象
     */
    private CharSequence reDrawNumberText(String winNumber,String[] showNumberString){
        return "";
    }
}
