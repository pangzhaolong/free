package com.donews.notify.launcher.utils.conditions;

import com.donews.notify.launcher.configs.baens.Notify2DataConfigBean;
import com.donews.notify.launcher.utils.NotifyLog;
import com.tencent.mmkv.MMKV;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lcl
 * Date on 2022/1/20
 * Description:
 * 复杂条件处理工具类，专门处理条件相关的业务逻辑
 */
public class ConditionProcessUtil {
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
     * 执行条件。注意：条件和相关条件锚定必须一一对应。否则可能造成结果不准确
     *
     * @param notifyConfig 当前分类配置内容
     * @return
     */
    public static boolean invokCondition(Notify2DataConfigBean.NotifyItemConfig notifyConfig) {
        List<Notify2DataConfigBean.JudgeConditionItem> conditions = notifyConfig.judgeConditions;
        List<Notify2DataConfigBean.ConditionalProcessItem> invokConditions = notifyConfig.conditionalProcess;
        if (conditions == null || conditions.isEmpty()) {
            return true; //没有条件。默认全部满足
        }
        if (invokConditions == null || invokConditions.isEmpty()) {
            return true; //每个条件都没有锚定值获取方法。直接返回全部满足
        }
//        (notifyConfig.dayReceiveRedCount < 0 ||
//                openRedPackCount == notifyConfig.dayReceiveRedCount) &&
//                (notifyConfig.dayLotteryCodeCount < 0 ||
//                        getLotteryCodeCount == notifyConfig.dayLotteryCodeCount);
        //条件的结果集合(条件处理完成的结果)
        List<Boolean> conditionResults = new ArrayList<>();
        //各种条件之间的连接关系(只支持：& ||),默认：&
        List<String> conditionResultRelationship = new ArrayList<>();

        //解析条件
        for (int i = 0; i < conditions.size(); i++) {
            Notify2DataConfigBean.JudgeConditionItem conditionItem = conditions.get(i);
            //处理条件连接符号
            if (conditionItem.condition.contains(Logic_And)) {
                conditionResultRelationship.add(Logic_And);
            } else {
                //没有and连接符
                if (conditionItem.condition.contains(Logic_Or)) {
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
    }

    /**
     * 解析和处理单个条件是否满足
     *
     * @param conditionItem 条件表达式
     * @param invokItem     该条件的锚定方法
     * @return
     */
    private static boolean runSingCondition(
            Notify2DataConfigBean.JudgeConditionItem conditionItem,
            Notify2DataConfigBean.ConditionalProcessItem invokItem) {
        if (invokItem.executioMethod == null || invokItem.executioMethod.isEmpty()) {
            return true;//锚定错误或者没有锚定
        }
        Class<?> invokClazz = null;
        Method invokMethod = null;
        try {
            if (conditionItem.condition.length() < 2) {
                NotifyLog.log("中台条件条件表达式不符合规范,请检查(-1)");
                return false; //条件不满足表达式要求
            }
            if (!conditionItem.condition.startsWith(BDS_EDN)) {
                NotifyLog.log("中台条件条件表达式不符合规范,请检查(-2)");
                return false; //条件不满足表达式要求
            }
            if (!conditionItem.condition.endsWith(BDS_EDN)) {
                if (conditionItem.condition.lastIndexOf(BDS_EDN) != conditionItem.condition.length() - 1) {
                    NotifyLog.log("中台条件条件表达式不符合规范,请检查(-3)");
                    return false;//条件不满足表达式要求
                }
            }
            //去掉括号之后的单纯表达式内容(括号内的表达式内容)
            String bdsConent = conditionItem.condition.substring(
                    conditionItem.condition.indexOf(BDS_START),
                    conditionItem.condition.lastIndexOf(BDS_EDN)
            );
            if (!invokItem.executioMethod.contains(Method_Splie)) {
                NotifyLog.log("中台条件锚定配置不符合规范,请检查(1)");
                return true;
            }
            String[] cmLs = invokItem.executioMethod.split(Method_Splie);
            if (cmLs.length != 2) {
                NotifyLog.log("中台条件锚定配置不符合规范,请检查(2)");
                return true;
            }
            invokClazz = Class.forName(cmLs[0]);
            try {
                invokMethod = invokClazz.getMethod(cmLs[1]);
            } catch (Exception e) {
                try {
                    invokMethod = invokClazz.getDeclaredMethod(cmLs[1]);
                } catch (Exception de) {
                    NotifyLog.log("中台条件锚定配置不符合规范,请检查,e=" + de);
                    return true;
                }
            }
            if (Integer.TYPE != invokMethod.getReturnType()) {
                NotifyLog.log("所配置的锚定方法返回类型不正确，只允许返回(int)类型。请检查(1)");
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
                    NotifyLog.log("配置锚定方法为非静态方法。请提供默认构造函数,请检查：" + invokClazz.getName());
                }
            }
            //锚定方法返回的实际锚定值
            int anchorNumber = -1;
            try {
                anchorNumber = (int) invokMethod.invoke(invokObj);
            } catch (Exception e) {
                NotifyLog.log("执行锚定方法出现异常,但不中断业务，e=" + e);
            }
            //开始处理动态逻辑
            //去掉括号。以及逻辑关系符号之后的字符串
            String[] conContentArr = bdsConent.split(BDS_FLG_Splie);
            //内部多个小条件的集合
            for (int i = 0; i < conContentArr.length; i++) {
                String checkTjFlg = checkIsContainsBDSFlg(conContentArr[i]);
                if (checkTjFlg.isEmpty()) {
                    //没有表达式,直接转换为数字,没有符号表示直接使用 '=' 即可
                    invokBdsFlg(anchorNumber,strToIntNumber(conContentArr[i]),"=");
                }else{
                    if(checkTjFlg.equals(BDS_FLG_QJ)){
                        //是一个集合。
                    }else{
                        // 正常的表达式处理
                    }
                }
            }
        } catch (Exception e) {
            NotifyLog.log("中台条件锚定配置错误。请检查配置,e=" + e);
            return true;//配置错误。直接当没有处理
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
            NotifyLog.log("strToIntNumber 转换字符为数字出错,e=" + e);
            return 0;
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
        } else if (tj.startsWith(BDS_FLG_QJ)) {
            return BDS_FLG_QJ;
        } else {
            return ""; //没有可操作的表达式符号
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

}
