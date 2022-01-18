package com.donews.mine.bean;

import android.view.View;

import com.donews.common.contract.BaseCustomViewModel;
import com.donews.middle.go.GotoUtil;
import com.donews.mine.views.operating.MineOperatingPosView;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;

import java.util.List;

/**
 * @author lcl
 * Date on 2021/12/29
 * Description:
 * 个人中心模块的运营位配置
 */
public class MineWithdraWallBean extends BaseCustomViewModel {
    //是否开启个人中心的运营位
    public boolean mine;
    public List<MineWallBeanItem> mineItems;
    //提现页面运营位是否启用
    public boolean withDrawal;
    public List<DrawalWallBeanItem> withDrawalItems;

    /**
     * 个人中心的运营位数据实体
     */
    public static class MineWallBeanItem extends BaseCustomViewModel
            implements MineOperatingPosView.IOperatingData {
        public String id;
        public String title;
        public String icon;
        public String action;

        @Override
        public String getIconUrl() {
            return icon;
        }

        @Override
        public void onClick(View view, MineOperatingPosView.IOperatingData data) {
            GotoUtil.doAction(view.getContext(), action, title, "withdraw");
            AnalysisUtils.onEventEx(view.getContext(), Dot.MINE_YYW_CLICK);
        }
    }

    /**
     * 提现页面的运营位
     */
    public static class DrawalWallBeanItem extends BaseCustomViewModel {
        public String id;
        public String title;
        public String img;
        public String action;
    }
}

