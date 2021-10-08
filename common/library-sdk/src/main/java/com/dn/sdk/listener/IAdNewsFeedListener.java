package com.dn.sdk.listener;

import com.dn.sdk.widget.AdView;

import java.util.List;

/**
 * @author by SnowDragon
 * Date on 2020/11/23
 * Description:
 */
public interface IAdNewsFeedListener {
    void success(List<AdView> viewList);

    void onError(String errorMsg);
}
