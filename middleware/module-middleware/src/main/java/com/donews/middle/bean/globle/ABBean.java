package com.donews.middle.bean.globle;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ABBean extends BaseCustomViewModel {
    @SerializedName("openAB")
    private boolean openAB;
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
    @SerializedName("openJumpDlg")
    private boolean openJumpDlg = true;
    public boolean isOpenJumpDlg() {
        return openJumpDlg;
    }
    @SerializedName("applicationShareJumpSwitch")                //抽奖页分享超链接跳转开关
    private boolean applicationShareJumpSwitch = false;
    @SerializedName("applicationBuyJumpSwitch")                //抽奖页购买超链接跳转开关
    private boolean applicationBuyJumpSwitch = false;
    @SerializedName("applicationBuyDelayedJump")                //延迟多少秒后跳转到商品购买详情（单位毫秒）
    private long applicationBuyDelayedJump = 500;
    @SerializedName("intervalsTime")                  //下一次的间隔时间差（单位毫秒）
    private long intervalsTime = 500;
    @SerializedName("applicationShareJumpUrl")                 //抽奖页分享超链接
    private List<String> applicationShareJumpUrl;
    @SerializedName("applicationBuyJumpUrl")                      //抽奖页购买超链接
    private List<String> applicationBuyJumpUrl;
    @SerializedName("applicationBuyJumpNumber")
    private long applicationBuyJumpNumber = 2;                 //抽奖页购买超链接跳转次数
    @SerializedName("screenUnlockJumpSwitch")                //手机解锁超链接跳转开关
    private boolean screenUnlockJumpSwitch = false;
    @SerializedName("delayedJump")                //延迟多少毫秒后跳转
    private long delayedJump = 2000;
    @SerializedName("revealNumber")                //每日展示次数
    private int revealNumber = 2;

    @SerializedName("skipSplashAd4NewUser")
    private boolean skipSplashAd4NewUser = false;   // # 是否打开新用户不展示开屏广告；true：不展示开屏广告；false：展示开屏广告

    @SerializedName("showInterstitialAdWhenOpenYyw")
    private boolean showInterstitialAdWhenOpenYyw = false;
    @SerializedName("showAppToParterCount")
    private int  showAppToParterCount = 2;          //# 运营位跳转合作商弹窗显示次数
    @SerializedName("kfQQ")
    private String kfQQ;
    @SerializedName("showSplashScaleBtn")
    private boolean showSplashScaleBtn = false;
    @SerializedName("initDnSdkWhenApplicationLanuch")
    private boolean initDnSdkWhenApplicationLanuch = false;

    public boolean isSkipSplashAd4NewUser() {
        return skipSplashAd4NewUser;
    }

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

    public boolean isScreenUnlockJumpSwitch() {
        return screenUnlockJumpSwitch;
    }

    public long getApplicationBuyDelayedJump() {
        return applicationBuyDelayedJump;
    }

    public void setApplicationBuyDelayedJump(int applicationBuyDelayedJump) {
        this.applicationBuyDelayedJump = applicationBuyDelayedJump;
    }
    public long getApplicationBuyJumpNumber() {
        return applicationBuyJumpNumber;
    }

    public void setApplicationBuyJumpNumber(long applicationBuyJumpNumber) {
        this.applicationBuyJumpNumber = applicationBuyJumpNumber;
    }

    public void setScreenUnlockJumpSwitch(boolean screenUnlockJumpSwitch) {
        this.screenUnlockJumpSwitch = screenUnlockJumpSwitch;
    }

    public long getIntervalsTime() {
        return intervalsTime;
    }

    public void setIntervalsTime(int intervalsTime) {
        this.intervalsTime = intervalsTime;
    }

    public long getDelayedJump() {
        return delayedJump;
    }

    public void setDelayedJump(int delayedJump) {
        this.delayedJump = delayedJump;
    }

    public int getRevealNumber() {
        return revealNumber;
    }

    public void setRevealNumber(int revealNumber) {
        this.revealNumber = revealNumber;
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

    public boolean isApplicationShareJumpSwitch() {
        return applicationShareJumpSwitch;
    }

    public void setApplicationShareJumpSwitch(boolean applicationShareJumpSwitch) {
        this.applicationShareJumpSwitch = applicationShareJumpSwitch;
    }

    public boolean isApplicationBuyJumpSwitch() {
        return applicationBuyJumpSwitch;
    }

    public void setApplicationBuyJumpSwitch(boolean applicationBuyJumpSwitch) {
        this.applicationBuyJumpSwitch = applicationBuyJumpSwitch;
    }

    public List<String> getApplicationShareJumpUrl() {
        return applicationShareJumpUrl;
    }

    public void setApplicationShareJumpUrl(List<String> applicationShareJumpUrl) {
        this.applicationShareJumpUrl = applicationShareJumpUrl;
    }

    public List<String> getApplicationBuyJumpUrl() {
        return applicationBuyJumpUrl;
    }

    public void setApplicationBuyJumpUrl(List<String> applicationBuyJumpUrl) {
        this.applicationBuyJumpUrl = applicationBuyJumpUrl;
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

    public boolean isShowInterstitialAdWhenOpenYyw() {
        return showInterstitialAdWhenOpenYyw;
    }

    public int getShowAppToParterCount() {
        return showAppToParterCount;
    }

    public String getKfQQ() {
        return kfQQ;
    }

    public boolean isShowSplashScaleBtn() {
        return showSplashScaleBtn;
    }

    public boolean isInitDnSdkWhenApplicationLanuch() {
        return initDnSdkWhenApplicationLanuch;
    }

    @Override
    public String toString() {
        return "ABBean{" +
                "ab='" + openAB + '\'' +
                '}';
    }

    public boolean isOpenAB() {
        return false;
    }

    public boolean getOpenAB() {
        return openAB;
    }
}
