package com.dn.sdk.lib;

/**
 * @author by SnowDragon
 * Date on 2021/1/15
 * Description:
 */

public enum SDKType {

    /**
     * 多牛聚合
     */
    DO_NEWS(1, "doNews"),

    /**
     * 流量宝
     */
    YOU_LIANG_BAO(2, "youliangbao"),

    /**
     * adcdn
     */
    ADCDN(3, "adcdn");

    public int VALUE;
    public String DESCRIPTION;

    SDKType(int i, String s) {
        VALUE = i;
        DESCRIPTION = s;
    }
}
