package com.example.filmsapp.util

import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.Coil
import coil.api.load
import coil.request.LoadRequest
import coil.request.Request
import coil.transform.RoundedCornersTransformation
import com.example.filmsapp.BuildConfig
import com.example.filmsapp.R
import com.example.filmsapp.domain.Resource

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
