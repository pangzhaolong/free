package com.donews.web.manager;

/**
 * @Author: honeylife
 * @CreateDate: 2020/6/1 10:11
 * @Description:
 */
public class WebModel {
    // 这种情况处理h5页面咋用一个页面时的放回 true 表示要返回，false 不返回
    private boolean isBackH5;
    private int mActionId;
    // true 表示视频加载没有加载，false表示加载，不可点击
    private boolean isVideo = true;
    // true 表示页面可见，加载视频
    private boolean isResume = false;

    // true 表示可摇晃，false 表示不可摇晃
    private boolean ismShake = true;
    //webview页面需要调分享成功的回调
    private int  mOpenType ;


    public int getmOpenType() {
        return mOpenType;
    }

    public void setmOpenType(int mOpenType) {
        this.mOpenType = mOpenType;
    }

    public boolean isBackH5() {
        return isBackH5;
    }

    public void setBackH5(boolean backH5) {
        isBackH5 = backH5;
    }

    public int getmActionId() {
        return mActionId;
    }

    public void setmActionId(int mActionId) {
        this.mActionId = mActionId;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public boolean isResume() {
        return isResume;
    }

    public void setResume(boolean resume) {
        isResume = resume;
    }

    public boolean isIsmShake() {
        return ismShake;
    }

    public void setIsmShake(boolean ismShake) {
        this.ismShake = ismShake;
    }
}
