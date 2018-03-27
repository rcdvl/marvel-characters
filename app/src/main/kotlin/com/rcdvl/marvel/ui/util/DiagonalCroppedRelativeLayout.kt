package com.rcdvl.marvel.ui.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.RelativeLayout

/**
 * Created by renan on 3/18/16.
 */
class DiagonalCroppedRelativeLayout(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    var path: Path = Path()

    override fun dispatchDraw(canvas: Canvas?) {
        val px: Float = AndroidUtils.dpToPx(20f)

        path.lineTo(width.toFloat(), 0f)
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat() - px)
        path.lineTo(0f, 0f)

        canvas?.clipPath(path)
        super.dispatchDraw(canvas)
    }

    init {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)
        }
    }
}