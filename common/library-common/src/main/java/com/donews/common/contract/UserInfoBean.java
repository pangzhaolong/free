package com.donews.common.contract;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import com.google.gson.annotations.SerializedName;

/**
 * <p> </p>
 * 作者： created by honeylife<br>
 * 日期： 2020/11/11 17:38<br>
 * 版本：V1.0<br>
 */
public class UserInfoBean extends BaseCustomViewModel {


    /**
     * id : string
     * user_name : string
     * wechat : string
     * head_img : string
     * gender : GENDER_UNKNOWN
     * birthday : string
     * token : string
     * third_party_id : string
     * is_new : true
     * wechat_extra : {"access_token":"string","expires_in":"string","refresh_token":"string","open_id":"string",
     * "scope":"string","nick_name":"string","sex":0,"province":"string","city":"string","country":"string",
     * "headimgurl":"string","privilege":["string"],"unionid":"string"}
     * taobao_extra : {"user_id":"string","open_sid":"string","top_access_token":"string","avatar_url":"string",
     * "havana_sso_token":"string","nick":"string","open_id":"string","top_auth_code":"string",
     * "top_expire_time":"string"}
     * mobile : string
     * isInvited :是否填写过邀请嘛
     */

    private String id;
    @SerializedName("user_name")
    private String userName;
    private String wechat;
    @SerializedName("head_img")
    private String headImg;
    private String gender;
    private String birthday;
    private String token;
    @SerializedName("third_party_id")
    private String thirdPartyId;
    @SerializedName("is_new")
    private boolean isNew;
    @SerializedName("wechat_extra")
    private WeChatBean wechatExtra;
    @SerializedName("taobao_extra")
    private TaoBaoBean taobaoExtra;
    private String mobile;
    //邀请码
    @SerializedName("invite_code")
    private String inviteCode = "";
    // true 表示填写过邀请码，false 表示未填写过邀请码
    @SerializedName("is_invited")
    private boolean isInvited;
    /** 注册时间 */
    @SerializedName("created_at")
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        notifyPropertyChanged(BR.userName);
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    @Bindable
    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
        notifyPropertyChanged(BR.headImg);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId;
    }

    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public WeChatBean getWechatExtra() {
        return wechatExtra;
    }

    public void setWechatExtra(WeChatBean wechatExtra) {
        this.wechatExtra = wechatExtra;
    }

    public TaoBaoBean getTaobaoExtra() {
        return taobaoExtra;
    }

    public void setTaobaoExtra(TaoBaoBean taobaoExtra) {
        this.taobaoExtra = taobaoExtra;
    }

    public boolean isInvited() {
        return isInvited;
    }

    public void setInvited(boolean invited) {
        isInvited = invited;
    }

    @Bindable
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
        notifyPropertyChanged(BR.mobile);
    }

    @Bindable
    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
        notifyPropertyChanged(BR.inviteCode);
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserInfoBean{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", wechat='" + wechat + '\'' +
                ", headImg='" + headImg + '\'' +
                ", gender='" + gender + '\'' +
                ", birthday='" + birthday + '\'' +
                ", token='" + token + '\'' +
                ", thirdPartyId='" + thirdPartyId + '\'' +
                ", isNew=" + isNew +
                ", wechatExtra=" + wechatExtra +
                ", taobaoExtra=" + taobaoExtra +
                ", mobile='" + mobile + '\'' +
                ", inviteCode='" + inviteCode + '\'' +
                ", isInvited=" + isInvited +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    /**
     * 头像是否为空
     *
     * @return
     */
    public boolean isHeadImageNull() {
        return !TextUtils.isEmpty(this.getHeadImg());
    }

    /**
     * 是否绑定手机号
     *
     * @return
     */
    public boolean isBindMobile() {
        return !TextUtils.isEmpty(this.getMobile());
    }

    /**
     * 是否绑定微信
     *
     * @return
     */
    public boolean isBindWechat() {
        return !TextUtils.isEmpty(this.getWechatExtra() != null ? this.getWechatExtra().getOpenId() : null);
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return !(TextUtils.isEmpty(getId()) || getId().equals("0"));

    }
}