package com.example.filmsapp.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import kotlin.math.min

object ScreenUtil {

    /**
     * @param itemWidth - width in pixels
     */
    fun getSpanCountForScreen(context: Context?, itemWidth: Int): Int {
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val spanCount = min(1, metrics.widthPixels / itemWidth)
        return spanCount
    }
}
