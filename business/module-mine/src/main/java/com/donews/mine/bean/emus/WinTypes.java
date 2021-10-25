package com.donews.mine.bean.emus;

/**
 * @author lcl
 * Date on 2021/10/21
 * Description:
 * 中奖类型
 */
public enum WinTypes {
    None("None","无"),
    Alike("Alike","相似\n中奖"),
    Equal("Equal","免单\n中奖"),
    ;

    public String type;
    public String name;

    WinTypes(String type,String name){
        this.type = type;
        this.name = name;
    }
}
