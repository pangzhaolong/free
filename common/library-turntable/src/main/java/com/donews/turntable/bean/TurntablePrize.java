package com.donews.turntable.bean;

import android.graphics.Bitmap;

import com.donews.common.contract.BaseCustomViewModel;

public class TurntablePrize extends BaseCustomViewModel {
    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    String mName;
    Bitmap mBitmap;


}
