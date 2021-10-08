package com.donews.crashhandler.reflect;

import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 通过反射的方式获取系统反射类，规避系统限制api
 *
 * @author Swei
 * @date 2021/4/6 18:58
 * @since v1.0
 */
public class Reflection {
    private Reflection(){}

    public static Method getDeclaredMethod(Object clazz, String name, Class<?>... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ReflectionNative.getDeclaredMethod(clazz, name, args);
        } else {
            Method getDeclaredMethod = Class.class.getMethod("getDeclaredMethod",
                    String.class, Class[].class
            );
            return (Method) getDeclaredMethod.invoke(clazz, name, args);
        }
    }

    public static Method getMethod(Object clazz, String name, Class<?>... args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ReflectionNative.getMethod(clazz, name, args);
        } else {
            Method getDeclaredMethod = Class.class.getMethod("getMethod",
                    String.class, Class[].class
            );
            return (Method) getDeclaredMethod.invoke(clazz, name, args);
        }
    }

    public static Field getDeclaredField(Class obj, String name) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ReflectionNative.getDeclaredField(obj, name);
        } else {
            Method getDeclaredField = Class.class.getMethod("getDeclaredField", String.class);
            return (Field) getDeclaredField.invoke(obj, name);
        }
    }


}
