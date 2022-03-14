package com.dn.events.events;

public class WalletRefreshEvent {
    /**
     * 0:红包引起
     * 1:提现引起
     */
    public int navIndex = 0;

    public WalletRefreshEvent(int navIndex) {
        this.navIndex = navIndex;
    }
}
