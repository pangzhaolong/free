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
    public static String DRAW_DIALOG_KEY = "draw_dialog_key";

    public static String FREE_PANIC_DIALOG_KEY = "Free_Panic_dialog_key";

    private static SharedPreferences frequencyData;
    private static String DRAW_DATA = "MAIN_SP";
    private static volatile DateManager dateManager;


    //用来获取是否是今天首次弹起



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
    public  boolean ifTheSameDay(String firstDay) {
        Calendar c = Calendar.getInstance();//
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        int value = frequencyData.getInt(firstDay, -1);
        if (mDay == value) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 判断是否是同一天，并且更新日期
     */
    public  boolean ifFirst(String firstDay) {
        //是否是同一天
        boolean ifSameDay = ifTheSameDay(firstDay);
        if (!ifSameDay) {
            Calendar c = Calendar.getInstance();//
            int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
            //更新日期
            putValue(firstDay, mDay);
        }
        return !ifSameDay;
    }


    private  void putValue(String key, int number) {
        if (frequencyData != null) {
            frequencyData.edit().putInt(key, number).commit();
        }
    }

}
