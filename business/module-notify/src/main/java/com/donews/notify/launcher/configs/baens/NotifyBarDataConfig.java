package com.donews.notify.launcher.configs.baens;

import java.util.List;

/**
 * @author lcl
 * Date on 2022/3/8
 * Description:
 * 通知栏数据的相关配置操作
 */
public class NotifyBarDataConfig {
    /**
     * 配置自动刷新时间
     */
    public int refreshInterval = 30;
    /**
     * 所有通知最多展示的次数(当前生效。跨天重置)
     */
    public int notifyMaxShowCount = 30;
    /**
     * 通知显示的最小间隔时间，两次通知的最小间隔时间限制
     */
    public int notifyShowLastOpenInterval = 0;
    /**
     * 通知的显示模式
     * 0:无限叠加模式
     * 1:相同样式内容替换模式
     */
    public int notifyShowModel = 1;
    /**
     * 【沿用桌面通知】
     * <p>
     * {@link Notify2DataConfigBean.NotifyItemConfig#judgeConditions 字段所对应的条件限制的参考锚定值获取池}
     * 锚定方法要求：返回值(int),参数：无，如下方法签名：
     * int xxx()
     * <p>
     * 锚定池数据结构和桌面通知使用相同的逻辑
     */
    public List<Notify2DataConfigBean.ConditionalProcessItem> conditionsPools;
    /**
     * 通知的相关数据结构。包括条件时间等
     */
    public List<NotifyBarItemConfig> notifyConfig;


    /**
     * 通知栏通知每项条件的相关配置
     */
    public static class NotifyBarItemConfig {
        /**
         * 类型id(当前分类的唯一id)
         */
        public int typeId;
        /**
         * 时间、时间段配置
         * 支持格式：
         * 8:00,8:05,9:10-9:20
         */
        public String time;
        /**
         * 是否开启这个时间段的通知(控制此时间段通知配置是否生效)
         */
        public boolean isOpen;

        /**
         * 当前分类通知的允许显示的最大次数
         */
        public int notifyMaxShowCount;

        /**
         * 【沿用桌面通知】
         * 条件限制集合(限制的条件)
         */
        public List<Notify2DataConfigBean.JudgeConditionItem> judgeConditions;
        /**
         * 【沿用桌面通知】
         * {@link #judgeConditions} 字段的条件锚定值获取配置。配置值必须来源于锚定池:{@link Notify2DataConfigBean#conditionsPools}
         * 多个锚定使用","分隔。<br/>
         * 例如：0,1,3 表示:使用锚定池中第:0,1,3 分别提供个条件对应位置使用
         */
        public String anchorCollection;
        /**
         * 通知UI的样式模板
         */
        public List<NotifyBarUIDataConfig> notifyUISytles;
    }

    /**
     * 通知栏UI相关数据配置
     */
    public static class NotifyBarUIDataConfig {
        /**
         * UI通知的样式类型
         * 0：只有标题和描述
         * 1：右侧有图标的
         */
        public int uiType = 0;
        /**
         * 标题内容
         */
        public String title;
        /**
         * 标题下的描述内容
         */
        public String desc;
        /**
         * 右侧的图标地址
         * 如果为空表示没有右侧图标，为空表示没有
         */
        public String rightIcon;
        /**
         * 跳转控制。参考桌面通知控制和配置逻辑
         */
        public String action;
    }
}
