package com.donews.notify.launcher.configs.baens;

import com.donews.notify.launcher.utils.fix.FixTagUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lcl
 * Date on 2022/1/5
 * Description:
 */
public class Notify2DataConfigBean {
    /**
     * 自动刷新间隔时间；单位：秒
     */
    public int refreshInterval;
    /**
     * 红包的最小金额
     */
    public float redPackageMinAmount;
    /**
     * 红包的最小金额
     */
    public float redPackageMaxAmount;
    /**
     * 红包的翻倍比例，可理解为上浮比例
     */
    public float redPackageDoubleProportion;
    /**
     * 中台对推送的相关配置
     */
    public List<NotifyItemConfig> notifyConfigs = new ArrayList<>();


    /**
     * 每项条件下的通知配置
     */
    public static class NotifyItemConfig {
        /**
         * 条件1：注册超过的时间
         */
        public int registerMore;
        /**
         * 条件2：当日领取红包的数量
         */
        public int dayReceiveRedCount;
        /**
         * 条件3：当日获取抽奖码的数量
         */
        public int dayLotteryCodeCount;
        /**
         * 当前条件下的UI模板集合
         */
        public List<UiTemplat> uiTemplate = new ArrayList<>();
    }

    /**
     * UI模板的相关配置参数
     */
    public static class UiTemplat {
        /**
         * 通用字段：ui模板的编号
         */
        public String id;
        /**
         * 通用字段：标题(建议通过getXxx系列方法获取)
         */
        private String title;
        /**
         * 通用字段：名称(建议通过getXxx系列方法获取)
         */
        private String name;
        /**
         * 通用字段：描述(建议通过getXxx系列方法获取)
         */
        private String desc;
        /**
         * 通用字段：动作(建议通过getXxx系列方法获取),(前端处理跳转的动作)
         */
        private String action;

        /**
         * 非通用：左侧的图标(建议通过getXxx系列方法获取)
         */
        private String iconLeft = null;

        /**
         * 非通用：左侧顶部的小图标(建议通过getXxx系列方法获取)
         */
        private String iconLeftTopMin = null;

        /**
         * 非通用：右侧的图标(建议通过getXxx系列方法获取)
         */
        private String iconRight = null;

        /**
         * 非通用：左侧的按钮文字(建议通过getXxx系列方法获取)
         */
        private String buttonLeft = null;
        /**
         * 非通用：右侧的按钮文字(建议通过getXxx系列方法获取)
         */
        private String buttonRight = null;

        /**
         * 获取标题。会处理掉标签内容
         *
         * @return
         */
        public String getTitle() {
            return title;
        }

        /**
         * 获取名称
         *
         * @return
         */
        public String getName() {
            return name;
        }

        /**
         * 获取描述
         *
         * @return
         */
        public String getDesc() {
            return desc;
        }

        /**
         * 获取动作
         *
         * @return
         */
        public String getAction() {
            return action;
        }

        /**
         * 获取左侧图标
         *
         * @return
         */
        public String getIconLeft() {
            return iconLeft;
        }

        /**
         * 获取左侧的顶部小图标
         *
         * @return
         */
        public String getIconLeftTopMin() {
            return iconLeftTopMin;
        }

        /**
         * 获取右侧图标
         *
         * @return
         */
        public String getIconRight() {
            return iconRight;
        }

        /**
         * 获取左侧按钮文字
         *
         * @return
         */
        public String getButtonLeft() {
            return buttonLeft;
        }

        /**
         * 获取右侧按钮文字
         *
         * @return
         */
        public String getButtonRight() {
            return buttonRight;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = FixTagUtils.buildContentTag(this, title);
        }

        public void setName(String name) {
            this.name = FixTagUtils.buildContentTag(this, name);
        }

        public void setDesc(String desc) {
            this.desc = FixTagUtils.buildContentTag(this, desc);
        }

        public void setAction(String action) {
            this.action = FixTagUtils.buildContentTag(this, action);
        }

        public void setIconLeft(String iconLeft) {
            this.iconLeft = FixTagUtils.buildContentTag(this, iconLeft);
        }

        public void setIconLeftTopMin(String iconLeftTopMin) {
            this.iconLeftTopMin = FixTagUtils.buildContentTag(this, iconLeftTopMin);
        }

        public void setIconRight(String iconRight) {
            this.iconRight = FixTagUtils.buildContentTag(this, iconRight);
        }

        public void setButtonLeft(String buttonLeft) {
            this.buttonLeft = FixTagUtils.buildContentTag(this, buttonLeft);
        }

        public void setButtonRight(String buttonRight) {
            this.buttonRight = FixTagUtils.buildContentTag(this, buttonRight);
        }

    }
}
