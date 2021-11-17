/**
 * <p> </p>
 * 作者： created by hegai<br>
 * 日期： 2021/10/14 15:43<br>
 * 版本：V1.0<br>
 */

package com.donews.main.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;

//广告播放管理类 日期
public class DateManager {
    private static SharedPreferences frequencyData;
    private static String DRAW_DATA = "draw_data";
    private static String DRAW_DAY = "draw_day";


    private static volatile DateManager dateManager;

    public static DateManager getInstance(Context mContext) {
        if (frequencyData == null) {
            synchronized (DateManager.class) {
                if (frequencyData == null) {
                    dateManager = new DateManager(mContext);
                }
            }
        }
        return dateManager;
    }


    private DateManager(Context mContext) {
        if (mContext != null) {
            frequencyData = mContext.getApplicationContext().getSharedPreferences(DRAW_DATA, 0);
        }
    }

    //判断是否是同一天
    public  boolean ifTheSameDay() {
        Calendar c = Calendar.getInstance();//
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        int value = frequencyData.getInt(DRAW_DAY, -1);
        if (mDay == value) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断是否是同一天，并且更新日期
     */
    public  boolean ifFirst() {
        //是否是同一天
        boolean ifSameDay = ifTheSameDay();
        if (!ifSameDay) {
            Calendar c = Calendar.getInstance();//
            int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
            //更新日期
            putValue(DRAW_DAY, mDay);
        }
        return !ifSameDay;
    }


    private  void putValue(String key, int number) {
        if (frequencyData != null) {
            frequencyData.edit().putInt(key, number).commit();
        }
    }

}
