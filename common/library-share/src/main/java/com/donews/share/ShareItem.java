package com.donews.share;



/**
 * @Author: honeylife
 * @CreateDate: 2020/4/15 17:48
 * @Description:
 */
public class ShareItem {
    public static final int TYPE_H5 = 1;// h5链接
    public static final int TYPE_IMAGE = 2;// 图片
    public static final int TYPE_TEXT = 3;// 纯文本分享

    /**
     * title : 标题
     * content : 内容解释
     * icon : 小头像
     * imageUrl : 如果是大图的时候的，图片地址
     * type : 类型，1 h5链接，2图片  3 纯文本，内容写在content中
     * shareFromType:  h5 需要客服端拼参数
     * webUrl :  h5链接地址
     * extraKey: 额外的参数key  webUrl后面拼参数用
     * extraValue ：额外参数的值
     * cmd :表示微信，朋友圈，
     */

    private String title;
    private String content;
    private String icon;
    private String imageUrl;
    private int type;
    private String webUrl;
    private int shareFromType;
    private int actionId;
    private String extraKey;
    private String extraValue;
    private int cmd;

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getActionId() {
        return actionId;
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public String getShareWebUrl() {
        return webUrl;
    }

    public String getShareWebUrl(String name, String inviCode, String isDarly) {
        return webUrl;
    }

    /**
     * @param isDarly 返给前端，是否为第三方页面， 传值为3
     * @return
     */
    public String getShareWebUrl(String isDarly) {
        return webUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String getExtraKey() {
        return extraKey;
    }

    public void setExtraKey(String extraKey) {
        this.extraKey = extraKey;
    }

    public String getExtraValue() {
        return extraValue;
    }

    public void setExtraValue(String extraValue) {
        this.extraValue = extraValue;
    }

    @Override
    public String toString() {
        return "ShareItem{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", icon='" + icon + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", type=" + type +
                ", webUrl='" + webUrl + '\'' +
                ", shareFromType=" + shareFromType +
                ", actionId=" + actionId +
                ", extraKey='" + extraKey + '\'' +
                ", extraValue='" + extraValue + '\'' +
                '}';
    }

    public int getShareFromType() {
        return shareFromType;
    }

    public void setShareFromType(int shareFromType) {
        this.shareFromType = shareFromType;
    }


}
