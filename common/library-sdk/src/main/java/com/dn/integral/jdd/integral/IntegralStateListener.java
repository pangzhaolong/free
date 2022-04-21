package com.dn.integral.jdd.integral;

public abstract interface IntegralStateListener {

    public void onAdShow();
    public void onAdClick();

    public void onStart();

    public void onProgress(long l, long l1);

    public void onComplete();

    public void onInstalled();

    public void onError(Throwable throwable);
    public void onRewardVerify();
    public void onRewardVerifyError(String s);

}
