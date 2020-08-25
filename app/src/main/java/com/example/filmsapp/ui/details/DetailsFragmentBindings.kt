package com.example.filmsapp.ui.details

import androidx.appcompat.widget.AppCompatRatingBar
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.example.domain.Resource
import com.example.domain.models.GenreModel
import com.example.filmsapp.R
import com.example.filmsapp.util.gone
import com.example.filmsapp.util.visible
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

@BindingAdapter("genres")
fun setGenres(textView: AppCompatTextView, genres: List<GenreModel>?) {
    genres?.let {
        textView.text = genres.joinToString { genre -> genre.name }
    }
}

@BindingAdapter("year")
fun setYear(textView: AppCompatTextView, releaseDate: String?) {
    if (!releaseDate.isNullOrEmpty()) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(releaseDate, dateFormatter)
        val zoneDate = date.atStartOfDay(ZoneId.systemDefault())
        textView.text = zoneDate.year.toString()
    }
}

@BindingAdapter("runtime")
fun setLength(textView: AppCompatTextView, runtime: Int?) {
    runtime?.let {
        val minuteInHour = textView.context.resources.getInteger(R.integer.minutes_in_hour)
        val h = it / minuteInHour
        val m = it - h * minuteInHour
        textView.text = textView.context.getString(R.string.time, h.toString(), m.toString())
    }
}

@BindingAdapter("android:visibility")
fun <T> setVisibility(ratingBar: AppCompatRatingBar, status: Resource<T>?) {
    if (status is Resource.LOADING) ratingBar.gone() else ratingBar.visible()
}
