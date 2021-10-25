package com.dn.sdk.sdk.bean;

/**
 * SDK 类型
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/25 13:54
 */
public enum SDKType {

    NO_AD(0, "无广告"),

    /**
     * 多牛聚合
     */
    DO_NEWS(1, "doNews"),

    /**
     * 多牛封装的穿山甲GroMore平台
     */
    DO_GRO_MORE(2, "doGroMore");


    public int value;
    public String description;

    SDKType(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
