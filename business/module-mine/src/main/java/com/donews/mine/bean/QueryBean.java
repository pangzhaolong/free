package com.donews.mine.bean;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/18 14:36<br>
 * 版本：V1.0<br>
 */
public class QueryBean extends BaseCustomViewModel {

    /**
     * icon : http://120.31.70.243/files/xtasks/bi.png
     * name : 可兑换0.03元
     * current_score : 250
     * today_score : 250
     * total_score : 250
     * money : 0.03
     * icon	否	string	图标
     * name	否	string	积分说明，例：可抵3.00
     * current_score	是	int	当前积分，有效积分
     * today_score	是	int	今天领取的积分
     * total_score	是	int	用户累计积分，只增不减
     * money	是	float	当前积分可以兑换人民币数
     */

    private String icon;
    private String name;
    @SerializedName("current_score")
    private int currentScore;
    @SerializedName("today_score")
    private int todayScore;
    @SerializedName("total_score")
    private int totalScore;
    private double money;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getTodayScore() {
        return todayScore;
    }

    public void setTodayScore(int todayScore) {
        this.todayScore = todayScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
