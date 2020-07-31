package com.example.filmsapp.ui.details.transformers

import android.view.View
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2

class OffsetTransformer(private val offsetPx: Float) : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val offset = OFFSET_MULTIPLIER * offsetPx * position
        val viewPager = page.parent.parent as ViewPager2
        if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            if (ViewCompat.getLayoutDirection(viewPager) ==
                ViewCompat.LAYOUT_DIRECTION_RTL
            ) {
                page.translationX = -offset
            } else {
                page.translationX = offset
            }
        } else {
            page.translationY = offset
        }
    }

    companion object {
        const val OFFSET_MULTIPLIER = -2.0f
    }
}
