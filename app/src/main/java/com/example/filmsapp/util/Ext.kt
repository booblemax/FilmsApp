package com.example.filmsapp.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import com.example.filmsapp.R
import com.google.android.material.snackbar.Snackbar

fun Activity.setupToolbar(toolbar: Toolbar) {
    (this as AppCompatActivity).setSupportActionBar(toolbar)
}

inline fun View.snack(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit = {}
) {
    snack(resources.getString(messageRes), length, f)
}

inline fun View.snack(
    message: String,
    length: Int = Snackbar.LENGTH_LONG,
    f: Snackbar.() -> Unit = {}
) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.animateVisible() {
    if (visibility != View.VISIBLE) {
        animate().alpha(1f).withEndAction { visible() }.start()
    }
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.animateGone() {
    animate().alpha(1f).withEndAction { visible() }.start()
}

fun View.setMarginTop(marginTop: Int) {
    val menuLayoutParams = layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(0, marginTop, 0, 0)
    layoutParams = menuLayoutParams
}

fun Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}

fun Fragment.waitForTransition(targetView: View) {
    postponeEnterTransition()
    targetView.doOnPreDraw { startPostponedEnterTransition() }
}

fun Activity.makeStatusBarTransparent() {
    if (Versions.isApiOrUp(Build.VERSION_CODES.LOLLIPOP)) {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
    }
}

fun Activity.makeStatusBarVisible() {
    window.apply {
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        statusBarColor = ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, theme)
    }
}
