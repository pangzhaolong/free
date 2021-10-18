package com.donews.home.bean;

import androidx.annotation.NonNull;

import com.donews.utilslibrary.utils.SPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchHistory {

    public static SearchHistory Ins() {
        return Instance.searchHistory;
    }

    private static class Instance {
        public static SearchHistory searchHistory = new SearchHistory();
    }


    private final List<String> mHistoryList = new ArrayList<>();

    private SearchHistory() {
        read();
    }

    public List<String> getList() {
        return mHistoryList;
    }

    public void addHistory(String history) {
        if (mHistoryList.size() >= 12) {
            mHistoryList.remove(0);
        }
        mHistoryList.add(history);
    }

    public void clear() {
        mHistoryList.clear();
    }

    public void read() {
        mHistoryList.clear();
        String strHistory = SPUtils.getInformain("jdd_search_history", "");
        if (strHistory.equalsIgnoreCase("")) {
            return;
        }
        String[] history = strHistory.split(";");
        mHistoryList.addAll(Arrays.asList(history));
    }

    public void write(String history) {
        SPUtils.setInformain("jdd_search_history", history);
    }

    @NonNull
    public String toString() {
        StringBuilder strHistory = new StringBuilder();
        for (String s : mHistoryList) {
            strHistory.append(s).append(";");
        }
        return strHistory.toString();
    }
}
