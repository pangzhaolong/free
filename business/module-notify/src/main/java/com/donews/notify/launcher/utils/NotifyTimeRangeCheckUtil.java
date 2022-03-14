package com.donews.notify.launcher.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lcl
 * Date on 2022/2/11
 * Description:
 * 时间区间检查工具
 */
public class NotifyTimeRangeCheckUtil {

    //时间的小数当前
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH");

    /**
     * 检查时间区间，检查当前时间是否再指定的时间区间内
     *
     * @param intervals 指定的时间区间
     * @return T:再区间内，F:不在区间内
     */
    public static boolean canTimeInterval(List<String> intervals) {
        long curTimeMillis = System.currentTimeMillis();
        String curH = SIMPLE_DATE_FORMAT.format(new Date(curTimeMillis));
        Map<Integer, Integer> targetRanges = getRangeMap(intervals);
        try {
            int curTimeH = Integer.parseInt(curH);
            Iterator<Integer> ite = targetRanges.keySet().iterator();
            while (ite.hasNext()) {
                Integer key = ite.next();
                Integer value = targetRanges.get(key);
                if(key <= value){
                    //正常的顺序。表示一天内的
                    if (curTimeH >= key && curTimeH <= value) {
                        return true;//只要有一个区间满足。就满足条件。直接返回
                    }
                }else{
                    //非正常时序。已经出现跨天时间段
                    if (curTimeH >= key || curTimeH <= value) {
                        return true;//只要有一个区间满足。就满足条件。直接返回
                    }
                }
            }
            return false;//没有满足的条件
        } catch (Exception e) {
            NotifyLog.log("处理时间区间配置出现了错误：e=" + e);
            return false;
        }
    }

    /**
     * 将原始数据转为可被识别的区间范围数据
     *
     * @param intervals 原始数据
     * @return 转换之后的区间数据, key:最小值，value:最大值
     */
    public static Map<Integer, Integer> getRangeMap(List<String> intervals) {
        Map<Integer, Integer> targetRanges = new HashMap<>();
        for (String interval : intervals) {
            try {
                String[] spl = interval.split("-");
                if (spl.length != 2) {
                    continue;//数据错误。忽略掉
                }
                int key = Integer.parseInt(spl[0].trim());
                int value = Integer.parseInt(spl[1].trim());
                if (value > 23) {
                    value = 23;
                }
                if (key > 23) {
                    key = 23;
                }
                targetRanges.put(key, value);
            } catch (Exception e) {
                NotifyLog.log("处理通知配置数据出错了：e=" + e);
                e.printStackTrace();
            }
        }
        return targetRanges;
    }
}
