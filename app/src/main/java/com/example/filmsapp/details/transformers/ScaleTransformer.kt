package com.example.filmsapp.details.transformers

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class ScaleTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        page.apply {
            translationY = abs(position) * TRANSLATION_MULTIPLIER
            scaleX = SCALE_X
        }
    }

    companion object {
        const val TRANSLATION_MULTIPLIER = 20f
        const val SCALE_X = 1.1f
    }
}
