package com.dn.drouter;

/**
 * @author by SnowDragon
 * Date on 2020/12/28
 * Description:
 */
public class Stamp {
    private String path;

    public Stamp(String path) {
        this.path = path;
    }

    public Object invoke(Object... parameters) {
        return ARouteObjectInject.invoke(path, parameters);
    }
}
