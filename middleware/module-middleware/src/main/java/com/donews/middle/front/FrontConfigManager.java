package com.donews.middle.front;

public class FrontConfigManager {
    private static final class Holder {
        private static final FrontConfigManager s_frontConfigMgr = new FrontConfigManager();
    }

    public FrontConfigManager Ins() {
        return Holder.s_frontConfigMgr;
    }

    private FrontConfigManager() {
    }
}
