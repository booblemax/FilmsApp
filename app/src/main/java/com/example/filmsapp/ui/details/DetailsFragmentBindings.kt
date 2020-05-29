package com.example.filmsapp.ui.details

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import coil.api.load
import com.example.filmsapp.BuildConfig
import com.example.filmsapp.R
import com.example.filmsapp.data.remote.response.Genre
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.util.gone
import com.example.filmsapp.util.visible
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

@BindingAdapter("genres")
fun setGenres(textView: AppCompatTextView, genres: List<Genre>?) {
    genres?.let {
        textView.text = genres.joinToString { genre -> genre.name }
    }
}

@BindingAdapter("year")
fun setYear(textView: AppCompatTextView, releaseDate: String?) {
    releaseDate?.let {
        var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(it, dateFormatter)
        val zoneDate = date.atStartOfDay(ZoneId.systemDefault())
        textView.text = zoneDate.year.toString()
    }
}

@BindingAdapter("runtime")
fun setLength(textView: AppCompatTextView, runtime: Int?) {
    runtime?.let {
        val h = it / 60
        val m = it - h * 60
        textView.text = "${h}h ${m}m"
    }
}

@BindingAdapter("backdrop")
fun setBackdrop(imageView: AppCompatImageView, url: String?) {
    imageView.load(BuildConfig.FULL_IMAGE_URL + url) {
        error(R.drawable.ic_error)
    }
}

@BindingAdapter("android:visibility")
fun <T> setVisibility(ratingBar: AppCompatRatingBar, status: Resource<T>?) {
    if (status is Resource.LOADING) ratingBar.gone() else ratingBar.visible()
}
