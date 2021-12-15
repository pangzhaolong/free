package com.donews.common.bean;

import com.donews.common.contract.BaseCustomViewModel;

/**
 * 暴击使者
 * 该类这要是抽奖页面通过EventBus 发送的暴击消息
 * <p>
 * -1 模式参数。暴击模式未开启
 * 200 表示满足了暴击时刻，可以开始暴击模式了
 */

public class CritMessengerBean extends BaseCustomViewModel {

    public int mStatus = -1;
    public CritMessengerBean(int status) {
        this.mStatus = status;
    }

}
