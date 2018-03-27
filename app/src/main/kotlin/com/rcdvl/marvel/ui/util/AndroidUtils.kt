package com.rcdvl.marvel.ui.util

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by renan on 3/22/16.
 */
class AndroidUtils {

    companion object {

        fun dpToPx(dp: Float): Float {
            return dp * Resources.getSystem().displayMetrics.density
        }

        fun screenSize(context: Context): DisplayMetrics {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            wm.defaultDisplay.getMetrics(metrics)

            return metrics
        }
    }
}