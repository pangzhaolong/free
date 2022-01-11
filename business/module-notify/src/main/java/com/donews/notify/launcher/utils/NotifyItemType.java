package com.donews.notify.launcher.utils;

import com.donews.notify.R;

/**
 * @author lcl
 * Date on 2022/1/5
 * Description:
 * 新增的通知类型。支持的通知类型,和中台的UI模板id列表对应
 */
public enum NotifyItemType {
    /**
     * UI模板0(原始的类型模板)
     */
    TYPE_TOP_1(
            0,
            "TYPE_TOP_1",
            R.layout.notify_item_type1,
            0
    ),
    /**
     * UI模板1(抽奖引导1：桌面顶部)
     */
    TYPE_LOTT_TOP_1(
            1,
            "TYPE_LOTT_TOP_1",
            R.layout.notify_item_type_lott_1,
            0
    ),
    /**
     * UI模板2(抽奖引导2：顶部)
     */
    TYPE_LOTT_TOP_2(
            2,
            "TYPE_LOTT_TOP_2",
            R.layout.notify_item_type_lott_2,
            0
    ),
    /**
     * UI模板3(红包引导1：顶部 — 头像、红包引导1：顶部 — 昵称)
     */
    TYPE_LOTT_TOP_3(
            3,
            "TYPE_LOTT_TOP_3",
            R.layout.notify_item_type_lott_3,
            0
    ),
    /**
     * UI模板3(红包引导1：顶部 — 头像、红包引导1：顶部 — 昵称)
     */
    TYPE_LOTT_TOP_4(
            4,
            "TYPE_LOTT_TOP_4",
            R.layout.notify_item_type_lott_4,
            0
    ),
    /**
     * UI模板5(红包引导3：底部 — 金额、红包引导4：底部 — 配图)
     */
    TYPE_LOTT_BOTTOM_5(
            5,
            "TYPE_LOTT_BOTTOM_5",
            R.layout.notify_item_type_lott_5,
            1
    ),
    ;

    //类型的id(唯一id,和中台服务器配置的UI模板的id对应)
    public final int typeId;
    //类型的唯一标记(唯一文字标记)
    public final String typeKey;
    //视图模板的视图id
    public final int typeLayoutId;
    //视图的方向(0：顶部,1：底部)
    public final int orientation;

    /**
     * 类型的参数构建
     *
     * @param typeId       类型的id(可作为后台配置的标记)
     * @param typeKey      类型标记(可作为后台配置的标记)
     * @param typeLayoutId 当前类型的视图资源id
     * @param orientation  通知所在的屏幕方向 (0：顶部,1：底部)
     */
    NotifyItemType(int typeId, String typeKey, int typeLayoutId, int orientation) {
        this.typeId = typeId;
        this.typeKey = typeKey;
        this.typeLayoutId = typeLayoutId;
        this.orientation = orientation;
    }
}
