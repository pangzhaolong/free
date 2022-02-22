package com.donews.mine.adapters;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.dn.drouter.ARouteHelper;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.glide.GlideUtils;
import com.donews.common.router.RouterActivityPath;
import com.donews.common.router.RouterFragmentPath;
import com.donews.middle.abswitch.OtherSwitch;
import com.donews.mine.R;
import com.donews.mine.bean.emus.WinTypes;
import com.donews.mine.bean.resps.WinRecordResp;
import com.donews.mine.utils.TextWinUtils;
import com.donews.mine.views.refresh.adapters.BaesLoadMoreAdapter;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;
import com.donews.utilslibrary.utils.UrlUtils;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/10/18
 * Description:
 * 参与记录 的适配器
 */
public class MineWinningRecordAdapter extends BaesLoadMoreAdapter<WinRecordResp.ListDTO, BaseViewHolder> {

    public MineWinningRecordAdapter() {
        super(R.layout.mine_winning_record_list_item);
    }

    public MineWinningRecordAdapter(int layoutResId, @Nullable List<WinRecordResp.ListDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, @Nullable WinRecordResp.ListDTO item) {
        ImageView iv = baseViewHolder.getView(R.id.mine_win_item_good_icon);
        TextView seal = baseViewHolder.getView(R.id.mine_win_item_seal);
        TextView winType = baseViewHolder.getView(R.id.mine_win_item_win_type_name);
        TextView butGo = baseViewHolder.getView(R.id.mine_win_item_goto);
        seal.setVisibility(View.VISIBLE);
        GlideUtils.loadImageView(iv.getContext(), UrlUtils.formatUrlPrefix(item.goods.image), iv);
        String lq = "未领取";
        if (item.received) {
            lq = "已领取";
        }
        baseViewHolder
                .setText(R.id.mine_win_item_title_l, item.period + "期")
                .setText(R.id.mine_win_item_title_r, item.openCode)
                .setText(R.id.mine_win_item_win_type_status, lq)
                .setText(R.id.mine_win_item_snap_number,
                        Html.fromHtml(TextWinUtils.drawOldText(item.openCode, item.code)))
                .setText(R.id.mine_win_item_good_name, item.goods.title)
                .setText(R.id.mine_win_item_goto, "再抽一次")
                .setText(R.id.mine_win_item_good_pic, "" + item.goods.price);
        seal.setText(WinTypes.Alike.name);
        if (WinTypes.Alike.type.equals(item.winType)) {
            winType.setText("相似奖");
            seal.setText(WinTypes.Alike.name);
        } else if (WinTypes.Equal.type.equals(item.winType)) {
            winType.setText("免单奖");
            seal.setText(WinTypes.Equal.name);
        } else {
            winType.setText("无");
            seal.setText(WinTypes.None.name);
        }
        butGo.setOnClickListener((v) -> {
            AnalysisUtils.onEventEx(baseViewHolder.itemView.getContext(), Dot.Page_ContactService);
            Bundle bundle = new Bundle();
            bundle.putString("url",
                    "https://recharge-web.xg.tagtic.cn/jdd/index.html#/customer");
            bundle.putString("title", "客服");
            ARouteHelper.routeSkip(RouterActivityPath.Web.PAGER_WEB_ACTIVITY, bundle);
        });
        baseViewHolder.itemView
                .setOnClickListener((v) -> {
                    AnalysisUtils.onEventEx(BaseApplication.getInstance(),
                            Dot.But_Goto_Lottery, "中奖记录>再抽一次");
                    ARouter.getInstance()
                            .build(RouterFragmentPath.Lottery.PAGER_LOTTERY)
                            .withString("goods_id", item.goods.id)
                            .withBoolean("start_lottery", OtherSwitch.Ins().isOpenAutoLottery())
                            .navigation();
                });
    }

}
