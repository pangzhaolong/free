package com.donews.notify.launcher.notifybar.utils;

import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.notify.launcher.configs.baens.NotifyBarDataConfig;
import com.donews.notify.launcher.notifybar.NotifyBarManager;
import com.donews.notify.launcher.utils.NotifyLog;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author lcl
 * Date on 2022/3/9
 * Description:
 * 条件执行和处理工具
 */
public class CollectionInvokUtil {

    //逻辑符号 = 并且
    private static final String Logic_And = "&";
    //逻辑符号 = 或者
    private static final String Logic_Or = "|";
    //Class和方法的分隔符
    private static final String Method_Splie = "#";
    //表达式开始符号
    private static final String BDS_START = "(";
    //表达式结束符号
    private static final String BDS_EDN = ")";

    //-----------括号内的各种支持符号---------------
    //表达式内部条件分隔符
    private static final String BDS_FLG_Splie = ",";
    //表达式大于符号
    private static final String BDS_FLG_DY = ">";
    //表达式小于符号
    private static final String BDS_FLG_XY = "<";
    //表达式区间符号
    private static final String BDS_FLG_QJ = "-";

    /**
     * 筛选最终需要显示的样式数据显示,调用此方法表示必被显示。所以会完成各种记录、判断之后返回一个可现实的数据
     *
     * @param sources 从当前数据源中选中一个显示
     *                一把来源于：{@link #getShowUIStyles}
     * @return null 没有可显示的内容，否则为需要显示的内容
     */
    public static NotifyBarDataConfig.NotifyBarUIDataConfig getFinalShowStyle(
            List<NotifyBarDataConfig.NotifyBarUIDataConfig> sources) {
        NotifyLog.logBar("开始进行显示数据筛选:" + sources.size());
        if (sources.size() <= 0) {
            NotifyLog.logBar("没有可现实的内容。放弃数据筛选");
            return null;
        }
        if (sources.size() == 1) {
            NotifyLog.logBar("只有一个可显示数据。直接使用。不在继续判断");
            NotifyBarManager.Ins().setLastShowId(getUIStyleConfigId(sources.get(0)));
            updateShowUILocalConfig(sources.get(0));
            return sources.get(0);
        }
        List<NotifyBarDataConfig.NotifyBarUIDataConfig> newDataSource = new ArrayList<>();
        String lastShowId = NotifyBarManager.Ins().getLastShowId();
        for (NotifyBarDataConfig.NotifyBarUIDataConfig item : sources) {
            if (!getUIStyleConfigId(item).equals(lastShowId)) {
                newDataSource.add(item);
            }
        }
        int pos = new Random().nextInt(newDataSource.size());
        if (pos >= newDataSource.size()) {
            pos = newDataSource.size() - 1;
        }
        //更新显示的数据
        NotifyBarManager.Ins().setLastShowId(getUIStyleConfigId(newDataSource.get(pos)));
        updateShowUILocalConfig(newDataSource.get(pos));
        return newDataSource.get(pos);
    }

    /**
     * 获取时间+条件+限制的UI样式数据。也就是允许显示的样式
     *
     * @return null:没有可现实的数据。返回需要显示的样式数据
     */
    public static List<NotifyBarDataConfig.NotifyBarUIDataConfig> getShowUIStyles() {
        List<NotifyBarDataConfig.NotifyBarUIDataConfig> result = new ArrayList<>();
        NotifyBarDataConfig barConfig = NotifyBarManager.Ins().getNotifyBarConfigBean();
        if (barConfig == null || barConfig.notifyConfig == null) {
            return result;
        }
        if (System.currentTimeMillis() - NotifyBarManager.Ins().getLastShowTime() <
                barConfig.notifyShowLastOpenInterval) {
            NotifyLog.logBar("距离上一次显示时间间隔小于配置要求。放弃处理逻辑");
            return result;
        }
        int allMaxCount = 0; //总次数
        for (NotifyBarDataConfig.NotifyBarItemConfig itemConfig : barConfig.notifyConfig) {
            allMaxCount += NotifyBarManager.Ins().getCurrentDayShowCount(itemConfig.typeId, false);
        }
        if (allMaxCount >= barConfig.notifyMaxShowCount) {
            NotifyLog.logBar("已经达到总的最大次数限制,放弃继续判断其他逻辑");
            return result;
        }
        for (int i = 0; i < barConfig.notifyConfig.size(); i++) {
            //获取每个时间区间的配置
            NotifyBarDataConfig.NotifyBarItemConfig itemConfig = barConfig.notifyConfig.get(i);
            if (itemConfig.isOpen &&
                    checkCurrentTimeIsRang(itemConfig.time) &&
                    checkConfigCondition(
                            barConfig.conditionsPools,
                            itemConfig.judgeConditions,
                            itemConfig.anchorCollection) &&
                    NotifyBarManager.Ins().getCurrentDayShowCount(itemConfig.typeId, false) < itemConfig.notifyMaxShowCount) {
                //时间复合，并且满足条件的UI模板
                result.addAll(itemConfig.notifyUISytles);
            }
        }
        NotifyLog.logBar("样式筛选完成。共:" + result.size() + "个可用显示项");
        return result;
    }

