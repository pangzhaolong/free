/**
 * <p> </p>
 * 作者： created by hegai<br>
 * 日期： 2021/10/14 15:43<br>
 * 版本：V1.0<br>
 */

package com.donews.utilslibrary.utils;

import java.util.Calendar;

// 判断是否是当天的工具类
public class DateManager {
    public final static String DRAW_DIALOG_KEY = "draw_dialog_key";

    public final static String FREE_PANIC_DIALOG_KEY = "Free_Panic_dialog_key";

    public final static String SHOW_DIALOG_WHEN_LAUNCH = "show_dialog_when_launch_dm";
    //抽奖次数
    public static String NUMBER_OF_DRAWS = "number_of_draws";
    //抽奖KEY
    public static String LOTTERY_KEY = "lottery_key";

    //抽奖次数
    public static String LOTTERY_COUNT = "lottery_count";


    private static volatile DateManager dateManager;


    //用来获取是否是今天首次弹起


    public static DateManager getInstance() {
        if (dateManager == null) {
            synchronized (DateManager.class) {
                dateManager = new DateManager();
            }
        }
        return dateManager;
    }


    /**
     * 判断是否是同一天
     *
     * @param timestampKey 时间戳Key
     */

    public boolean ifTheSameDay(String timestampKey) {
        Calendar c = Calendar.getInstance();//
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        int value = SPUtils.getInformain(timestampKey, -1);
        return mDay == value;
    }


    /**
     * 判断是否是同一天，并且更新日期
     *
     * @param key 用来判断的依据，使用者需要自己定义一个key
     */
    public boolean ifFirst(String key) {
        //是否是同一天
        boolean ifSameDay = ifTheSameDay(key);
        if (!ifSameDay) {
            Calendar c = Calendar.getInstance();//
            int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
            //更新日期
            putValue(key, mDay);
            if (key != null && key.equalsIgnoreCase(DateManager.SHOW_DIALOG_WHEN_LAUNCH)) {
                SPUtils.setInformain(KeySharePreferences.SHOW_DIALOG_WHEN_LAUNCH, true);
            }
        }
        return !ifSameDay;
    }

    /**
     * 用来判断每天的次数限制
     *
     * @param key       用来区分天的key
     * @param numberKey 用来判断每天限制的次数的key
     * @param num       限制的次数
     */
    public boolean timesLimit(String key, String numberKey, int num) {
        //是否是同一天
        boolean ifSameDay = ifTheSameDay(key);
        if (ifSameDay) {
            //判断次数是否用完
            boolean frequencyState = bigFrequency(numberKey, num);
            //次数加1
            int frequency = SPUtils.getInformain(numberKey, 0);
            frequency = frequency + 1;
            putValue(numberKey, frequency);
            return frequencyState;
        } else {
            //不是同一天次数重置并且更新数据
            //不是同一天重置次数
            putValue(numberKey, 1);
            Calendar c = Calendar.getInstance();//
            int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
            //更新数据
            putValue(LOTTERY_KEY, mDay);
            return true;
        }
    }


    //判断次数限制
    private boolean bigFrequency(String key, int num) {
        int frequency = SPUtils.getInformain(key, 0);
        if (frequency < num) {
            return true;
        } else {
            return false;
        }
    }


    private void putValue(String key, int number) {
        SPUtils.setInformain(key, number);
    }


    /**
     * @param lotteryCountKey 获取抽奖次数的key
     */
    public int getLotteryCount(String lotteryCountKey) {
        int number = SPUtils.getInformain(lotteryCountKey, 0);
        return number;
    }


    /**
     * 需要保存抽奖次数
     */
    public void putLotteryCount(String lotteryCountKey) {
        //获取次数
        int number = SPUtils.getInformain(lotteryCountKey, 0);
        number = number + 1;
        SPUtils.setInformain(lotteryCountKey, number);
    }

}
