package com.donews.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 *
 *
 * @author XuShuai
 * @version v1.0
 * @date 2021/10/22 17:28
 */
class GridItemDecoration(private val spanCount: Int, val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)
        outRect.bottom = space
        if (position % spanCount == 0) {
            outRect.left = 0
        } else {
            outRect.left = space * (position % spanCount)
        }

        if (position >= spanCount) {
            outRect.top = space
        } else {
            outRect.top = 0;
        }
    }
}