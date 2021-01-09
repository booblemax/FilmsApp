package com.example.filmsapp.navigation

import androidx.fragment.app.Fragment
import com.example.filmsapp.databinding.SettingsFragmentBinding
import com.example.filmsapp.details.DetailsFragment
import com.example.filmsapp.imagesCarousel.ImagesCarouselFragment
import com.example.filmsapp.lists.ListsFragment
import com.example.filmsapp.main.MainFragment
import com.example.filmsapp.player.PlayerFragment
import com.example.filmsapp.search.SearchFragment
import com.example.filmsapp.settings.SettingsFragment
import com.example.filmsapp.splash.SplashFragment
import com.github.terrakok.cicerone.androidx.Creator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
sealed class FilmScreen(tag: String, createInstance: () -> Fragment) :
    FragmentScreen(tag, Creator { createInstance() }) {

    class SplashScreen : FilmScreen(SplashFragment.TAG, { SplashFragment() })

    class DetailsScreen(
        filmId: String, posterUrl: String, backdropUrl: String, isFavorite: Boolean
    ) : FilmScreen(DetailsFragment.TAG, {
        DetailsFragment.newInstance(filmId, posterUrl, backdropUrl, isFavorite)
    })

    class MainScreen(listType: Int) : FilmScreen(
        MainFragment.TAG, { MainFragment.newInstance(listType) }
    )

    class ListsScreen : FilmScreen(
        ListsFragment.TAG, { ListsFragment() }
    )

    class SearchScreen : FilmScreen(
        SearchFragment.TAG, { SearchFragment() }
    )

    class SettingsScreen : FilmScreen(
        SettingsFragment.TAG, { SettingsFragment() }
    )

    class ImagesCarouselScreen(urls: List<String>, position: Int) : FilmScreen(
        ImagesCarouselFragment.TAG, { ImagesCarouselFragment.newInstance(urls, position) }
    )

    class PlayerScreen(videoId: String) : FilmScreen(
        PlayerFragment.TAG, { PlayerFragment.newInstance(videoId) }
    )
}