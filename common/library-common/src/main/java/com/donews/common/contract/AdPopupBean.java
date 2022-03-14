package com.donews.common.contract;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/10 15:59<br>
 * 版本：V1.0<br>
 */
public class AdPopupBean {
    private DrawedBean drawed;
    private DrawedBean drawing;
    private boolean gold;
    private boolean page;
    private boolean video;
    private boolean adClick;
    private int adClickPercent;
    private int closePlayRewardVideoPercent;
    private int integralAdClickPercent;

    public DrawedBean getDrawed() {
        return drawed;
    }

    public void setDrawed(DrawedBean drawed) {
        this.drawed = drawed;
    }

    public DrawedBean getDrawing() {
        return drawing;
    }

    public void setDrawing(DrawedBean drawing) {
        this.drawing = drawing;
    }

    public boolean isGold() {
        return gold;
    }

    public void setGold(boolean gold) {
        this.gold = gold;
    }

    public boolean isPage() {
        return page;
    }

    public void setPage(boolean page) {
        this.page = page;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public boolean isAdClick() {
        return adClick;
    }

    public void setAdClick(boolean adClick) {
        this.adClick = adClick;
    }

    public int getAdClickPercent() {
        return adClickPercent;
    }

    public void setAdClickPercent(int adClickPercent) {
        this.adClickPercent = adClickPercent;
    }

    public int getClosePlayRewardVideoPercent() {
        return closePlayRewardVideoPercent;
    }

    public void setClosePlayRewardVideoPercent(int closePlayRewardVideoPercent) {
        this.closePlayRewardVideoPercent = closePlayRewardVideoPercent;
    }

    public int getIntegralAdClickPercent() {
        return integralAdClickPercent;
    }

    public void setIntegralAdClickPercent(int integralAdClickPercent) {
        this.integralAdClickPercent = integralAdClickPercent;
    }
}
