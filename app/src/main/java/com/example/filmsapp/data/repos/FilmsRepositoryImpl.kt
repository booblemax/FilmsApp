package com.example.filmsapp.data.repos

import android.util.SparseArray
import androidx.core.util.contains
import com.example.filmsapp.data.remote.FilmsApi
import com.example.filmsapp.data.remote.response.films.BackdropsDto
import com.example.filmsapp.domain.DispatcherProvider
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.domain.exceptions.RetrofitException
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.ui.base.models.FilmModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext

class FilmsRepositoryImpl(
    private val api: FilmsApi,
    private val dispatcher: DispatcherProvider,
    private val pageSize: Int
) : FilmsRepository {

    private val cache = SparseArray<MutableList<FilmModel>>()

    override suspend fun getLatestFilm(): Resource<FilmModel> =
        withContext(dispatcher.io()) {
            val response = api.getLatestFilm()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Resource.SUCCESS(body.toModel())
            } else {
                Resource.ERROR<FilmModel>(
                    RetrofitException(response.code(), response.message())
                )
            }
        }

    override suspend fun getPopularFilms(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> =
        withContext(dispatcher.io()) { getFilmsCached(page, forceUpdate, FilmType.POPULAR) }

    override suspend fun getTopRatedFilms(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> =
        withContext(dispatcher.io()) { getFilmsCached(page, forceUpdate, FilmType.TOP_RATED) }

    override suspend fun getUpcomingFilms(
        page: Int,
        forceUpdate: Boolean
    ): Resource<List<FilmModel>> =
        withContext(dispatcher.io()) { getFilmsCached(page, forceUpdate, FilmType.UPCOMING) }

    override suspend fun getFilm(id: String): Resource<FilmModel> =
        withContext(dispatcher.io()) {
            val filmResponse = api.getFilm(id)
            if (filmResponse.isSuccessful && filmResponse.body() != null) {
                val filmDto = filmResponse.body()
                var images: BackdropsDto? = null
                supervisorScope {
                    launch(coroutineContext) {
                        images = api.getBackdrops(id)
                    }
                }
                Resource.SUCCESS(filmDto?.toModel(images))
            } else {
                Resource.ERROR<FilmModel>(RetrofitException(filmResponse.code(), filmResponse.message()))
            }
        }

    private suspend fun getFilmsCached(
        page: Int,
        forceUpdate: Boolean,
        type: FilmType
    ): Resource<List<FilmModel>> {
        val filmsCache = getCacheForFilmType(type)

        if (forceUpdate) {
            filmsCache.clear()
        }
        if (forceUpdate || filmsCache.size < page * pageSize) {
            val response = performRequest(page, type)

            val body = response.body()
            if (response.isSuccessful && body != null) {
                val films = body.results.map { it.toModel() }

                filmsCache.addAll(films)
            } else {
                return Resource.ERROR<List<FilmModel>>(
                    RetrofitException(response.code(), response.message())
                )
            }
        }
        return Resource.SUCCESS(filmsCache.toList())
    }

    private fun getCacheForFilmType(type: FilmType): MutableList<FilmModel> {
        return if (cache.contains(type.ordinal)) cache.get(type.ordinal)
        else {
            val newFilmsCache = mutableListOf<FilmModel>()
            cache.put(type.ordinal, newFilmsCache)
            newFilmsCache
        }
    }

    private suspend fun performRequest(page: Int, type: FilmType) =
        when (type) {
            FilmType.POPULAR -> api.getPopularList(page)
            FilmType.TOP_RATED -> api.getTopRatedList(page)
            FilmType.UPCOMING -> api.getUpcomingList(page)
        }

    enum class FilmType {
        POPULAR,
        TOP_RATED,
        UPCOMING
    }
}
