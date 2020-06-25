package com.example.filmsapp.util

import android.content.res.Resources
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.doOnPreDraw
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.Coil
import coil.api.load
import coil.request.LoadRequest
import coil.request.Request
import coil.transform.RoundedCornersTransformation
import com.example.filmsapp.BuildConfig
import com.example.filmsapp.R
import com.example.filmsapp.domain.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun startCoroutinesTimer(delay: Long, action: () -> Unit) = GlobalScope.launch {
    kotlinx.coroutines.delay(delay)
    action()
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
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
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

@BindingAdapter("textOrGone")
fun AppCompatTextView.setTextOrGone(text: String?) {
    if (text.isNullOrEmpty()) {
        gone()
    } else {
        visible()
        this.text = text
    }
}

@BindingAdapter("url", "requestListener", requireAll = false)
fun AppCompatImageView.src(url: String?, requestListener: Request.Listener? = null) {
    val imageLoader = Coil.imageLoader(context)
    val request = LoadRequest.Builder(context)
        .crossfade(true)
        .transformations(RoundedCornersTransformation(16.0f))
        .listener(requestListener)
        .error(R.drawable.ic_error)
        .data(BuildConfig.REDUCED_IMAGE_URL + url)
        .target(this)
        .build()

    imageLoader.execute(request)
}

@BindingAdapter("backdrop", "requestListener", requireAll = false)
fun setBackdrop(imageView: AppCompatImageView, url: String?, requestListener: Request.Listener? = null) {
    imageView.load(BuildConfig.FULL_IMAGE_URL + url) {
        listener(requestListener)
        error(R.drawable.ic_error)
    }
}

@BindingAdapter("android:rating")
fun AppCompatRatingBar.rating(rating: Double) {
    this.rating = rating.toFloat()
}

@BindingAdapter("android:visibility")
fun <T> View.visibility(status: Resource<T>?) {
    if (status is Resource.LOADING) gone() else visible()
}

@BindingAdapter("android:visibility")
fun <T> setVisibilityForProgress(progressBar: ProgressBar, status: Resource<T>?) {
    if (status is Resource.LOADING) progressBar.visible() else progressBar.gone()
}

@BindingAdapter("progress")
fun <T> bindStatus(layout: SwipeRefreshLayout, status: Resource<T>?) {
    layout.isRefreshing = status is Resource.LOADING
}

@BindingAdapter("progress")
fun <T> bindStatus(layout: SwipeRefreshLayout, status: Boolean) {
    layout.isRefreshing = status
}
