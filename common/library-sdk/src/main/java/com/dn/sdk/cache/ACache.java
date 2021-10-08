package com.dn.sdk.cache;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;

import com.dn.sdk.bean.AdInfo;
import com.dn.sdk.lib.ad.VideoNative;
import com.dn.sdk.listener.AdVideoListener;
import com.dn.sdk.widget.AdView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author by SnowDragon
 * Date on 2020/11/20
 * Description:
 */
public class ACache {
    private Map<String, AdInfo> adInfoMap = new HashMap<String, AdInfo>();
    private Map<String, AdVideoListener> videoListener = new HashMap<>(1);
    private final String VIDEO_LISTENER_KEY = "com.dn.sdk.listener.AdVideoListener";

    private SparseArray<Activity> topActivityArray = new SparseArray();

    public LinkedList<AdView> customRenderList = new LinkedList<>();


    private static ACache instance;

    private ACache() {
    }

    public static ACache getInstance() {
        if (instance == null) {
            synchronized (ACache.class) {
                if (instance == null) {
                    instance = new ACache();
                }
            }
        }

        return instance;
    }

    public void putNative(String id, AdInfo adInfo) {
        adInfoMap.put(id, adInfo);
    }

    /**
     * 获取缓存中的对象，同时移出缓存
     *
     * @param id 广告位
     * @return
     */
    public AdInfo getAdInfo(String id) {
        AdInfo adInfo = adInfoMap.get(id);
        adInfoMap.remove(id);
        return adInfo;

    }


    /**
     * 保存AdView
     *
     * @param viewList AdView列表
     */
    public void saveCustomRender(List<AdView> viewList) {
        if (viewList != null) {
            for (AdView view : viewList) {
                customRenderList.addLast(view);
            }
        }

    }

    /**
     * 从缓存中取出AdView
     *
     * @return AdView
     */
    public AdView getCustomRenderView() {
        if (customRenderList.size() > 0) {
            return customRenderList.pop();
        }
        return null;
    }

    /**
     * 注册一个激励视频监听器
     *
     * @param listener 监听器
     */
    public void registerRewardVideoListener(AdVideoListener listener) {
        videoListener.put(VIDEO_LISTENER_KEY, listener);
    }

    /**
     * 激励视频监听
     *
     * @return 接口
     */
    public AdVideoListener getRewardVideoListener() {
        return videoListener.remove(VIDEO_LISTENER_KEY);
    }

    public void saveTopActivity(Activity activity) {
        topActivityArray.put(0, activity);
    }


    /**
     * @return 获取顶部Activity
     */
    public Activity getTopActivity() {
        return topActivityArray.get(0, null);
    }


    private Map<String, VideoNative> videoCache = new HashMap<>(1);
    private static final String VIDEO_NATIVE_CACHE_KEY = "com.dn.sdk.lib.ad.VideoNative";

    public void cacheVideoNative(VideoNative videoNative) {
        videoCache.put(VIDEO_NATIVE_CACHE_KEY, videoNative);
    }

    /**
     * 判断激励视频是否有缓存
     */
    public boolean hasVideoNativeCache() {
        return videoCache.containsKey(VIDEO_NATIVE_CACHE_KEY);
    }

    public VideoNative getVideoNative() {
        return videoCache.remove(VIDEO_NATIVE_CACHE_KEY);
    }

    public void cleanInvalidCache() {
        getVideoNative();
    }


    /**
     * 缓存feedTempList
     */
    public LinkedList<View> feedTempList = new LinkedList<>();

    /**
     * 返回信息流模板缓存数量
     *
     * @return 数量
     */
    public int getFeedTempCacheSize() {
        return feedTempList.size();
    }

    public void cacheFeedTemplate(List<View> viewList) {
        if (viewList == null) {
            return;
        }
        for (View view : viewList) {
            feedTempList.addLast(view);
        }
    }

    public View getFeedTemplate() {
        return feedTempList.poll();
    }

}




