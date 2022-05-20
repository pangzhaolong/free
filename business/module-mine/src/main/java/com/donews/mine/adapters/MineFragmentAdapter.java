package com.donews.mine.adapters;

import android.view.ViewGroup;
import android.widget.ImageView;

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
import com.donews.mine.utils.TextWinUtils;
import com.donews.mine.views.refresh.adapters.BaesLoadMoreAdapter;
import com.donews.mine.views.SectionCornerMessageLayout;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/18
 * Description:
 * 个人中心 -> 精选推荐 适配器
 */
public class MineFragmentAdapter extends BaesLoadMoreAdapter<RecommendGoodsResp.ListDTO, BaseViewHolder> {

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder ho = super.onCreateViewHolder(parent, viewType);
        SectionCornerMessageLayout corenrLayout = ho.itemView.findViewById(R.id.mine_me_list_icon_layout);
        if (corenrLayout != null) {
            corenrLayout.setCornerRadius(ConvertUtils.dp2px(10));
            corenrLayout.setTopCornerMode();
        }
        return ho;
    }

    public MineFragmentAdapter() {
        super(R.layout.mine_fragment_list_item);
    }

    public MineFragmentAdapter(int layoutResId, @Nullable List<RecommendGoodsResp.ListDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, @Nullable RecommendGoodsResp.ListDTO item) {
        ImageView icon = helper.getView(R.id.mine_me_list_icon);
        GlideUtils.loadImageView(icon.getContext(), UrlUtils.formatUrlPrefix(item.mainPic), icon);
        helper.setText(R.id.mine_me_list_title, item.title)
                .setText(R.id.mine_me_list_count_num, "¥" + item.originalPrice)
                .setText(R.id.mine_me_list_bot_info, "累计" + item.totalPeople + "人参与抽奖");
        TextWinUtils.updateWinStatus(
                helper.getView(R.id.mine_me_list_count_tv),
                item
        );
        helper.itemView
                .setOnClickListener(v -> {
                    ARouter.getInstance()
                            .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                            .withBoolean("needLotteryEvent", true)
                            .withInt("position", helper.getAdapterPosition())
                            .withString("goods_id", item.goodsId)
                            .navigation();
                });
    }
}
