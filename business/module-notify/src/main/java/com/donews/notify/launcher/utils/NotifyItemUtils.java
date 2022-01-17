package com.donews.notify.launcher.utils;

import static com.donews.utilslibrary.utils.KeySharePreferences.TIME_SERVICE;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentActivity;

import com.donews.base.utils.GsonUtils;
import com.donews.base.utils.ToastUtil;
import com.donews.common.BuildConfig;
import com.donews.notify.launcher.NotifyAnimationView;
import com.donews.notify.launcher.configs.Notify2ConfigManager;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.notify.launcher.utils.funs.NotifyItemTypeLottTop1Impl;
import com.donews.notify.launcher.utils.funs.NotifyItemTypeLotBottom5Impl;
import com.donews.notify.launcher.utils.funs.NotifyItemTypeLottTop2Impl;
import com.donews.notify.launcher.utils.funs.NotifyItemTypeLottTop3Impl;
import com.donews.notify.launcher.utils.funs.NotifyItemTypeLottTop4Impl;
import com.donews.notify.launcher.utils.funs.NotifyItemTypeTop0Impl;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;
import com.tencent.mmkv.MMKV;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * @author lcl
 * Date on 2022/1/4
 * Description:
 * 顶部桌面通知工具，支持的类型：{@link NotifyItemType}
 */
public class NotifyItemUtils {

    //是否开启通知的debug模式。开启之后再UI模板筛选时。就跳过后台逻辑条件可以
    private static boolean isOpenNotifyDebug = BuildConfig.DEBUG && false;

    private static MMKV mmkv;
    /**
     * 今天抽奖次数
     */
    private static String TODAY_LOTTERY_TIMES = "todayLotteryTimes";
    //------------------------本地次数限制相关的key------------------------------
    //本地配置保存的配置文件
    private static String allowDayCountFile = "notify_allowDayCount_file";
    //本地记录着通知当天打开的次数的配置
    private static String allowDayCountKey = "notify_allowDayCountKey";
    //获取天数，当前生效的是哪一天的配置(因为隔天需要清除)
    private static String allowDayKey = "notify_allowDayKey";
    //------------------------本地保存上一次显示的信息相关key------------------------------
    //上一次显示的弹窗保存的文件
    private static String notifyLastShowFile = "notify_lastShow_file";
    //上一次显示的弹窗保存的key,格式：分类id|UI模板id
    private static String notifyLastShowFlgKey = "notify_last_show_flg";

    /**
     * 所有通知类型所支持的处理器集合，需要新增UI模板的处理。将其加入到此集合即可
     */
    public static final Map<NotifyItemType, AbsNotifyInvokTask> notifyTypeInvokList = new HashMap<NotifyItemType, AbsNotifyInvokTask>() {
        {
            //(UI模板0) 最原始模板：桌面顶部
            put(NotifyItemType.TYPE_TOP_1, new NotifyItemTypeTop0Impl());
            //(UI模板1) 抽奖引导1：桌面顶部
            put(NotifyItemType.TYPE_LOTT_TOP_1, new NotifyItemTypeLottTop1Impl());
            //(UI模板2) 抽奖引导2：顶部
            put(NotifyItemType.TYPE_LOTT_TOP_2, new NotifyItemTypeLottTop2Impl());
            //(UI模板3) 红包引导1：顶部 — 头像、红包引导1：顶部 — 昵称
            put(NotifyItemType.TYPE_LOTT_TOP_3, new NotifyItemTypeLottTop3Impl());
            //(UI模板4) 红包引导2：桌面顶部
            put(NotifyItemType.TYPE_LOTT_TOP_4, new NotifyItemTypeLottTop4Impl());
            //(UI模板5) 红包引导3：底部 — 金额、红包引导4：底部 — 配图
            put(NotifyItemType.TYPE_LOTT_BOTTOM_5, new NotifyItemTypeLotBottom5Impl());
        }
    };

    /**
     * 初始化设置应该显示的通知模板
     *
     * @param activity     页面对象
     * @param targetView   显示内容的目标视图容器
     * @param lastBindTask 最后更新视图的机会(再数据绑定完成之后的回调。提供给上层更改视图的机会)
     */
    public static void initNotifyParams(FragmentActivity activity,
                                        NotifyAnimationView targetView,
                                        Runnable lastBindTask) {
        List<Notify2DataConfigBean.UiTemplat> uiTemplatList = getMeetConditionalUiTemplats();
        if (uiTemplatList.isEmpty()) {
            activity.finish(); //直接退出
            return;
        }
        //从任务池中挑选需要显示的UI模板
        Notify2DataConfigBean.UiTemplat uiTemp = getShowUiTemplat(uiTemplatList);
        if (uiTemp == null) {
            activity.finish(); //没有可现实的模板，直接退出
            return;
        }
        //保存上一次显示的类型。防止重复显示
        String saveShowFlg = uiTemp.notifyTypeId + "|" + uiTemp.id;
        com.blankj.utilcode.util.SPUtils.getInstance(notifyLastShowFile).put(notifyLastShowFlgKey, saveShowFlg);
        targetView.setTag(uiTemp);
        //处理数据
        for (NotifyItemType notifyItemType : NotifyItemType.values()) {
            if (notifyItemType.typeId == uiTemp.id) {
                targetView.notifyType = notifyItemType;
                if (uiTemp.orientation < 0) {
                    targetView.orientation = notifyItemType.orientation;
                } else {
                    targetView.orientation = uiTemp.orientation;
                }
                break;
            }
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
        if (targetView.orientation == 0) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        }
        if (targetView.orientation == 1) {
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        } else if (targetView.orientation >= 2) {
            lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        }
        targetView.setLayoutParams(lp);
        //添加当前类型的通知视图和绑定数据
        addItemView(activity, targetView, targetView.notifyType, lastBindTask);
    }

