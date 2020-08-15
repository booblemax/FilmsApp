package com.example.filmsapp.data.repos

import android.util.SparseArray
import androidx.core.util.contains
import com.example.filmsapp.data.db.FilmsDao
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
    private val filmsDao: FilmsDao,
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

    override suspend fun getFavouritesFilms(page: Int): Resource<List<FilmModel>> {
        return withContext(dispatcher.io()) {
            try {
                val result = filmsDao.getFilms()
                Resource.SUCCESS(result.map { it.toModel() })
            } catch (e: IllegalArgumentException) {
                Resource.ERROR<List<FilmModel>>(e)
            }
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

    override suspend fun saveFilm(film: FilmModel) {
        withContext(dispatcher.io()) {
            filmsDao.insert(film.toDataModel())
        }
    }

    override suspend fun deleteFilm(film: FilmModel) {
        withContext(dispatcher.io()) {
            filmsDao.delete(film.toDataModel())
        }
    }

    override suspend fun isFilmStoredInDb(id: String): Boolean =
        withContext(dispatcher.io()) {
            return@withContext filmsDao.contains(id) > 0
        }

    override suspend fun getFilm(id: String, needUpdate: Boolean): Resource<FilmModel> =
        withContext(dispatcher.io()) {
            if (needUpdate) {
                val filmResponse = api.getFilm(id)
                if (filmResponse.isSuccessful && filmResponse.body() != null) {
                    val filmDto = filmResponse.body()
                    var images: BackdropsDto? = null
                    supervisorScope {
                        launch(coroutineContext) {
                            images = api.getBackdrops(id)
                        }
                    }
                    val filmModel = filmDto?.toModel(images)
                    filmModel?.toDataModel()?.let { filmsDao.insert(it) }
                    Resource.SUCCESS(filmModel)
                } else {
                    Resource.ERROR<FilmModel>(
                        RetrofitException(
                            filmResponse.code(),
                            filmResponse.message()
                        )
                    )
                }
            } else {
                val savedFilm = filmsDao.getFilm(id)
                Resource.SUCCESS(savedFilm?.toModel())
            }
        }

    override suspend fun searchFilms(query: String, page: Int): Resource<List<FilmModel>> =
        withContext(dispatcher.io()) {
            val searchResponse = api.searchFilms(query, page)
            if (searchResponse.isSuccessful && searchResponse.body() != null) {
                val filmsDto = searchResponse.body()
                Resource.SUCCESS(
                    filmsDto?.results?.map { it.toModel() } ?: listOf()
                )
            } else {
                Resource.ERROR<List<FilmModel>>(
                    RetrofitException(searchResponse.code(), searchResponse.message())
                )
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
                return Resource.ERROR(
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
