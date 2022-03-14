package com.dn.drouter.entity;

import java.lang.reflect.Method;

public class RouteBean {

    public Object obj;
    public Method method;
    public Class<?>[] paramsTypes;
    public String methodName;
    public Object[] params;


    public RouteBean(Object obj, Method method, Class<?>[] paramsTypes) {
        this.obj = obj;
        this.method = method;
        this.paramsTypes = paramsTypes;
    }
}
