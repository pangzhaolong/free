package com.donews.notify.launcher.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.donews.base.utils.ToastUtil;
import com.donews.common.NotifyLuncherConfigManager;
import com.donews.notify.R;
import com.donews.notify.launcher.NotifyActivity;
import com.donews.notify.launcher.NotifyAnimationView;
import com.donews.notify.launcher.NotifyInitProvider;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.notify.launcher.utils.funs.NotifyItemTypeLottTop1Impl;
import com.donews.notify.launcher.utils.funs.NotifyItemTypeLottottom2Impl;
import com.donews.notify.launcher.utils.funs.NotifyItemTypeTop1Impl;
import com.donews.utilslibrary.utils.AppInfo;
import com.donews.utilslibrary.utils.KeySharePreferences;
import com.donews.utilslibrary.utils.SPUtils;
import com.tencent.mmkv.MMKV;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lcl
 * Date on 2022/1/4
 * Description:
 * 顶部桌面通知工具，支持的类型：{@link NotifyItemType}
 */
public class NotifyItemUtils {

    private static MMKV mmkv;
    /**
     * 今天抽奖次数
     */
    private static String TODAY_LOTTERY_TIMES = "todayLotteryTimes";

    /**
     * 所有通知类型所支持的处理器集合
     */
    public static final Map<NotifyItemType, AbsNotifyInvokTask> notifyTypeInvokList = new HashMap<NotifyItemType, AbsNotifyInvokTask>() {
        {
            //默认操作方式，最原始的大小图模式
            put(NotifyItemType.TYPE_TOP_1, new NotifyItemTypeTop1Impl());
            //抽奖引导1的消息末班样式
            put(NotifyItemType.TYPE_LOTT_TOP_1, new NotifyItemTypeLottTop1Impl());
            //抽奖和红包底部弹窗引导样式
            put(NotifyItemType.TYPE_LOTT_BOTTOM_5, new NotifyItemTypeLottottom2Impl());
        }
    };

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
     * 获取满足条件的UI模板集合
     *
     * @return
     */
    public static List<Notify2DataConfigBean.UiTemplat> getMeetConditionalUiTemplats() {
        initAdCountMMkv();
        //获取注册时间
        String regTime = AppInfo.getUserRegisterTime();
        //今日已经打开的红包数量
        String openRedPackCount = SPUtils.setInformain(KeySharePreferences.OPENED_RED_PACKAGE_COUNTS, mOpenedRpCounts);
        //今日已经获取了抽奖码的数量
        int getLotteryCodeCount = getTodayLotteryCount();
    }

    /**
     * 获取今日参与抽奖的次数
     *
     * @return Int
     */
    private static int getTodayLotteryCount() {
        return mmkv.decodeInt(TODAY_LOTTERY_TIMES, 0);
    }

    //初始化数据存储
    private static void initAdCountMMkv() {
        if (mmkv != null) {
            return;
        }
        mmkv = MMKV.mmkvWithID("AdCount", MMKV.MULTI_PROCESS_MODE);
    }
}
