package com.module.lottery.view;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

public class TextureVideoViewOutlineProvider extends ViewOutlineProvider {
    @Override
    public void getOutline(View view, Outline outline) {
        int left = 0;
        int top = (view.getHeight() - view.getWidth()) / 2;
        int right = view.getWidth();
        int bottom = (view.getHeight() - view.getWidth()) / 2 + view.getWidth();
        outline.setOval(left, top, right, bottom);
    }
}