    /**
     * 获取满足条件的UI模板集合
     *
     * @return
     */
    public static List<Notify2DataConfigBean.UiTemplat> getMeetConditionalUiTemplats() {
        initAdCountMMkv();
        //当前的时间
        long currentTime = com.donews.utilslibrary.utils.SPUtils.getLongInformain(TIME_SERVICE, 0L) * 1000;
        //获取本地的保存已经限制的次数(各种类型的通知已经提示过的次数)
        // key:分类id，value:分类当日已经展示的次数
        Map<Integer, Integer> notifyCountMap = new HashMap<Integer, Integer>();
        //获取当前生效的日期(格式:yyyyMMdd)
        String localAllowDay = com.blankj.utilcode.util.SPUtils.getInstance(allowDayCountFile).getString(allowDayKey);
        if (localAllowDay != null && localAllowDay.length() > 0) {
            //检查是否超过了一天。超过需要清除本地的存储
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            String currentTimeDay = sf.format(new Date(currentTime));
            if (!currentTimeDay.equals(sf.format(new Date(localAllowDay)))) {
                //已经超过一天了。更新配置
                com.blankj.utilcode.util.SPUtils.getInstance(allowDayCountFile).put(allowDayKey, currentTimeDay);
                com.blankj.utilcode.util.SPUtils.getInstance(allowDayCountFile).put(allowDayCountKey,
                        GsonUtils.toJson(notifyCountMap));
                updateTodayLotteryCount();
                localAllowDay = currentTimeDay;
            } else {
                //没有超过一天。那么直接读取本地的配置信息
                String localCountJson = com.blankj.utilcode.util.SPUtils.getInstance(allowDayCountFile)
                        .getString(allowDayCountKey, "");
                if (localCountJson.length() > 0) {
                    notifyCountMap = GsonUtils.fromLocalJson(localCountJson, notifyCountMap.getClass());
                    if (notifyCountMap == null) {
                        notifyCountMap = new HashMap<>();
                    }
                }
            }
        }

        //获取注册时间
        long regTime = Long.parseLong(AppInfo.getUserRegisterTime());
        //今日已经打开的红包数量
        int openRedPackCount = SPUtils.getInformain(KeySharePreferences.OPENED_RED_PACKAGE_COUNTS, 0);
        //今日已经获取了抽奖码的数量
        int getLotteryCodeCount = getTodayLotteryCount();
        List<Notify2DataConfigBean.UiTemplat> uiTemplats = new ArrayList<>();
        List<Notify2DataConfigBean.NotifyItemConfig> notifyConfigs =
                Notify2ConfigManager.Ins().getNotifyConfigBean().notifyConfigs;
        if (notifyConfigs == null || notifyConfigs.isEmpty()) {
            return uiTemplats;
        }

        for (Notify2DataConfigBean.NotifyItemConfig notifyConfig : notifyConfigs) {
            if (notifyConfig.uiTemplate == null || notifyConfig.uiTemplate.isEmpty()) {
                continue; //没有判断价值
            }
            if (!notifyConfig.isOpen) {
                continue; //通知已关闭,跳过
            }
            //判断是否超过最大的显示次数
            int typeShowCount = 0;
            if (notifyCountMap.get(notifyConfig.id) != null) {
                typeShowCount = notifyCountMap.get(notifyConfig.id);
            }
            if (typeShowCount >= notifyConfig.notifyMaxCount) {
                continue; //当前分类通知已经显示达到最大值。不在参与显示
            }
            //开始判断条件
            boolean regTimeTj = notifyConfig.registerMore < 0 ||
                    currentTime - regTime > notifyConfig.registerMore * 1000L;
            boolean dayTj2 = (notifyConfig.dayReceiveRedCount < 0 ||
                    openRedPackCount == notifyConfig.dayReceiveRedCount) &&
                    (notifyConfig.dayLotteryCodeCount < 0 ||
                            getLotteryCodeCount == notifyConfig.dayLotteryCodeCount);
            //检查上诉条件是否足够(同时优先判断是否开启debug跳过)
            if (isOpenNotifyDebug || regTimeTj && dayTj2) {
                for (Notify2DataConfigBean.UiTemplat uiModel : notifyConfig.uiTemplate) {
                    if (!uiModel.isOpen) {
                        continue; //当前通知未打开。忽略
                    }
                    uiTemplats.add(uiModel); //将通知添加到可现实的模板池
                }
            }
        }
        return uiTemplats;
    }

