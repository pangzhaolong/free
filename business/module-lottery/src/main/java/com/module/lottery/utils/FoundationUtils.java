package com.module.lottery.utils;

import com.donews.middle.abswitch.OtherSwitch;

import java.util.List;

public class FoundationUtils {
    //判断是否是自选码
    public static boolean getOptionalCodeType(int local) {
        List<Integer> localList = OtherSwitch.Ins().getOpenOptionalLocationList();
        for (int i = 0; i < localList.size(); i++) {
            if (local == localList.get(i)) {
                return true;
            }
        }
        return false;
    }


    //判断是否剩余自选码机会
    public static boolean getLotteryChance(int local) {
        List<Integer> localList = OtherSwitch.Ins().getOpenOptionalLocationList();
        for (int i = 0; i < localList.size(); i++) {
            if (localList.get(i) >= local) {
                //还有自选码
                return true;
            }
        }
        //没有自选码了
        return false;
    }


    //获取下一个自选码的位置
    public static int getOptionalCodeLocation(int local) {
        List<Integer> localList = OtherSwitch.Ins().getOpenOptionalLocationList();
        int numberLocal = 0;
        for (int i = 0; i < localList.size(); i++) {
            if (localList.get(i) > local) {
                //还有自选码
                if (numberLocal > localList.get(i) || numberLocal == 0) {
                    numberLocal = localList.get(i);
                }
            }
        }
        //没有自选码了
        return (numberLocal - local);
    }
}
