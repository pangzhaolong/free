package com.dn.drouter;

import android.util.Log;

import com.dn.drouter.entity.RouteBean;
import com.dn.drouter.processor.DNMethodRouteProcessor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/12/31
 * Description:
 */
public class ARouteObjectInject {
    private static final String TAG = "ARouterAssist";

    /**
     * 对象hashCode 映射方法路径列表
     */
    static HashMap<Integer, ArrayList<String>> objectMap = new HashMap<>();
    /**
     * route路径映射方法
     */
    static HashMap<String, RouteBean> routeMap = new HashMap<>();

    /**
     * 绑定对象
     *
     * @param obj Object
     */
    public static void bind(Object obj) {
        new DNMethodRouteProcessor().annotationProcess(objectMap, routeMap, obj);
    }


    /**
     * 解绑
     *
     * @param obj Object
     */
    public static void unBind(Object obj) {
        if (obj == null) {
            return;
        }
        List<String> methodPathList = objectMap.remove(obj.hashCode());
        if (methodPathList == null) {
            return;
        }
        for (String path : methodPathList) {
            routeMap.remove(path);
        }
    }

    /**
     * 方法调用
     *
     * @param path       路径
     * @param parameters 参数
     * @return Object
     */
    public static Object invoke(String path, Object... parameters) {
        RouteBean routeBean = routeMap.get(path);

        if (routeBean == null) {
            //日志输出
            Log.e(TAG, "No matching method was found");
            return null;
        }
        ArouterLogger.i(ArouterLogger.TAG, "invoke path: " + path + " method " + routeBean.method);
        try {
            return routeBean.method.invoke(routeBean.obj, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            ArouterLogger.e(ArouterLogger.TAG, e);
            e.printStackTrace();

        }
        return null;
    }
}
