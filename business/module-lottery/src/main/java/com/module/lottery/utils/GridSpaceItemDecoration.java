package com.module.lottery.utils;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created
 */
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final String TAG = "GridSpaceItemDecoration";

    private int mSpanCount;//横条目数量

    /**
     * @param spanCount     列数
     */
    public GridSpaceItemDecoration(int spanCount) {
        this.mSpanCount = spanCount;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // 获取view 在adapter中的位置。
        int column = position % mSpanCount; // view 所在的列
        if (position != 0) {
            //设置右边距离
            if (column == 1) {
                outRect.right = 15;


                outRect.left=80;
            }
            if (column == 0) {
                outRect.left = 15;

                outRect.right = 80;
            }
            outRect.top = 30; // item top

        }
    }
}