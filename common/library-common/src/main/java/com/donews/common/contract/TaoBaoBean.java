package com.donews.common.contract;

import com.google.gson.annotations.SerializedName;

/**
 * <p> 淘宝登录之后的返回的数据模型</p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/11 17:49<br>
 * 版本：V1.0<br>
 */
public class TaoBaoBean {

    /**
     * user_id : string
     * open_sid : string
     * top_access_token : string
     * avatar_url : string
     * havana_sso_token : string
     * nick : string
     * open_id : string
     * top_auth_code : string
     * top_expire_time : string
     */

    @SerializedName("user_id")
    private String userId;
    @SerializedName("open_sid")
    private String openSid;
    @SerializedName("top_access_token")
    private String topAccessToken;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("havana_sso_token")
    private String havanaSsoToken;
    private String nick;
    @SerializedName("open_id")
    private String openId;
    @SerializedName("top_auth_code")
    private String topAuthCode;
    @SerializedName("top_expire_time")
    private String topExpireTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenSid() {
        return openSid;
    }

    public void setOpenSid(String openSid) {
        this.openSid = openSid;
    }

    public String getTopAccessToken() {
        return topAccessToken;
    }

    public void setTopAccessToken(String topAccessToken) {
        this.topAccessToken = topAccessToken;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getHavanaSsoToken() {
        return havanaSsoToken;
    }

    public void setHavanaSsoToken(String havanaSsoToken) {
        this.havanaSsoToken = havanaSsoToken;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getTopAuthCode() {
        return topAuthCode;
    }

    public void setTopAuthCode(String topAuthCode) {
        this.topAuthCode = topAuthCode;
    }

    public String getTopExpireTime() {
        return topExpireTime;
    }

    public void setTopExpireTime(String topExpireTime) {
        this.topExpireTime = topExpireTime;
    }

    @Override
    public String toString() {
        return "TaoBaoBean{" +
                "userId='" + userId + '\'' +
                ", openSid='" + openSid + '\'' +
                ", topAccessToken='" + topAccessToken + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", havanaSsoToken='" + havanaSsoToken + '\'' +
                ", nick='" + nick + '\'' +
                ", openId='" + openId + '\'' +
                ", topAuthCode='" + topAuthCode + '\'' +
                ", topExpireTime='" + topExpireTime + '\'' +
                '}';
    }
}
