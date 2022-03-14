package com.donews.common.contract;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/9 16:03<br>
 * 版本：V1.0<br>
 */
public class DrawedBean {
    /**
     * display : true
     * type : 0
     */

    public final static int SELF_TEMP = 0;
    public final static int TEMP = 1;

    private boolean display;
    private int type;
    private int skipTime;

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSkipTime() {
        return skipTime;
    }

    public void setSkipTime(int skipTime) {
        this.skipTime = skipTime;
    }
}
