package com.dn.drouter.processor;


import com.dn.drouter.annotation.DNMethodRoute;
import com.dn.drouter.entity.RouteBean;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author by SnowDragon
 * Date on 2020/12/28
 * Description:
 */
public class DNMethodRouteProcessor {

    /**
     * @param objMap   对象路径关系映射
     * @param routeMap 路径方法关系映射
     * @param obj      绑定对象
     */
    public void annotationProcess(HashMap<Integer, ArrayList<String>> objMap,
                                  HashMap<String, RouteBean> routeMap,
                                  Object obj) {
        Class<?> cls = obj.getClass();
        Method[] methods = cls.getMethods();

        //方法路径列表
        ArrayList<String> methodPathList = objMap.get(obj.hashCode());
        if (methodPathList == null) {
            methodPathList = new ArrayList<>();
        }
        for (Method method : methods) {

            DNMethodRoute annotation = method.getAnnotation(DNMethodRoute.class);
            if (annotation == null) {
                continue;
            }
            routeMap.put(annotation.value(), new RouteBean(obj, method, method.getParameterTypes()));
            methodPathList.add(annotation.value());

        }
        objMap.put(obj.hashCode(), methodPathList);


    }

}


