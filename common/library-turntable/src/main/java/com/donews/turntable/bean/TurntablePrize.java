package com.donews.turntable.bean;

import android.graphics.Bitmap;

public class TurntablePrize {
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
