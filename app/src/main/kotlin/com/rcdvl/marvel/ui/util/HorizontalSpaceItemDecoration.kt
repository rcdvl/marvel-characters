package com.rcdvl.marvel.ui.util

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View

/**
 * Created by Renan on 20/03/2016.
 */
class HorizontalSpaceItemDecoration : RecyclerView.ItemDecoration {

    val horizontalSpaceWidth: Float;

    constructor(spacing: Float = 8f) {
        horizontalSpaceWidth = spacing
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        if (parent?.getChildAdapterPosition(view) == 0) {
            return;
        }

        val r = parent?.context?.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, horizontalSpaceWidth, r?.displayMetrics);

        outRect?.left = px.toInt();
    }
}