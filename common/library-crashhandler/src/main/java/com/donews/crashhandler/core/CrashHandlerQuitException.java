package com.donews.crashhandler.core;

/**
 * 退出crash异常
 *
 * @author Swei
 * @date 2021/4/9 16:24
 * @since v1.0
 */
public class CrashHandlerQuitException extends RuntimeException{
    public CrashHandlerQuitException(String message) {
        super(message);
    }
}
