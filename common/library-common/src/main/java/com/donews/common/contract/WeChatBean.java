package com.donews.common.contract;

import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <p>  微信登录返回的数据类型 </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/11 17:47<br>
 * 版本：V1.0<br>
 */
public class WeChatBean extends BaseCustomViewModel {


    /**
     * access_token : string
     * expires_in : string
     * refresh_token : string
     * open_id : string
     * scope : string
     * nick_name : string
     * sex : 0
     * province : string
     * city : string
     * country : string
     * headimgurl : string
     * privilege : ["string"]
     * unionid : string
     */

    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("expires_in")
    private String expiresIn;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("open_id")
    private String openId;
    private String scope;
    @SerializedName("nick_name")
    private String nickName;
    private int sex;
    private String province;
    private String city;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<String> privilege;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Bindable
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
        notifyPropertyChanged(BR.openId);
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<String> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<String> privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "WeChatBean{" +
                "accessToken='" + accessToken + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", openId='" + openId + '\'' +
                ", scope='" + scope + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", unionid='" + unionid + '\'' +
                ", privilege=" + privilege +
                '}';
    }
}
