package com.donews.crashhandler.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 使用JVM无法识别调用方式，规避调用反射调用隐藏API被检测问题，原理见
 * https://androidreverse.wordpress.com/2020/05/02/android-api-restriction-bypass-for-all-android-versions/
 *
 * @author Swei
 * @date 2021/4/6 19:09
 * @since v1.0
 */
class ReflectionNative {
    private ReflectionNative(){}
    public static native Method getDeclaredMethod(Object recv, String name, Class<?>[] parameterTypes);
    public static native Method getMethod(Object recv, String name, Class<?>[] parameterTypes);
    public static native Field getDeclaredField(Object recv, String name);

    static{
        System.loadLibrary("safe-reflection");
    }

}
