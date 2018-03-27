package com.rcdvl.marvel.ui.util

import android.support.v4.view.ViewPager
import android.view.View

/**
 * Created by renan on 3/22/16.
 */
class ZoomOutSlideTransformer : ViewPager.PageTransformer {

    companion object {
        val MIN_SCALE = 0.9f;
        val MIN_ALPHA = 0.6f;
    }

    override fun transformPage(page: View?, position: Float) {
        if (page == null) {
            return
        }

        val correctPosition = position - (page.parent as ViewPager).paddingRight / page.width.toFloat()

        if (correctPosition <= -1 || correctPosition >= 1) {
            page.alpha = 0f
        } else {
            page.alpha = 1f
        }

        if (correctPosition >= -1 || correctPosition <= 1) {
            // Modify the default slide transition to shrink the page as well
            val height = page.height
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(correctPosition))
            val vertMargin = height * (1 - scaleFactor) / 2
            val horzMargin = page.width * (1 - scaleFactor) / 2

            // Center vertically
            page.pivotY = 0.5f * height

            if (correctPosition < 0) {
                page.translationX = horzMargin - vertMargin / 2
            } else {
                page.translationX = -horzMargin + vertMargin / 2
            }

            // Scale the page down (between MIN_SCALE and 1)
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor

            // Fade the page relative to its size.
            page.alpha = (MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
        }
    }
}