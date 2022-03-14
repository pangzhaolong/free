package com.donews.mine.bean;

import com.donews.common.contract.BaseCustomViewModel;

import org.jetbrains.annotations.NotNull;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/2 15:24<br>
 * 版本：V1.0<br>
 */
public class CodeBean extends BaseCustomViewModel {

    /**
     * code : string
     */

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @NotNull
    @Override
    public String toString() {
        return "{" +
                "\"invite_code\":\"" + code + '\"' +
                '}';
    }
}
