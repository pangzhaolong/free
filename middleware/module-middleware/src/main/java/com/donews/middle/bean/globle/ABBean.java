package com.donews.middle.bean.globle;

import com.donews.common.contract.BaseCustomViewModel;
import com.google.gson.annotations.SerializedName;

public class ABBean extends BaseCustomViewModel {
    @SerializedName("openAB")
    private boolean openAB;
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
    @SerializedName("openCritModelByLotteryCount")
    private int openCritModelByLotteryCount = 1;
    @SerializedName("critModelSwitch")
    private int critModelSwitch = 1;
    @SerializedName("openCritModel")
    private boolean openCritModel = false;

    public boolean isOpenCritModel() {
        return openCritModel;
    }

    public int getOpenCritModelByLotteryCount() {
        return openCritModelByLotteryCount;
    }

    public int getCritModelSwitch() {
        return critModelSwitch;
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

    public boolean isOpenAB() {
        return openAB;
    }

    public boolean getOpenAB() {
        return openAB;
    }
}