    /**
     * 检查配置的条件是否满足
     *
     * @param conditionsPools  条件池
     * @param conditions       条件
     * @param anchorCollection 每个条件对应的锚定池下标
     * @return
     */
    private static boolean checkConfigCondition(
            List<Notify2DataConfigBean.ConditionalProcessItem> conditionsPools,
            List<Notify2DataConfigBean.JudgeConditionItem> conditions,
            String anchorCollection) {
        List<Notify2DataConfigBean.ConditionalProcessItem> invokConditions = new ArrayList<>();
        if (conditions == null || conditions.isEmpty()) {
            NotifyLog.logBar("invokCondition 没有配置条件(1),直接过");
            return true; //没有条件。默认全部满足
        }
        //处理条件。选取处获取锚定值的方法集合
        if (anchorCollection.isEmpty() ||
                anchorCollection.replace(" ", "").isEmpty()) {
            NotifyLog.logBar("invokCondition 没有配置锚定池处理方法编号(2),直接过");
            return true;//没有配置条件。直接都满足
        }
        String[] tjInvokArr = anchorCollection
                .replace(" ", "")
                .split(BDS_FLG_Splie);
        if (tjInvokArr.length <= 0) {
            NotifyLog.logBar("invokCondition 锚定池处理方法编号不符合规范(3),直接过");
            return true; //都不符合规范。默认当没有配置处理
        }
        for (int i = 0; i < tjInvokArr.length; i++) {
            try {
                int pos = strToIntNumber(tjInvokArr[i]);
                if (pos >= conditionsPools.size() || pos < 0) {
                    NotifyLog.logBar("invokCondition 锚定值条件配置错误。超过锚定池范围(5),只跳过该条件");
                    invokConditions.add(null);
                } else {
                    invokConditions.add(conditionsPools.get(pos));
                }
            } catch (Exception e) {
                invokConditions.add(null);
                NotifyLog.logBar("invokCondition 锚定值条件配置错误(5-1),e=" + e);
            }
        }
        if (invokConditions.isEmpty()) {
            return true; //每个条件都没有锚定值获取方法。直接返回全部满足
        }
        //条件的结果集合(条件处理完成的结果)
        List<Boolean> conditionResults = new ArrayList<>();
        //各种条件之间的连接关系(只支持：& ||),默认：&
        List<String> conditionResultRelationship = new ArrayList<>();
        //解析条件
        for (int i = 0; i < conditions.size(); i++) {
            Notify2DataConfigBean.JudgeConditionItem conditionItem = conditions.get(i);
            String condStr = conditionItem.condition;
            //处理条件连接符号
            if (condStr.contains(Logic_And)) {
                conditionResultRelationship.add(Logic_And);
            } else {
                //没有and连接符
                if (condStr.contains(Logic_Or)) {
                    conditionResultRelationship.add(Logic_Or);
                } else {
                    //没有关系连接符,添加默认连接符：and
                    conditionResultRelationship.add(Logic_And);
                }
            }
            //处理条件内的条件
            if (i >= invokConditions.size()) {
                //没有可匹配的条件锚定,直接返回true
                conditionResults.add(true);
            } else {
                conditionResults.add(runSingCondition(conditionItem, invokConditions.get(i)));
            }
        }
        //开始验证逻辑
        if (conditionResults.size() <= 1) {
            if (conditionResults.isEmpty()) {
                return true;//没有条件。无条件满足
            }
            //只有一个条件。根据实际情况返回
            return conditionResults.get(0);
        }
        //输出匹配之后的条件详情
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < conditions.size(); i++) {
            sb.append(conditions.get(i).condition + " ");
        }
        NotifyLog.logBarNotToast("\n【主条件结果集】=" + sb.toString(), conditionResults);
        //超过一个条件。那么根据条件返回
        return checkTJResult(conditionResults, conditionResultRelationship);
    }

    /**
     * 检查结果集合和关系运算之间运算之后的最终解雇。此结果是最后返回的结果集合
     *
     * @param results            各条件的结果集合
     * @param resultRelationship 每个条件对下一个条件的运算方式集合
     * @return
     */
    private static boolean checkTJResult(List<Boolean> results, List<String> resultRelationship) {
        boolean result = results.get(0);
        for (int i = 0; i < results.size() - 1; i++) {
            if (Logic_And.equals(resultRelationship.get(i))) {
                //计算当前条件和下一个条件逻辑结果
                result = result && results.get(i + 1);
            } else if (Logic_Or.equals(resultRelationship.get(i))) {
                //计算当前条件和下一个条件逻辑结果
                result = result || results.get(i + 1);
            } else {
                //如果未知的。那么放弃这个条件。直接进行下一个
                NotifyLog.logBar("checkTJResult 未知的逻辑运算符，请检查(-5):" + resultRelationship.get(i));
            }
        }
        return result;
    }

    /**
     * 解析和处理单个条件是否满足,也就是处理单个主条件。判断是否满足
     *
     * @param conditionItem 条件表达式
     * @param invokItem     该条件的锚定方法
     * @return
     */
    private static boolean runSingCondition(
            Notify2DataConfigBean.JudgeConditionItem conditionItem,
            Notify2DataConfigBean.ConditionalProcessItem invokItem) {
        if (invokItem == null) {
            NotifyLog.logBar("没有解析到锚定方法,请检查(-10):条件=" + conditionItem.condition);
            return true;//锚定错误或者没有锚定
        }
        if (invokItem.executioMethod == null || invokItem.executioMethod.isEmpty()) {
            NotifyLog.logBar("没有解析到锚定方法,请检查(-11):条件=" + conditionItem.condition);
            return true;//锚定方法没有或者为空
        }
        Class<?> invokClazz = null;
        Method invokMethod = null;
        try {
            String condStr = conditionItem.condition
                    .replace(" ", "");
            if (condStr.length() < 2) {
                NotifyLog.logBar("中台条件条件表达式不符合规范,请检查(-1)");
                return false; //条件不满足表达式要求
            }
            if (!condStr.startsWith(BDS_START)) {
                NotifyLog.logBar("中台条件条件表达式不符合规范,请检查(-2)");
                return false; //条件不满足表达式要求
            }
            if (!condStr.endsWith(BDS_EDN)) {
                if (condStr.lastIndexOf(BDS_EDN) != condStr.length() - 2) {
                    NotifyLog.logBar("中台条件条件表达式不符合规范,请检查(-3)");
                    return false;//条件不满足表达式要求
                }
            }
            //去掉括号之后的单纯表达式内容(括号内的表达式内容)
            String bdsConent = condStr.substring(
                    condStr.indexOf(BDS_START) + 1,
                    condStr.lastIndexOf(BDS_EDN)
            );
            if (!invokItem.executioMethod.contains(Method_Splie)) {
                NotifyLog.logBar("中台条件锚定配置不符合规范,请检查(1)");
                return true;
            }
            String[] cmLs = invokItem.executioMethod.split(Method_Splie);
            if (cmLs.length != 2) {
                NotifyLog.logBar("中台条件锚定配置不符合规范,请检查(2)");
                return true;
            }
            invokClazz = Class.forName(cmLs[0]);
            try {
                invokMethod = invokClazz.getMethod(cmLs[1]);
            } catch (Exception e) {
                try {
                    invokMethod = invokClazz.getDeclaredMethod(cmLs[1]);
                } catch (Exception de) {
                    NotifyLog.logBar("中台条件锚定配置不符合规范,请检查,e=" + de);
                    return true;
                }
            }
            if (Integer.TYPE != invokMethod.getReturnType()) {
                NotifyLog.logBar("所配置的锚定方法返回类型不正确，只允许返回(int)类型。请检查(1)");
                return true;
            }
            int modifiers = invokMethod.getModifiers();
            //依赖的执行对象。默认为静态方法。依赖class
            Object invokObj = invokClazz;
            if (!Modifier.isStatic(modifiers)) {
                //如果不是静态方法。更新依赖对象
                try {
                    invokObj = invokClazz.newInstance();
                } catch (Exception e) {
                    NotifyLog.logBar("配置锚定方法为非静态方法。请提供默认构造函数,请检查：" + invokClazz.getName());
                }
            }
            //锚定方法返回的实际锚定值
            int anchorNumber = -1;
            try {
                anchorNumber = (int) invokMethod.invoke(invokObj);
                NotifyLog.logBarNotToast("[锚定值]：" + anchorNumber + ",方法：" + invokMethod.getName());
            } catch (Exception e) {
                NotifyLog.log("执行锚定方法出现异常,但不中断业务，e=" + e);
            }
            //开始处理动态逻辑
            //去掉括号。以及逻辑关系符号之后的字符串
            String[] conContentArr = bdsConent.split(BDS_FLG_Splie);
            //内部多个小条件的集合
            List<Boolean> innerTjList = new ArrayList<>();
            for (int i = 0; i < conContentArr.length; i++) {
                String checkTjFlg = checkIsContainsBDSFlg(conContentArr[i]);
                boolean innerTjResult = false;
                if (checkTjFlg.isEmpty()) {
                    //没有表达式,直接转换为数字,没有符号表示直接使用 '=' 即可
                    innerTjResult = invokBdsFlg(anchorNumber, strToIntNumber(conContentArr[i]), "=");
                } else {
                    if (checkTjFlg.equals(BDS_FLG_QJ)) {
                        //是一个集合。
                        String[] qjStrVal = conContentArr[i].split(BDS_FLG_QJ);
                        int[] qjIntVal = new int[qjStrVal.length];
                        for (int j = 0; j < qjStrVal.length; j++) {
                            qjIntVal[j] = strToIntNumber(qjStrVal[j]);
                        }
                        innerTjResult = invokBdsFlg(anchorNumber, qjIntVal);
                    } else {
                        // 正常的表达式处理
                        innerTjResult = invokBdsFlg(anchorNumber,
                                strToIntNumber(conContentArr[i]
                                        .replace(checkTjFlg, "")),
                                checkTjFlg);
                    }
                }
                innerTjList.add(innerTjResult);
            }
            boolean innerTjListResult = false;
            for (Boolean aBoolean : innerTjList) {
                if (aBoolean) {
                    //因为内部条件是小条件。所以所有条件之间是并且的关系
                    innerTjListResult = true;
                    break;
                }
            }
            //输出匹配之后的条件详情
            NotifyLog.logNotToast("\n内部小条件结果集=" + condStr, innerTjList);
            //将当前条件表达式的内容。结果返回
            return innerTjListResult;
        } catch (Exception e) {
            NotifyLog.log("中台条件锚定配置错误。请检查配置,e=" + e);
            return true;//配置错误。直接当没有处理
        }
    }

    /**
     * 执行条件，根据符号执行相关逻辑运算
     *
     * @param currValue 当前本地的标定值
     * @param tjValue   条件值
     * @param flg       运算标记
     * @return T:满足条件，F:不满足条件
     */
    private static boolean invokBdsFlg(int currValue, int tjValue, String flg) {
        if (flg.equals(BDS_FLG_DY)) {
            return currValue > tjValue;
        } else if (flg.equals(BDS_FLG_XY)) {
            return currValue < tjValue;
        } else {
            return currValue == tjValue;
        }
    }

    /**
     * 执行条件，根据符号执行相关逻辑运算
     *
     * @param currValue 当前本地的标定值
     * @param tjValue   条件区间值，0：起始值，1：结束值
     * @return T:满足条件，F:不满足条件
     */
    private static boolean invokBdsFlg(int currValue, int[] tjValue) {
        if (tjValue.length == 0) {
            return true; //没有指定区间。表示任意区间都可以
        }
        if (tjValue.length == 1) {
            return currValue >= tjValue[0]; //只要条件大于起始值即可
        } else {
            if (tjValue[1] > tjValue[0]) {
                return currValue >= tjValue[0] && currValue <= tjValue[1];
            } else {
                return currValue >= tjValue[1] && currValue <= tjValue[0];
            }
        }
    }

    /**
     * 检查子条件中。是否包含表达式符号(>、<、-)
     *
     * @param tj 子条件
     * @return 如果包含返回符号。否则为不包含
     */
    private static String checkIsContainsBDSFlg(String tj) {
        if (tj.startsWith(BDS_FLG_DY)) {
            return BDS_FLG_DY;
        } else if (tj.startsWith(BDS_FLG_XY)) {
            return BDS_FLG_XY;
        } else if (tj.contains(BDS_FLG_QJ)) {
            return BDS_FLG_QJ;
        } else {
            return ""; //没有可操作的表达式符号
        }
    }

    /**
     * 检查当前时间是否再某个区间内，
     *
     * @return
     */
    private static boolean checkCurrentTimeIsRang(String rangTimes) {
        try {
            SimpleDateFormat dsdf = new SimpleDateFormat("yyyy-mm-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm");
            long curTimeMs = System.currentTimeMillis();
            //当前的日期(年月日)
            String currentDD = dsdf.format(curTimeMs);
            String[] times = rangTimes.replace(" ", "")
                    .split(BDS_FLG_Splie);
            String logMsg = "[" + rangTimes + "]";
            for (String time : times) {
                if (!time.contains(BDS_FLG_QJ)) {
                    //非区间类型时间处理
                    long targetDataTime = sdf.parse(currentDD + " " + time).getTime();
                    long currDataTime = sdf.parse(sdf.format(curTimeMs)).getTime();
                    if (currDataTime == targetDataTime) {
                        NotifyLog.logBar("时间区间检查通过：" + "子条件：" + time + ",总条件：" + logMsg);
                        return true;
                    }
                    continue;
                }
                //区间范围检查
                String[] ranTimes = time.split(BDS_FLG_QJ);
                if (ranTimes.length != 2) {
                    NotifyLog.logBar("时间区间配置不符合规范，请检查：" + time);
                    continue;
                }
                long currDataTime = sdf.parse(sdf.format(curTimeMs)).getTime();
                long targetStartDataTime = sdf.parse(currentDD + " " + ranTimes[0]).getTime();
                long targetEndDataTime = sdf.parse(currentDD + " " + ranTimes[1]).getTime();
                if (targetStartDataTime > targetEndDataTime) {
                    NotifyLog.logBar("时间区间配置不符合规范，暂不支持跨天时区间：" + time);
                    continue;
                }
                if (currDataTime >= targetStartDataTime && currDataTime <= targetEndDataTime) {
                    NotifyLog.logBar("时间区间检查通过：" + "子条件：" + time + ",总条件：" + logMsg);
                    return true;
                }
            }
            NotifyLog.logBar("时间区间检查没有通过：" + logMsg);
            return false;
        } catch (Exception e) {
            NotifyLog.logBar("检查时间区间出错了。请检查时间配置：e=" + e);
            return false;
        }
    }

    /**
     * 获取UI样式对象的唯一 id
     *
     * @param uiStyle ui样式对象
     * @return
     */
    private static String getUIStyleConfigId(NotifyBarDataConfig.NotifyBarUIDataConfig uiStyle) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append(uiStyle.title);
            sb.append(uiStyle.desc);
            sb.append(uiStyle.rightIcon);
            String id = UUID.nameUUIDFromBytes(sb.toString().getBytes(StandardCharsets.UTF_8)).toString();
            NotifyLog.logBar("生成UI样式唯一id = " + id);
            return id;
        } catch (Exception e) {
            NotifyLog.logBar("生成唯一id出现了错误。使用自动id");
            return "auto_" + new Random().nextInt();
        }
    }

    /**
     * 将指定类型的数据转为整数类型
     *
     * @param str 需要转换的字符串
     * @return
     */
    private static int strToIntNumber(String str) {
        try {
            try {
                return Integer.parseInt(str);
            } catch (Exception intE) {
                return (int) Double.parseDouble(str);
            }
        } catch (Exception e) {
            NotifyLog.logBar("strToIntNumber 转换字符为数字出错,e=" + e);
            return 0;
        }
    }

    /**
     * 更新显示数据需要更新的配置信息
     *
     * @param showItem 当前需要显示的内容
     */
    private static void updateShowUILocalConfig(NotifyBarDataConfig.NotifyBarUIDataConfig showItem) {
        String id = getUIStyleConfigId(showItem);
        //更新显示的总数
        for (NotifyBarDataConfig.NotifyBarItemConfig itemConfig : NotifyBarManager.Ins().getNotifyBarConfigBean().notifyConfig) {
            boolean isFindOk = false;
            for (NotifyBarDataConfig.NotifyBarUIDataConfig notifyUISytle : itemConfig.notifyUISytles) {
                if (getUIStyleConfigId(notifyUISytle).equals(id)) {
                    isFindOk = true;
                    break;
                }
            }
            if (isFindOk) {
                //更新当前分类显示的总次数
                int currentCount = NotifyBarManager.Ins().getCurrentDayShowCount(itemConfig.typeId, true);
                NotifyLog.logBar("自动增加分类[" + itemConfig.typeId + "]总次数："+currentCount);
                break;
            }
        }
    }

}
