package com.donews.common.contract;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/12/9 11:49<br>
 * 版本：V1.0<br>
 */
public class ReceiveDialogModel {
    // id
    private int id;
    // action
    private int action;
    // 奖励
    private int reward;
    // 奖励类型
    private String rewardType;
    // 总的奖励值
    private int rewardTotal;
    // 兑换的money
    private double money;

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public int getRewardTotal() {
        return rewardTotal;
    }

    public void setRewardTotal(int rewardTotal) {
        this.rewardTotal = rewardTotal;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
