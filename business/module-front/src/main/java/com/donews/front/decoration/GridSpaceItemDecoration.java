package com.donews.front.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * created
 */
public class GridSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpanCount;//横条目数量

    /**
     * @param spanCount 列数
     */
    public GridSpaceItemDecoration(int spanCount) {
        this.mSpanCount = spanCount;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view); // 获取view 在adapter中的位置。
        int column = position % mSpanCount; // view 所在的列
        outRect.top = 32;
        if (column == 0) {
            outRect.left = 0;
            outRect.right = 15;
        } else {
            outRect.right = 0;
            outRect.left = 15;
        }
    }
}