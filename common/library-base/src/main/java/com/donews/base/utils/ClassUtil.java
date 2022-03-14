package com.donews.base.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author by SnowDragon
 * Date on 2020/12/23
 * Description:
 */
public class ClassUtil {

    /**
     * 获取泛型ViewModel的class对象
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> Class<T> getViewModel(Object obj) {


        Class<?> currentClass = obj.getClass();
        Class<T> tClass = getGenericClass(currentClass, ViewModel.class);

        if (tClass == null || tClass == ViewModel.class) {
            return null;
        }
        boolean isAbstract = Modifier.isAbstract(tClass.getModifiers());
        if (isAbstract) {
            return null;
        }

        return tClass;
    }

    private static <T> Class<T> getGenericClass(Class<?> cls, Class<?> filterClass) {

        ParameterizedType parameterizedType = getParameterizedType(cls);
        Type[] types = parameterizedType.getActualTypeArguments();
        try {
            for (Type t : types) {
                Class<T> tClass = (Class<T>) t;
                if (filterClass.isAssignableFrom(tClass)) {
                    return tClass;
                }
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 返回一个参数化类型的对象：泛型类型对象
     *
     * @param object 实体类
     * @param i      第几个参数
     * @param <T>    T
     * @return 对象
     */
    public static <T> T getInstance(Object object, int i) {
        ParameterizedType parameterizedType = getParameterizedType(object.getClass());
        if (parameterizedType == null) {
            return null;
        }
        try {
            Class<T> tClass = (Class<T>) parameterizedType.getActualTypeArguments()[i];
            return tClass.newInstance();

        } catch (InstantiationException | ClassCastException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static ParameterizedType getParameterizedType(Class<?> cls) {

        Type type = cls.getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        return parameterizedType;

    }


    public static @NonNull
    <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }


}
