package com.donews.middle.bean.home;

import java.util.ArrayList;
import java.util.List;

public class TmpSearchHistory {

    public static TmpSearchHistory Ins() {
        return Instance.searchHistory;
    }

    private static class Instance {
        public static TmpSearchHistory searchHistory = new TmpSearchHistory();
    }

    private final List<String> mHistoryList = new ArrayList<>();

    public List<String> getList() {
        return mHistoryList;
    }

    public void clear() {
        mHistoryList.clear();
    }

}
