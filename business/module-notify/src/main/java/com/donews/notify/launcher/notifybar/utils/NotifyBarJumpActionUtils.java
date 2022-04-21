package com.donews.notify.launcher.notifybar.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.alibaba.android.arouter.launcher.ARouter;
import com.donews.base.base.BaseApplication;
import com.donews.base.utils.ToastUtil;
import com.donews.common.BuildConfig;
import com.donews.common.router.RouterActivityPath;
import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.notify.launcher.configs.baens.NotifyBarDataConfig;
import com.donews.utilslibrary.analysis.AnalysisUtils;
import com.donews.utilslibrary.dot.Dot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author lcl
 * Date on 2022/1/13
 * Description:
 * 通知栏的跳转处理工具
 */
public class NotifyBarJumpActionUtils {

    //支持的协议：http、https
    private static final String SchemeHttp = "http";
    //支持的本地处理协议:notifyAction
    private static final String NotifyAction = "notifyAction";

    /**
     * 通知栏的点击跳转
     *
     * @param context 上下文
     * @param data    UI配置的数据
     * @return 返回跳转的逻辑Intent，null:表示处理失败
     */
    public static Intent jumpBuild(Context context, NotifyBarDataConfig.NotifyBarUIDataConfig data) {
        String action = data.action;
        if (action == null || action.isEmpty()) {
            return null;//上层自己处理
        }
        Uri uri = Uri.parse(action);
        String scheme = uri.getScheme(); //获取协议部分
        if (scheme == null || scheme.isEmpty()) {
            return null;//不支持的协议。不处理跳转逻辑
        }
        AnalysisUtils.onEventEx(BaseApplication.getInstance(),
                Dot.Notify_Bar_Click, "样式:" + data.uiType);
        if (scheme.startsWith(SchemeHttp)) {
            AnalysisUtils.onEventEx(BaseApplication.getInstance(),
                    Dot.Notify_Bar_Click_To, "站外");
            String title = uri.getQueryParameter("title");
            if (title == null || title.isEmpty()) {
                title = "通知详情";
            }
            Class<?> activityCzz = null;
            try {
                activityCzz = Class.forName("com.donews.web.ui.WebViewObjActivity");
                Intent intent = new Intent(context, activityCzz);
                intent.putExtra("title", title)
                        .putExtra("url", action);
                return intent;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;//处理掉http相关协议的跳转
        } else if (scheme.equals(NotifyAction)) {
            //自定义的本地协议处理
            try {
                AnalysisUtils.onEventEx(BaseApplication.getInstance(),
                        Dot.Notify_Bar_Click_To, "站内");
                String actPageCzz = uri.getHost();
                if (actPageCzz == null || actPageCzz.isEmpty()) {
                    if (BuildConfig.DEBUG) {
                        ToastUtil.show(context, "未找到跳转的页面。请检查跳转参数配置");
                    }
                    return null;
                }
                Class<?> activityCzz = Class.forName(actPageCzz);
                Intent intent = new Intent(context, activityCzz);
                if (buildParams(intent, uri)) {
                    return intent;
                }
                if (BuildConfig.DEBUG) {
                    ToastUtil.show(context, "跳转处理异常。请检查跳转参数配置");
                }
                return null;//参数处理错误了。上层自己处理
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    ToastUtil.show(context, "点击跳转相关逻辑处理错误：" + e);
                    e.printStackTrace();
                }
                return null;
            }
        }
        return null;
    }

