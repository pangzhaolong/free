package com.donews.crashhandler.reflect;

import java.lang.reflect.Method;

/**
 * 突破Android 9-11对反射系统隐藏api的限制
 * http://weishu.me/2019/03/16/another-free-reflection-above-android-p/
 * https://androidreverse.wordpress.com/2020/05/02/android-api-restriction-bypass-for-all-android-versions/
 * @author Swei
 * @date 2021/4/6 18:46
 * @since v1.0
 */
public final class ReflectionLimit {
    /** 确保只调用一次 */
    private static boolean isLimit = false;
    private ReflectionLimit(){}

    /**
     *             try {
     *                 Method forName = Class.class.getDeclaredMethod("forName", String.class);
     *                 Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
     *
     *                 Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
     *                 Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
     *                 setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
     *                 sVmRuntime = getRuntime.invoke(null);
     *             } catch (Throwable e) {
     *                 Log.w(TAG, "reflect bootstrap failed:", e);
     *             }
     *
     *             反射方法，使用ndk中 JavaVM :: attackCurrentThread 来调用，规避系统的检测
     */

    public static boolean clearLimit(){
        if(isLimit){
            return isLimit;
        }
        try{
            Method getRuntime = Reflection.getDeclaredMethod(Class.forName("dalvik.system.VMRuntime"),"getRuntime");
            getRuntime.setAccessible(true);
            Object vmRuntime = getRuntime.invoke(null);

            Method setHiddenApiExemptions = Reflection.getDeclaredMethod(vmRuntime.getClass(),"setHiddenApiExemptions",String[].class);
            setHiddenApiExemptions.setAccessible(true);

            //所有java方法签名都是L开头，因此所有方法都不会被限制
            String[] list = new String[1];
            list[0] = "L";
            Object[] args = new Object[1];
            args[0] = list;
            setHiddenApiExemptions.invoke(vmRuntime, args);
            isLimit = true;
            return isLimit;
        }catch (Throwable t){
            t.printStackTrace();
        }
        return false;
    }


}
