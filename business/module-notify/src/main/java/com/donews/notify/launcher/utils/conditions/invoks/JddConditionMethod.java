package com.donews.notify.launcher.utils.conditions.invoks;

import static com.donews.utilslibrary.utils.KeySharePreferences.TIME_SERVICE;

import com.donews.base.BuildConfig;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.GsonUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;
import com.tencent.mmkv.MMKV;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author lcl
 * Date on 2022/1/20
 * Description:
 * 奖多多处理业务条件相关工具方法,因为开发基于奖多多。所以默认支持了奖多多的锚定池方法
 */
public class JddConditionMethod {

    /**
     * 今天抽奖次数
     */
    private static String TODAY_LOTTERY_TIMES = "todayLotteryTimes";
    //------------------------本地次数限制相关的key------------------------------
    //本地配置保存的配置文件
    private static String allowDayCountFile = "notify_allowDayCount_file";
    //获取天数，当前生效的是哪一天的配置(因为隔天需要清除)
    private static String allowDayKey = "notify_allowDayKey";

    private static MMKV mmkv;

    /**
     * 获取当前开启红包的个数
     *
     * @return 当前已开红包个数
     */
    public int getDayReceiveRedCount() {
        return SPUtils.getInformain(KeySharePreferences.OPENED_RED_PACKAGE_COUNTS, 0);
    }

    /**
     * 当天获取抽奖码的个数
     *
     * @return 当前已开红包个数
     */
    public int getDayLotteryCodeCount() {
        initAdCountMMkv();
        //当前的时间
        long currentTime = com.donews.utilslibrary.utils.SPUtils.getLongInformain(TIME_SERVICE, 0L) * 1000;
        String localAllowDay = com.blankj.utilcode.util.SPUtils.getInstance(allowDayCountFile).getString(allowDayKey);
        //检查是否超过了一天。超过需要清除本地的存储
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String currentTimeDay = sf.format(new Date(currentTime));
        if (localAllowDay != null && localAllowDay.length() > 0) {
            try {
                if (!currentTimeDay.equals(sf.format(sf.parse(localAllowDay)))) {
                    updateTodayLotteryCount();
                }
            } catch (ParseException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                    ToastUtil.showShort(BaseApplication.getInstance(), "本地获取处抽奖码出错了:" + e);
                }
            }
        }
        return mmkv.decodeInt(TODAY_LOTTERY_TIMES, 0);
    }

    /**
     * 超过一天了。更新抽奖数据
     *
     * @return Int
     */
    private static void updateTodayLotteryCount() {
        mmkv.encode(TODAY_LOTTERY_TIMES, 0);
    }

    //初始化数据存储
    private static void initAdCountMMkv() {
        if (mmkv != null) {
            return;
        }
        mmkv = MMKV.mmkvWithID("AdCount", MMKV.MULTI_PROCESS_MODE);
    }
}
