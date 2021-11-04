/**
 * <p> </p>
 * 作者： created by hegai<br>
 * 日期： 2021/10/14 15:43<br>
 * 版本：V1.0<br>
 */

package com.module.lottery.utils;
import android.content.Context;
import android.content.SharedPreferences;
import java.util.Calendar;
//广告播放管理类 日期
public class DateManager {
    private static SharedPreferences frequencyData;
    private static String FREQUENCY_DATA = "frequency_data";
    private static String FREQUENCY_KEY = "frequency_key";
    private static String CALENDAR_DAY = "calendar_day";
    public static String HIN_VALUE = "今天次数已用完，明日再来";
    public DateManager(Context mContext) {
        if (mContext != null) {
            frequencyData = mContext.getApplicationContext().getSharedPreferences(FREQUENCY_DATA, 0);
        }
    }

    //判断是否是同一天
    public boolean ifTheSameDay() {
        Calendar c = Calendar.getInstance();//
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        int value = frequencyData.getInt(CALENDAR_DAY, -1);
        if (mDay == value) {
            return true;
        } else {
            return false;
        }
    }

    public boolean ifPlayAd(int num) {
        //是否是同一天
        boolean ifSameDay = ifTheSameDay();
        if (ifSameDay) {
            //判断次数是否用完
            boolean frequencyState = bigFrequency(num);
            //次数加1
            int frequency = frequencyData.getInt(FREQUENCY_KEY, 0);
            frequency = frequency + 1;
            putValue(FREQUENCY_KEY, frequency);
            return frequencyState;
        } else {
            //不是同一天次数重置并且更新数据
            //不是同一天重置次数
            putValue(FREQUENCY_KEY, 1);
            Calendar c = Calendar.getInstance();//
            int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
            //更新数据
            putValue(CALENDAR_DAY, mDay);
            return true;
        }
    }


    private void putValue(String key, int number) {
        if (frequencyData != null) {
            frequencyData.edit().putInt(key, number).commit();
        }
    }


    //判断次数限制
    public boolean bigFrequency(int num) {
        int frequency = frequencyData.getInt(FREQUENCY_KEY, 0);
        if (frequency < num) {
            return true;
        } else {
            return false;
        }
    }
}
