package com.donews.alive.api;

/**
 * @author by SnowDragon
 * Date on 2020/12/8
 * Description:
 */
public class BaseResponse<T> {
    public T data;
    public int code;
    public String msg;
}
