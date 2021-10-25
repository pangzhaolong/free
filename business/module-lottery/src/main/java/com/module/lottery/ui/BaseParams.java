package com.module.lottery.ui;

import android.util.ArrayMap;

import java.util.Map;

public class BaseParams {


    public static Map<String, String> getBaseParams() {
        Map<String , String>  baseParams=new ArrayMap<>();
        baseParams.put("page_size",10+"");
        return baseParams;
    }


    public static Map<String, String> getMap() {
        Map<String , String>  params=new ArrayMap<>();
        return params;
    }



}
