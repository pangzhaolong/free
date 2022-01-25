package com.donews.middle.bean.globle;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class ABBean extends BaseCustomViewModel {
    @SerializedName("openAB")
    private boolean openAB = true;
    @SerializedName("refreshInterval")
    private int refreshInterval = 30;
    @SerializedName("openVideoToast")
    private boolean openVideoToast = true;
    @SerializedName("openAutoLottery")
    private boolean openAutoLottery = true;
    @SerializedName("openHomeGuid")
    private int openHomeGuid = 0;
    @SerializedName("openAutoAgreeProtocol")
    private boolean openAutoAgreeProtocol = false;
    @SerializedName("openAutoLotteryCount")
    private int openAutoLotteryCount = 3;
    @SerializedName("openAutoLotteryAfterLoginWx")
    private boolean openAutoLotteryAfterLoginWx = true;
    @SerializedName("openAutoLotteryAfterLoginWxAtExitDialog")
    private boolean openAutoLotteryAfterLoginWxAtExitDialog = true;
    @SerializedName("openGuidGif")
    private boolean openGuidGif = false;
    @SerializedName("lotteryLine")
    private int lotteryLine = 0;
    @SerializedName("lotteryPriceShow")
    private int lotteryPriceShow = 1;  //应用显示的抽奖价格 默认1元
    @SerializedName("openCritModel")                //暴击模式开关；true: 打开；false:关闭
    private boolean openCritModel = true;
    @SerializedName("openCritModelByOldUserCount")  //老用户抽奖几次开启暴击
    private int openCritModelByOldUserCount = 1;

    @SerializedName("enableOpenCritModelCount")     //每天允许开启暴击模式总次数（包含老用户，新用户，等各种模式）
    private int enableOpenCritModelCount = 3;

    @SerializedName("openCritModelByNewUser")       //新用户是否开启暴击模式；true:开启；false:关闭
    private boolean openCritModelByNewUser = true;

    @SerializedName("openCritModelByNewUserCount")  //新用户开启暴击模式次数（前提：openCritModelByNewUser==true）；
    private int openCritModelByNewUserCount = 3;

    @SerializedName("openScoreModelCrit")           //积分模式是否开启暴击模式开关； true: 打开；false：关闭
    private boolean openScoreModelCrit = true;


    @SerializedName("scoreTaskPlayTime")            //积分墙任务，玩多少时间，单位：秒
    private int scoreTaskPlayTime = 60;
    @SerializedName("openScoreTask")                //积分墙总开关
    private boolean openScoreTask = false;
    @SerializedName("openScoreTaskMax")             //积分任务次数上限
    private int openScoreTaskMax = 3;
    @SerializedName("selectNumberLocation")             //抽奖页自选码出现的位置(1---6)
    private int selectNumberLocation = 6;
    @SerializedName("openOptionalCode")             // 自选码的开关 true 打开 false 关闭
    private boolean openOptionalCode = true;

    public boolean isOpenOptionalCode() {
        return openOptionalCode;
    }

    @SerializedName("openJumpDlg")
    private boolean openJumpDlg = true;

    public boolean isOpenJumpDlg() {
        return openJumpDlg;
    }

    public int getSelectNumberLocation() {
        return selectNumberLocation;
    }

    public boolean isOpenSkipSplashAd4NewUser() {
        return openSkipSplashAd4NewUser;
    }

    @SerializedName("openSkipSplashAd4NewUser")
    private boolean openSkipSplashAd4NewUser = false;   // # 是否打开新用户不展示开屏广告；true：不展示开屏广告；false：展示开屏广告

    public boolean isOpenScoreTask() {
        return openScoreTask;
    }

    public int getOpenScoreTaskMax() {
        return openScoreTaskMax;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public int getEnableOpenCritModelCount() {
        return enableOpenCritModelCount;
    }

    public boolean isOpenCritModelByNewUser() {
        return openCritModelByNewUser;
    }

    public int getOpenCritModelByNewUserCount() {
        return openCritModelByNewUserCount;
    }


    public boolean isOpenScoreModelCrit() {
        return openScoreModelCrit;
    }

    public void setOpenScoreModelCrit(boolean openScoreModelCrit) {
        this.openScoreModelCrit = openScoreModelCrit;
    }

    public int getScoreTaskPlayTime() {
        return scoreTaskPlayTime;
    }

    public void setScoreTaskPlayTime(int scoreTaskPlayTime) {
        this.scoreTaskPlayTime = scoreTaskPlayTime;
    }


    public boolean isOpenCritModel() {
        return openCritModel;
    }

    public int getOpenCritModelByOldUserCount() {
        return openCritModelByOldUserCount;
    }

    public int getLotteryLine() {
        return lotteryLine;
    }

    public void setLotteryLine(int lotteryLine) {
        this.lotteryLine = lotteryLine;
    }

    public boolean isOpenGuidGif() {
        return openGuidGif;
    }

    public void setOpenGuidGif(boolean openGuidGif) {
        this.openGuidGif = openGuidGif;
    }

    public boolean isOpenAutoLotteryAfterLoginWxAtExitDialog() {
        return openAutoLotteryAfterLoginWxAtExitDialog;
    }

    public void setOpenAutoLotteryAfterLoginWxAtExitDialog(boolean openAutoLotteryAfterLoginWxAtExitDialog) {
        this.openAutoLotteryAfterLoginWxAtExitDialog = openAutoLotteryAfterLoginWxAtExitDialog;
    }

    public boolean isOpenAutoLotteryAfterLoginWx() {
        return openAutoLotteryAfterLoginWx;
    }

    public void setOpenAutoLotteryAfterLoginWx(boolean openAutoLotteryAfterLoginWx) {
        this.openAutoLotteryAfterLoginWx = openAutoLotteryAfterLoginWx;
    }

    public int getOpenAutoLotteryCount() {
        return openAutoLotteryCount;
    }

    public void setOpenAutoLotteryCount(int openAutoLotteryCount) {
        this.openAutoLotteryCount = openAutoLotteryCount;
    }

    public boolean isOpenAutoAgreeProtocol() {
        return openAutoAgreeProtocol;
    }

    public void setOpenAutoAgreeProtocol(boolean openAutoAgreeProtocol) {
        this.openAutoAgreeProtocol = openAutoAgreeProtocol;
    }

    public int getOpenHomeGuid() {
        return openHomeGuid;
    }

    public void setOpenHomeGuid(int openHomeGuid) {
        this.openHomeGuid = openHomeGuid;
    }

    public void setOpenAB(boolean openAB) {
        this.openAB = openAB;
    }

    public boolean isOpenAutoLottery() {
        return openAutoLottery;
    }

    public void setOpenAutoLottery(boolean openAutoLottery) {
        this.openAutoLottery = openAutoLottery;
    }

    public boolean isOpenVideoToast() {
        return openVideoToast;
    }

    public void setOpenVideoToast(boolean openVideoToast) {
        this.openVideoToast = openVideoToast;
    }

    @Override
    public String toString() {
        return "ABBean{" +
                "ab='" + openAB + '\'' +
                '}';
    }

    public int getLotteryPriceShow() {
        return lotteryPriceShow;
    }

    public void setLotteryPriceShow(int lotteryPriceShow) {
        this.lotteryPriceShow = lotteryPriceShow;
    }

    public boolean isOpenAB() {
        return false;
//        return openAB;
    }
}
