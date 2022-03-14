package com.dn.drouter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.alibaba.android.arouter.core.LogisticsCenter;
import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

import java.lang.reflect.Method;

/**
 * @author by SnowDragon
 * Date on 2020/11/16
 * Description:
 */
public class ARouteHelper {

    /**
     * 初始化Arouter
     *
     * @param context context
     */
    public static void init(Context context) {
        ARouter.init((Application) context);
    }

    /**
     * 获取fragment
     * @param routePath 路径
     * @return 获取Fragment
     */
//    public static Fragment getFragment(String routePath) {
//        return (Fragment) ARouter.getInstance().build(routePath).navigation();
//    }

    /**
     * @param routePath 路径
     */
    public static void routeSkip(String routePath) {
        routeSkip(routePath, null);
    }

    /**
     * @param routePath 路径
     * @param bundle    参数类
     */

    public static void routeSkip(String routePath, Bundle bundle) {
        ARouter.getInstance().build(routePath).with(bundle).navigation();
    }

    /**
     * 在Activity的 @onActivityResult方法中做处理操作
     *
     * @param routePath   路径
     * @param bundle      参数
     * @param requestCode requestCode
     */
    public static void routeSkipForResult(Activity activity, String routePath, Bundle bundle, int requestCode) {
        ARouter.getInstance().build(routePath)
                .with(bundle)
                .navigation(activity, requestCode);
    }

    /**
     * 调用指定service的 @methodName 无参方法
     *
     * @param path       service路径
     * @param methodName 方法名
     */
    public static void routeAccessService(String path, String methodName) {
        routeAccessService(path, methodName, null);
    }

    /**
     * @param path       service路径
     * @param methodName 方法名
     * @param params     参数
     */
    public static void routeAccessService(String path, String methodName, Object[] params) {
        routeAccessServiceForResult(path, methodName, params);
    }

    /**
     * @param path       route 路径
     * @param methodName 方法名
     * @param params     方法参数 注意方法参数目前只支持 基本类型和String类型
     * @param <T>        返回类型
     * @return 返回结果
     */
    public static <T> T routeAccessServiceForResult(String path, String methodName, Object[] params) {
        Postcard postcard = getPostCard(path);
        ArouterLogger.i(ArouterLogger.TAG, "invoke path: " + path);
        ArouterLogger.i(ArouterLogger.TAG, "invoke methodName: " + methodName);
        Object obj = postcard.getProvider();
        Class<?> cls = postcard.getDestination();
        ArouterLogger.i(ArouterLogger.TAG, "invoke obj: " + obj+" cls: "+cls);
        try {
            Method method = getMethod(cls, methodName, params == null ? 0 : params.length);
            ArouterLogger.i(ArouterLogger.TAG, "invoke method: " + method);
            if (method != null) {
                T t = (T) method.invoke(obj, params);
                return t;
            } else {
                ArouterLogger.i(ArouterLogger.TAG, obj.getClass() + "." + methodName);
                ArouterLogger.i(ArouterLogger.TAG, "Please check the method name and parameters");
                throw new NoSuchMethodException(obj.getClass() + "." + methodName + " :Please check the method name and parameters ");
            }

        } catch (Exception e) {
            ArouterLogger.e(ArouterLogger.TAG, e);
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取Postcard
     *
     * @param path route路径
     * @return PostCard
     */
    private static Postcard getPostCard(String path) {
        Postcard postcard = ARouter.getInstance().build(path);
        LogisticsCenter.completion(postcard);
        return postcard;
    }

    /**
     * @param cls          class
     * @param methodName   方法名
     * @param paramsLength 参数长度
     * @return
     */
    private static Method getMethod(final Class cls, String methodName, int paramsLength) {
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.getParameterTypes().length == paramsLength) {
                return method;
            }
        }
        return null;
    }

    /**
     * 注意这里都是基本类型 int，而不是Integer，接受的方法里面，对于int
     *
     * @param params 基本类型、String类型的参数
     * @return 返回所以参数对应的 Class数组
     */
    @Deprecated
    private static Class<?>[] getClassParameterTypes(Object... params) {
        if (params == null || params.length == 0) {
            return null;
        }
        Class<?>[] parameterTypes = new Class<?>[params.length];
        for (int i = 0; i < params.length; i++) {

            if (params[i] instanceof Integer) {
                parameterTypes[i] = int.class;
            } else if (params[i] instanceof Long) {
                parameterTypes[i] = long.class;
            } else if (params[i] instanceof Float) {
                parameterTypes[i] = float.class;
            } else if (params[i] instanceof Double) {
                parameterTypes[i] = double.class;
            } else if (params[i] instanceof Boolean) {
                parameterTypes[i] = boolean.class;
            } else if (params[i] instanceof String) {
                parameterTypes[i] = String.class;
            } else if (params[i] instanceof Activity) {
                parameterTypes[i] = Activity.class;
            }
        }

        return parameterTypes;

    }


    /**
     * 使用绑定对象，使用注解声明方法
     * {@link com.dn.drouter.ARouteHelper#bind(Object)}
     * 绑定对象
     *
     * @param path 路径
     * @param obj  对象
     */
    @Deprecated
    public static void bind(String path, Object obj) {
        ARouterAssist.bind(path, obj);
    }

    /**
     * 绑定对象
     * {@link com.dn.drouter.ARouteHelper#unBind(Object)}
     *
     * @param path 路径
     */
    @Deprecated
    public static void unBind(String path) {
        ARouterAssist.unBind(path);
    }


    /**
     * 注意：由于没有做相同方法名下参数不同判断，暂不支持方法重载
     * 调用指定路径对应对象的方法
     *
     * @param path 路径
     */
    @Deprecated
    public static <T> T invoke(String path, String methodName, Object... params) {
        return ARouterAssist.invoke(path, methodName, params);
    }


/************************************************************************/
    /**
     * 绑定对象
     *
     * @param obj 对象
     */
    public static void bind(Object obj) {
        ARouteObjectInject.bind(obj);
    }

    /**
     * 绑定RouterProvider
     *
     * @param path 路径
     */
    public static void bindRouteProvider(String path) {
        Postcard postcard = getPostCard(path);
        bind(postcard.getProvider());
    }

    /**
     * 接触绑定
     *
     * @param obj Object
     */
    public static void unBind(Object obj) {
        ARouteObjectInject.unBind(obj);
    }

    public static Stamp build(String path) {
        return new Stamp(path);
    }


}
