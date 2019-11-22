package com.rcdvl.marvel.ui.util

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.transition.Transition
import android.transition.Transition.TransitionListener
import android.util.AttributeSet
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.rcdvl.marvel.R


/**
 * Created by renan on 3/18/16.
 */
class DiagonalDividerImageView : ImageView {

    private val lineColor: Int
    private var path: Path = Path()
    private val borderPaint = Paint()
    private var borderColor: Int = 0
    private var isBorderEnabled = true
    private var isFirstTransition = true
    var onBorderChangeListener: OnBorderChangeListener? = null

    interface OnBorderChangeListener {
        fun onBorderChange(borderColor: Int)
    }

    private val transitionListener = object : TransitionListener {
        override fun onTransitionEnd(p0: Transition?) {
            if (isFirstTransition) {
                isFirstTransition = false
                isBorderEnabled = true
                invalidate()
            }
        }

        override fun onTransitionResume(p0: Transition?) {
        }

        override fun onTransitionPause(p0: Transition?) {
        }

        override fun onTransitionCancel(p0: Transition?) {
        }

        override fun onTransitionStart(p0: Transition?) {
            isBorderEnabled = false
            invalidate()
        }
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val a: TypedArray = context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.DiagonalDividerImageView,
                0, 0)

        try {
            lineColor = a.getColor(R.styleable.DiagonalDividerImageView_dividerColor, Color.GREEN)
        } finally {
            a.recycle()
        }

        borderPaint.strokeWidth = AndroidUtils.dpToPx(5f)
        borderPaint.style = Paint.Style.STROKE

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(LAYER_TYPE_SOFTWARE, null)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (context as AppCompatActivity).window.sharedElementReturnTransition?.let {
                it.addListener(transitionListener)
            }
        }
    }

    override fun setImageBitmap(bm: Bitmap) {
        super.setImageBitmap(bm)

        Palette.from(bm).generate {
            borderColor = it!!.getVibrantColor(0)
            invalidate()
            onBorderChangeListener?.onBorderChange(borderColor)
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)

        if (drawable != null) {
            Palette.from(drawableToBitmap(drawable)).generate {
                borderColor = it!!.getVibrantColor(0)
                invalidate()
                onBorderChangeListener?.onBorderChange(borderColor)
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        val px: Float = AndroidUtils.dpToPx(20f)

        path.lineTo(width.toFloat(), 0f)
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(0f, height.toFloat() - px)
        path.lineTo(0f, 0f)

        canvas?.clipPath(path)
        super.onDraw(canvas)

        if (isBorderEnabled) {
            borderPaint.color = borderColor
            canvas?.drawLine(0f, height.toFloat() - px, width.toFloat(), height.toFloat(),
                    borderPaint)
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        val bitmap: Bitmap

        when (drawable) {
            is BitmapDrawable -> {
                if (drawable.bitmap != null) {
                    return drawable.bitmap
                }
            }
            is TransitionDrawable -> {
                val firstDrawable = drawable.getDrawable(drawable.numberOfLayers - 1)

                when (firstDrawable) {
                    is GifDrawable -> return firstDrawable.firstFrame
                    is BitmapDrawable -> return firstDrawable.bitmap
                }
            }
        }

        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1,
                    Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            (context as AppCompatActivity).window.sharedElementReturnTransition?.let {
                it.removeListener(transitionListener)
            }
        }
    }
}