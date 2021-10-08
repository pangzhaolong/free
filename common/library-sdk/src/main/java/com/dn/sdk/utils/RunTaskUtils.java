package com.dn.sdk.utils;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/12/4
 * Description:
 */
public class RunTaskUtils {
    public static boolean isAppOnForeground(Context context) {

        String mPackageName = context.getPackageName();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
        if (tasksInfo.size() > 0) {
            // 应用程序位于堆栈的顶层
            if (mPackageName.equals(tasksInfo.get(0).topActivity
                    .getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public boolean isAppForeground(Context context) {
        long ts = System.currentTimeMillis();
        String mPackageName = context.getPackageName();
        UsageStatsManager mUsageStatsManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            List<UsageStats> usageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, ts - 1000, ts);
            //如果为空则返回""
            if (usageStats == null || usageStats.size() == 0) {
                return false;
            }
            //mRecentComp = new RecentUseComparator()
            Collections.sort(usageStats, new RecentUseComparator());

            return mPackageName.equals(usageStats.get(0).getPackageName());
        }
        return false;

    }

    static class RecentUseComparator implements Comparator<UsageStats> {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public int compare(UsageStats lhs, UsageStats rhs) {
            return (lhs.getLastTimeUsed() > rhs.getLastTimeUsed()) ? -1 : (lhs.getLastTimeUsed() == rhs.getLastTimeUsed()) ? 0 : 1;
        }
    }



}
