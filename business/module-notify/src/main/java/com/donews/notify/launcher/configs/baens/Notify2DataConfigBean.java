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
         * 通知分类配置的 id
         */
        public int id;
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
         * 当前类型的通知是否启用(true:启用，F:关闭)
         */
        public boolean isOpen = true;
        /**
         * 当前类型通知最大次数
         */
        public int notifyMaxCount;
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
        public int id;
        /**
         * 通用字段：当前通知模板是否启用(true:启用，F:关闭)
         */
        public boolean isOpen = false;
        /**
         * 通用字段：当前通知的弹出方式(<0:未配置,使用客户端默认配置,0:顶部，1:底部,>=2:中间)
         */
        public int orientation = -1;
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
         * [UI模板2 特有]
         * 非通用：左侧的按钮文字(建议通过getXxx系列方法获取)
         */
        private String buttonLeft = null;

        /**
         * [UI模板2 特有]
         * 非通用：右侧的按钮文字(建议通过getXxx系列方法获取)
         */
        private String buttonRight = null;

        /**
         * [UI模板5 特有]
         * 非通用：UI类型 (0：金额模式，1：图标模式)
         */
        private int type = 0;

        /**
         * [UI模板5 特有]
         * 非通用：金额顶部的描述文字
         */
        private String titleNumnerDesc;

        /**
         * [UI模板5 特有]
         * 非通用：标题的前缀文案
         */
        private String titlePrefix;

        /**
         * [UI模板4 特有]
         * 非通用字段：(服务对象)描述
         */
        private String namePrefix;

        /**
         * [UI模板4、5 特有]
         * 非通用：描述的前缀文案
         */
        private String descPrefix;

        /**
         * [UI模板4 特有]
         * 非通用：(生效时间)前缀文案
         */
        private String timePrefix;

        /**
         * [UI模板5 特有]
         * 非通用：描述的前缀文案
         */
        private List<UiTemplatImageItem> goodImages;


        //本地字段。非中台配置字段
        /**
         * 本地字段：当前UI模板归宿的分类id，上级所属分类的id
         */
        public int notifyTypeId;

        /**
         * 构建和处理标签标签
         */
        public void buildFixTag() {
            this.title = FixTagUtils.buildContentTag(this, title);
            this.name = FixTagUtils.buildContentTag(this, name);
            this.desc = FixTagUtils.buildContentTag(this, desc);
            this.action = FixTagUtils.buildContentTag(this, action);
            this.iconLeft = FixTagUtils.buildContentTag(this, iconLeft);
            this.iconLeftTopMin = FixTagUtils.buildContentTag(this, iconLeftTopMin);
            this.iconRight = FixTagUtils.buildContentTag(this, iconRight);
            this.buttonLeft = FixTagUtils.buildContentTag(this, buttonLeft);
            this.buttonRight = FixTagUtils.buildContentTag(this, buttonRight);
            this.titleNumnerDesc = FixTagUtils.buildContentTag(this, titleNumnerDesc);
            this.titlePrefix = FixTagUtils.buildContentTag(this, titlePrefix);
            this.namePrefix = FixTagUtils.buildContentTag(this, namePrefix);
            this.descPrefix = FixTagUtils.buildContentTag(this, descPrefix);
            this.timePrefix = FixTagUtils.buildContentTag(this, timePrefix);
            //反向处理一次。因为有可能存在反向引用
            this.timePrefix = FixTagUtils.buildContentTag(this, timePrefix);
            this.descPrefix = FixTagUtils.buildContentTag(this, descPrefix);
            this.namePrefix = FixTagUtils.buildContentTag(this, namePrefix);
            this.titlePrefix = FixTagUtils.buildContentTag(this, titlePrefix);
            this.titleNumnerDesc = FixTagUtils.buildContentTag(this, titleNumnerDesc);
            this.buttonRight = FixTagUtils.buildContentTag(this, buttonRight);
            this.buttonLeft = FixTagUtils.buildContentTag(this, buttonLeft);
            this.iconRight = FixTagUtils.buildContentTag(this, iconRight);
            this.iconLeftTopMin = FixTagUtils.buildContentTag(this, iconLeftTopMin);
            this.iconLeft = FixTagUtils.buildContentTag(this, iconLeft);
            this.action = FixTagUtils.buildContentTag(this, action);
            this.desc = FixTagUtils.buildContentTag(this, desc);
            this.name = FixTagUtils.buildContentTag(this, name);
            this.title = FixTagUtils.buildContentTag(this, title);
        }

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

        /**
         * 获取UI模板5的类型
         *
         * @return (0 ： 金额模式 ， 1 ： 图标模式)
         */
        public int getType() {
            return type;
        }

        /**
         * 获取UI模板5的标题前缀
         *
         * @return
         */
        public String getTitlePrefix() {
            return titlePrefix;
        }

        /**
         * 获取标题金额的描述文字
         * @return
         */
        public String getTitleNumnerDesc() {
            return titleNumnerDesc;
        }

        /**
         * 获取UI模板5的描述前缀
         * @return
         */
        public String getDescPrefix() {
            return descPrefix;
        }

        /**
         * 获取ui模板5的商品图标集合
         * @return
         */
        public List<UiTemplatImageItem> getGoodImages() {
            return goodImages;
        }

        public String getNamePrefix() {
            return namePrefix;
        }

        public String getTimePrefix() {
            return timePrefix;
        }

        public void setNamePrefix(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        public void setTimePrefix(String timePrefix) {
            this.timePrefix = timePrefix;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public void setIconLeft(String iconLeft) {
            this.iconLeft = iconLeft;
        }

        public void setIconLeftTopMin(String iconLeftTopMin) {
            this.iconLeftTopMin = iconLeftTopMin;
        }

        public void setIconRight(String iconRight) {
            this.iconRight = iconRight;
        }

        public void setButtonLeft(String buttonLeft) {
            this.buttonLeft = buttonLeft;
        }

        public void setButtonRight(String buttonRight) {
            this.buttonRight = buttonRight;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setTitlePrefix(String titlePrefix) {
            this.titlePrefix = titlePrefix;
        }

        public void setTitleNumnerDesc(String titleNumnerDesc) {
            this.titleNumnerDesc = titleNumnerDesc;
        }

        public void setDescPrefix(String descPrefix) {
            this.descPrefix = descPrefix;
        }

        public void setGoodImages(List<UiTemplatImageItem> goodImages) {
            this.goodImages = goodImages;
        }
    }

    /**
     * ui模板5中的图片集合实体
     */
    public static class UiTemplatImageItem{
        /**
         * 商品的图标地址
         */
        public String goodIcon;
    }
}
