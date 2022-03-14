package com.dn.drouter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author by SnowDragon
 * Date on 2020/12/28
 * Description: 用于标注方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DNMethodRoute {

    /**
     * 方法路径
     *
     * @return String
     */
    String value() default "";
}
