package com.donews.mine.adapters;

import android.graphics.Paint;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.donews.base.utils.ToastUtil;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.common.router.RouterFragmentPath;
import com.donews.mine.R;
import com.donews.mine.bean.resps.RecommendGoodsResp;
import com.donews.mine.views.SectionCornerMessageLayout;
import com.donews.mine.views.refresh.adapters.BaesLoadMoreAdapter;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/18
 * Description:
 */
public class MineWinningCodeAdapter extends BaesLoadMoreAdapter<RecommendGoodsResp.ListDTO, BaseViewHolder> {

    public MineWinningCodeAdapter() {
        super(R.layout.mine_open_win_good_list_item);
    }

    public MineWinningCodeAdapter(int layoutResId, @Nullable List<RecommendGoodsResp.ListDTO> data) {
        super(layoutResId, data);
    }

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

    @Override
    protected void convert(@NonNull BaseViewHolder holder, @Nullable RecommendGoodsResp.ListDTO item) {
        holder.setText(R.id.mine_par_reco_list_title, item.title)
                .setText(R.id.mine_par_reco_list_count_old, "¥" + item.originalPrice);
        ImageView iv = holder.getView(R.id.mine_par_reco_list_icon);
        GlideUtils.loadImageView(
                iv.getContext(), UrlUtils.formatUrlPrefix(item.mainPic), iv);
        holder.getView(R.id.mine_par_reco_list_bot_info)
                .setOnClickListener(v -> {
                    ARouter.getInstance()
                            .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                            .withString("goods_id", item.goodsId)
                            .navigation();
                });
    }
}
