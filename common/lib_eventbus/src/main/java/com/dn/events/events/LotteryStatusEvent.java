package com.dn.events.events;

public class LotteryStatusEvent {
    public int position = 0;
    public String goodsId = "";
    public int lotteryStatus;

    public LotteryStatusEvent(int position, String goodsId, int lotteryStatus) {
        this.position = position;
        this.goodsId = goodsId;
        this.lotteryStatus = lotteryStatus;
    }
}
