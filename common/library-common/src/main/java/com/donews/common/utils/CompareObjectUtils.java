package com.donews.common.utils;


import java.lang.reflect.Field;

/**
 * @author by SnowDragon
 * Date on 2021/4/9
 * Description:
 * 比较两个类的值是否相等，注意，只针对public字段，私有字段无效
 */
public class CompareObjectUtils {

    public static <T> boolean contrastObj(T original, T current, Class<T> cls) {
        if (original == null && current != null) {
            return false;
        }

        if (current == null && original != null) {
            return false;
        }

        try {
            Field[] fields = cls.getDeclaredFields();

            for (Field field : fields) {
                Object o1 = field.get(original);
                Object o2 = field.get(current);
                if (o1 == null && o2 == null) {
                    continue;
                }
                if (o1 != null) {
                    if (!o1.equals(o2)) {
                        return false;
                    } else {
                        //o1==o2 继续执行
                    }

                } else {
                    return false;
                }

            }
        } catch (Exception e) {
            return false;
        }

        return true;

    }

}