package com.donews.common.contract;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;


/**
 * @auther ming
 * @date$
 */
public class ApplyUpdateBean extends BaseCustomViewModel {

    /**
     * package_name : com.walk.fqzl
     * channel : toutiao3
     * apk_url : http://ad-static-xg.tagtic.cn/mtasks/api/apk/pack?version_code=30006&package_name=com.walk.fqzl
     * version_code : 30006
     * force_upgrade : 0
     * upgrade_info :
     */

    private String package_name;
    private String channel;
    private String apk_url;
    private int version_code;
    private int force_upgrade;
    private String upgrade_info;
    private int progress;

    @Bindable
    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
        notifyPropertyChanged(BR.package_name);
    }

    @Bindable
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
        notifyPropertyChanged(BR.channel);
    }

    @Bindable
    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
        notifyPropertyChanged(BR.apk_url);
    }

    @Bindable
    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
        notifyPropertyChanged(BR.version_code);
    }

    @Bindable
    public int getForce_upgrade() {
        return force_upgrade;
    }

    public void setForce_upgrade(int force_upgrade) {
        this.force_upgrade = force_upgrade;
        notifyPropertyChanged(BR.force_upgrade);
    }

    @Bindable
    public String getUpgrade_info() {
        return upgrade_info;
    }

    public void setUpgrade_info(String upgrade_info) {
        this.upgrade_info = upgrade_info;
        notifyPropertyChanged(BR.upgrade_info);
    }
      @Bindable
    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        notifyPropertyChanged(BR.progress);
    }

    @Override
    public String toString() {
        return "ApplyUpdataBean{" +
                "package_name='" + package_name + '\'' +
                ", channel='" + channel + '\'' +
                ", apk_url='" + apk_url + '\'' +
                ", version_code=" + version_code +
                ", force_upgrade=" + force_upgrade +
                ", upgrade_info='" + upgrade_info + '\'' +
                '}';
    }
}
