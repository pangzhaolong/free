package com.donews.home.bean;

import com.donews.common.contract.BaseCustomViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> 所有bean文件集成BaseCustomViewModel，为后面的混淆做准备</p>
 * 作者： created by honeylife<br>
 * 日期： 2021/7/21 14:59<br>
 * 版本：V1.0<br>
 */
public class TopBannerBean extends BaseCustomViewModel {
    int code;
    String msg;
    /*DataBean data;

    public DataBean getData() {
        return data;
    }*/

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setCode(int code) {
        this.code = code;
    }
/*
    public void setData(DataBean data) {
        this.data = data;
    }*/

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TopBannerBean{" +
                ", code=" + code +
                ", msg='" + msg + '\'' +
//                ", data=" + data +
                '}';
    }

}
