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

        val childCount = parent.childCount

        val totalRaw = if (childCount % spanCount == 0) {
            childCount / spanCount
        } else {
            childCount / spanCount + 1
        }

        val position = parent.getChildAdapterPosition(view)
        val column = (position % spanCount) + 1

        val raw = position / spanCount

        outRect.left = (column - 1) * space / spanCount
        outRect.right = (spanCount - column) * space / spanCount
        if (raw == 0) {
            outRect.top = 0
        } else {
            outRect.top = space
        }
    }
}