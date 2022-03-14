package com.dn.drouter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author by SnowDragon
 * Date on 2020/12/14
 * Description:
 */
class ARouterAssist {
    private static Map<String, Object> map = new HashMap<>();

    /**
     * 绑定对象
     *
     * @param path 路径
     * @param obj  对象
     */
    public static void bind(String path, Object obj) {
        ArouterLogger.i(ArouterLogger.TAG, "bind path: " + path);
        ArouterLogger.i(ArouterLogger.TAG, "bind Object: " + obj);
        map.put(path, obj);
    }

    /**
     * 解绑对象
     *
     * @param path 路径
     */
    public static void unBind(String path) {
        map.remove(path);
    }


    /**
     * 调用指定路径对应的对象的方法
     *
     * @param path       路径
     * @param methodName 方法名
     * @param params     参数
     */
    public static <T> T invoke(String path, String methodName, Object... params) {
        Object obj = map.get(path);
        if (obj != null) {
            try {
                Method method = getMethod(obj.getClass(), methodName, params == null ? 0 : params.length);

                if (method != null) {
                    T t = (T) method.invoke(obj, params);
                    return t;
                } else {
                    throw new NoSuchMethodException(obj.getClass() + "." + methodName + " :Please check the method name and parameters ");
                }

            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return null;

    }

    /**
     * 获取方法
     * 注意这里只做了方法名相同，参数长度相同，未做参数类型判断，所以不支持方法重载
     *
     * @param cls         class
     * @param methodName  方法名
     * @param paramLength 参数长度
     * @return
     */
    private static Method getMethod(final Class cls, String methodName, int paramLength) {
        Method[] methods = cls.getMethods();

        for (Method method : methods) {
            if (method.getName().endsWith(methodName) && method.getParameterTypes().length == paramLength) {
                return method;
            }
        }
        return null;
    }


}