    /**
     * 处理构建参数，跳转本地页面参数处理
     *
     * @param intent 跳转对象
     * @param uri    参数相关对象
     * @return T:处理成功，F:参数处理出错
     */
    private static boolean buildParams(Intent intent, Uri uri) {
        try {
            Set<String> paramsNames = uri.getQueryParameterNames();
            Iterator<String> iteNames = paramsNames.iterator();
            while (iteNames.hasNext()) {
                String keyName = iteNames.next();
                String paraValue = uri.getQueryParameter(keyName);
                if (paraValue == null) {
                    continue;
                }
                if (paraValue.length() <= 1) {
                    //无法直接判断了。设置错误或者其他。直接当String参数设置
                    intent.putExtra(keyName, paraValue);
                    continue;
                }
                if (!paraValue.startsWith("(")) {
                    //开始标记不对。直接当String
                    intent.putExtra(keyName, paraValue);
                    continue;
                }
                if (paraValue.indexOf(")", 1) == -1) {
                    //开始标记正确。但是没有结束标记。直接当String
                    intent.putExtra(keyName, paraValue);
                    continue;
                }
                int endKHPos = paraValue.indexOf(")", 1);
                String flgTypeFlg = paraValue.substring(1, endKHPos);
                if (flgTypeFlg.isEmpty()) {
                    //类型未设置。直接当String
                    intent.putExtra(keyName, paraValue);
                    continue;
                }
                String dataValue = paraValue.substring(endKHPos + 1);
                //是否为数组集合类型(标记以 [ 开头表示是一个集合类型)
                boolean isArray = flgTypeFlg.startsWith("[");
                //是否为List集合。决定是否转换为List
                boolean isConvertList = flgTypeFlg.endsWith("]");
                int flgLen = flgTypeFlg.length();
                if (flgLen > 3) {
                    //类型处理失败。类型不被支持。直接当String
                    intent.putExtra(keyName, paraValue);
                    continue;
                }
                if (isArray) {
                    //是数组需要重新处理下,已经记录是。将类型标记处理成当前代表的类型
                    if (isConvertList) {
                        flgTypeFlg = flgTypeFlg.substring(1, flgTypeFlg.length() - 1);
                    } else {
                        flgTypeFlg = flgTypeFlg.substring(1);
                    }
                }
                if (!setParams2Intent(intent, flgTypeFlg, isArray, isConvertList, keyName, dataValue)) {
                    //类型处理失败。类型不被支持。直接当String
                    intent.putExtra(keyName, paraValue);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置参数到Intent参数中
     *
     * @param intent        跳转对象
     * @param typeFlg       数据的类型
     * @param isArray       是否为数组
     * @param isConvertList 是否转为List(只有String和Int)有效
     * @param paramsName    参数的名称
     * @param paramsValue   除了标记之外的参数值。实际需要处理的参数数据
     */
    private static boolean setParams2Intent(
            Intent intent, String typeFlg, boolean isArray, boolean isConvertList, String paramsName, String paramsValue) {
        try {
            if (typeFlg.equals("S")) {
                //字符类型
                setData(String.class, intent, isArray, isConvertList, paramsName, paramsValue);
                return true;
            } else if (typeFlg.equals("I")) {
                //整型
                setData(Integer.TYPE, intent, isArray, isConvertList, paramsName, paramsValue);
                return true;
            } else if (typeFlg.equals("L")) {
                //长整型
                setData(Long.TYPE, intent, isArray, isConvertList, paramsName, paramsValue);
                return true;
            } else if (typeFlg.equals("F")) {
                //浮点型
                setData(Float.TYPE, intent, isArray, isConvertList, paramsName, paramsValue);
                return true;
            } else if (typeFlg.equals("D")) {
                //长浮点型
                setData(Double.TYPE, intent, isArray, isConvertList, paramsName, paramsValue);
                return true;
            } else if (typeFlg.equals("B")) {
                //boolean 逻辑类型
                setData(Boolean.TYPE, intent, isArray, isConvertList, paramsName, paramsValue);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 设置指定类型的数据
     *
     * @param type
     * @param intent
     * @param isArray
     * @param isConvertList
     * @param paramsName
     * @param paramsValue
     */
    private static void setData(Class<?> type, Intent intent, boolean isArray, boolean isConvertList, String paramsName, String paramsValue) {
        if (isArray) {
            String[] paramAar = paramsValue.split("\\|");
            List<Object> filterList = new ArrayList<>();
            if (paramAar != null && paramAar.length > 0) {
                if (type == String.class) {
                    String[] paramAarNew = new String[paramAar.length];
                    for (int i = 0; i < paramAar.length; i++) {
                        String item = paramAar[i].trim();
                        if (item.isEmpty()) {
                            continue;
                        }
                        paramAarNew[i] = item;
                    }
                    if (isConvertList) {
                        ArrayList<String> list = new ArrayList<String>();
                        for (int i = 0; i < paramAarNew.length; i++) {
                            list.add(paramAarNew[i]);
                        }
                        intent.putStringArrayListExtra(paramsName, list);
                    } else {
                        intent.putExtra(paramsName, paramAarNew);
                    }
                } else if (type == Integer.TYPE) {
                    for (int i = 0; i < paramAar.length; i++) {
                        String item = paramAar[i].replace(" ", "");
                        if (item.isEmpty()) {
                            continue;
                        }
                        try {
                            filterList.add((int) Double.parseDouble(item));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    int[] paramAarNew = new int[filterList.size()];
                    for (int i = 0; i < filterList.size(); i++) {
                        paramAarNew[i] = (int) filterList.get(i);
                    }
                    if (isConvertList) {
                        ArrayList<Integer> list = new ArrayList<Integer>();
                        for (int i = 0; i < paramAarNew.length; i++) {
                            list.add(paramAarNew[i]);
                        }
                        intent.putIntegerArrayListExtra(paramsName, list);
                    } else {
                        intent.putExtra(paramsName, paramAarNew);
                    }
                } else if (type == Long.TYPE) {
                    for (int i = 0; i < paramAar.length; i++) {
                        String item = paramAar[i].replace(" ", "");
                        if (item.isEmpty()) {
                            continue;
                        }
                        try {
                            filterList.add((long) Double.parseDouble(item));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    long[] paramAarNew = new long[filterList.size()];
                    for (int i = 0; i < filterList.size(); i++) {
                        paramAarNew[i] = (long) filterList.get(i);
                    }
                    intent.putExtra(paramsName, paramAarNew);
                } else if (type == Float.TYPE) {
                    for (int i = 0; i < paramAar.length; i++) {
                        String item = paramAar[i].replace(" ", "");
                        if (item.isEmpty()) {
                            continue;
                        }
                        try {
                            filterList.add(Float.parseFloat(item));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    float[] paramAarNew = new float[filterList.size()];
                    for (int i = 0; i < filterList.size(); i++) {
                        paramAarNew[i] = (float) filterList.get(i);
                    }
                    intent.putExtra(paramsName, paramAarNew);
                } else if (type == Double.TYPE) {
                    for (int i = 0; i < paramAar.length; i++) {
                        String item = paramAar[i].replace(" ", "");
                        if (item.isEmpty()) {
                            continue;
                        }
                        try {
                            filterList.add(Double.parseDouble(item));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    double[] paramAarNew = new double[filterList.size()];
                    for (int i = 0; i < filterList.size(); i++) {
                        paramAarNew[i] = (double) filterList.get(i);
                    }
                    intent.putExtra(paramsName, paramAarNew);
                } else if (type == Boolean.TYPE) {
                    for (int i = 0; i < paramAar.length; i++) {
                        String item = paramAar[i].replace(" ", "");
                        if (item.isEmpty()) {
                            continue;
                        }
                        try {
                            filterList.add(Boolean.parseBoolean(item));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    boolean[] paramAarNew = new boolean[filterList.size()];
                    for (int i = 0; i < filterList.size(); i++) {
                        paramAarNew[i] = (boolean) filterList.get(i);
                    }
                    intent.putExtra(paramsName, paramAarNew);
                }
            }
        } else {
            String item = paramsValue.trim();
            if (!item.isEmpty()) {
                if (type != String.class) {
                    item = paramsValue.replace(" ", "");
                }
                if (type == String.class) {
                    intent.putExtra(paramsName, item);
                } else if (type == Integer.TYPE) {
                    try {
                        intent.putExtra(paramsName,
                                (int) Double.parseDouble(item));
                    } catch (Exception e) {
                        intent.putExtra(paramsName,
                                item);
                    }
                } else if (type == Long.TYPE) {
                    try {
                        intent.putExtra(paramsName,
                                (long) Double.parseDouble(item));
                    } catch (Exception e) {
                        intent.putExtra(paramsName,
                                item);
                    }
                } else if (type == Float.TYPE) {
                    try {
                        intent.putExtra(paramsName,
                                Float.parseFloat(item));
                    } catch (Exception e) {
                        intent.putExtra(paramsName,
                                item);
                    }
                } else if (type == Double.TYPE) {
                    try {
                        intent.putExtra(paramsName,
                                Double.parseDouble(item));
                    } catch (Exception e) {
                        intent.putExtra(paramsName,
                                item);
                    }
                } else if (type == Boolean.TYPE) {
                    try {
                        intent.putExtra(paramsName,
                                Boolean.parseBoolean(item));
                    } catch (Exception e) {
                        intent.putExtra(paramsName,
                                item);
                    }
                }
            }
        }
    }
}
