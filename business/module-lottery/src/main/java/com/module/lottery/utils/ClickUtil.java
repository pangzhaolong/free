package com.module.lottery.utils;

public class ClickUtil {



    /**
     * 两次点击按钮之间的点击间隔不能少于1000毫秒
     */
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    /**
     * 最后一次点击的时间
     */
    private static long mLastClickTime = -1;
    /**
     * 是否为快速点击
     *
     * @return true:快速点击  false:非快速点击
     */
    public static boolean isFastClick() {
        boolean flag;
        long curClickTime = System.currentTimeMillis();
        if (curClickTime - mLastClickTime > MIN_CLICK_DELAY_TIME) {
            flag = false;
        } else {
            flag = true;
        }
        mLastClickTime = curClickTime;
        return flag;
    }

}
