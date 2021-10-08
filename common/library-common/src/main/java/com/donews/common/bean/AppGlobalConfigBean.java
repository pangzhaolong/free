package com.donews.common.bean;

import com.donews.common.contract.BaseCustomViewModel;

/**
 * @author by SnowDragon
 * Date on 2021/4/6
 * Description:
 */
public class AppGlobalConfigBean extends BaseCustomViewModel {
    public boolean crashIsOpen = false;
    public boolean crashLifecycleHandler = false;

    public int refreshInterval = 10;

}