    /**
     * 添加当前类型的视图并绑定数据
     *
     * @param activity     生命周期组件
     * @param targetView   显示内容的目标容器
     * @param type         通知类型
     * @param lastBindTask 绑定之后的回调任务
     */
    public static void addItemView(
            FragmentActivity activity,
            NotifyAnimationView targetView,
            NotifyItemType type,
            Runnable lastBindTask) {
        View v = LayoutInflater.from(targetView.getContext())
                .inflate(type.typeLayoutId, targetView, false);
        targetView.addView(v);
        AbsNotifyInvokTask taskInvok = notifyTypeInvokList.get(type);
        if (taskInvok == null) {
            ToastUtil.showShort(activity, "抱歉,暂不支持处理此类通知");
            return;
        }
        taskInvok.attchActivity(activity);
        taskInvok.bindTypeData(targetView, lastBindTask);
    }

    /**
     * 从任务池中获取需要显示的UI模板:
     * 条件：上一次显示的分类、上一次显示的模板
     * 目的：禁止和上一次显示同一个模板。以及同一类模板
     *
     * @param uiTemplatList 模板池
     * @return 需要显示的UI模板
     */
    private static Notify2DataConfigBean.UiTemplat getShowUiTemplat(List<Notify2DataConfigBean.UiTemplat> uiTemplatList) {
        String lastShowFlg = com.blankj.utilcode.util.SPUtils.getInstance(notifyLastShowFile).getString(notifyLastShowFlgKey, "");
        //最终筛选出来的数据源
        List<Notify2DataConfigBean.UiTemplat> newDataResu = new ArrayList<>();
        if (lastShowFlg.isEmpty()) {
            newDataResu.addAll(uiTemplatList);
            return getRandomUITemp(newDataResu);
        }
        String[] types = lastShowFlg.split("\\|");
        if (types.length != 2) {
            newDataResu.addAll(uiTemplatList);
            return getRandomUITemp(newDataResu);
        }
        int lastTypeId = Integer.parseInt(types[0]);
        int lastUiTemplatId = Integer.parseInt(types[1]);
        //将数据转为map。好判断分类
        Map<Integer, List<Notify2DataConfigBean.UiTemplat>> newTypeMap = new HashMap<>();
        for (Notify2DataConfigBean.UiTemplat uiTemplat : uiTemplatList) {
            if (newTypeMap.containsKey(uiTemplat.notifyTypeId)) {
                newTypeMap.get(uiTemplat.notifyTypeId).add(uiTemplat);
            } else {
                List<Notify2DataConfigBean.UiTemplat> temLs = new ArrayList<>();
                temLs.add(uiTemplat);
                newTypeMap.put(uiTemplat.notifyTypeId, temLs);
            }
        }
        //检查同分类和通UI模板
        if (newTypeMap.keySet().size() == 1 &&
                newTypeMap.containsKey(lastTypeId)) {
            //只有一个分类。并且和上次分类相同。也就是不得不用相同的分类来显示了
            List<Notify2DataConfigBean.UiTemplat> converList = newTypeMap.get(lastTypeId);
            if (converList.size() <= 1) {
                //只有一个。就算是上一次相同也没办法的了。直接加入
                newDataResu.addAll(converList);
            } else {
                //大于1个。可以排除和上一次相同的数据。最起码不会造成无数据显示
                for (Notify2DataConfigBean.UiTemplat uiTemplat : converList) {
                    if (uiTemplat.id != lastUiTemplatId) {
                        newDataResu.add(uiTemplat);
                    }
                }
            }
        } else {
            //大于一个分类。直接移除上一次显示的分类即可。防止同类型显示
            newTypeMap.remove(lastTypeId);
            Set<Integer> keys = newTypeMap.keySet();
            for (Integer key : keys) {
                newDataResu.addAll(newTypeMap.get(key));
            }
        }
        return getRandomUITemp(newDataResu);
    }

    /**
     * 随机获取一个UI模板显示
     *
     * @param uiTemplatList
     * @return
     */
    private static Notify2DataConfigBean.UiTemplat getRandomUITemp(
            List<Notify2DataConfigBean.UiTemplat> uiTemplatList) {
        if (uiTemplatList.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int uiTempPos = random.nextInt(uiTemplatList.size());
        return uiTemplatList.get(uiTempPos);
    }

    /**
     * 获取今日参与抽奖的次数
     *
     * @return Int
     */
    private static int getTodayLotteryCount() {
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
