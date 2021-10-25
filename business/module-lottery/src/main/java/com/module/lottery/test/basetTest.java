package com.module.lottery.test;

import com.module.lottery.model.LotteryModel;

public abstract class basetTest <s extends LotteryModel>{
    public abstract int initView();



    public  void tset(){
        System.out.print(initView());
    }

}
