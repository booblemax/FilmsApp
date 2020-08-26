package com.example.filmsapp.base

import androidx.annotation.StringRes
import com.example.filmsapp.R

enum class ListType(@StringRes val titleId: Int) {
    POPULAR(R.string.title_popular),
    TOP_RATED(R.string.title_toprated),
    UPCOMING(R.string.title_upcoming),
    FAVOURITES(R.string.title_favourites)
}
