package com.dn.events.events;

public class LotteryStatusEvent {
    public int position = 0;
    public String goodsId = "";
    public Object object;

    public LotteryStatusEvent(int position, String goodsId, Object object) {
        this.position = position;
        this.goodsId = goodsId;
        this.object = object;
    }
}
