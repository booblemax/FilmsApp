package com.example.filmsapp.data.datasource

import com.example.filmsapp.data.remote.FilmsApi
import com.example.filmsapp.data.remote.response.films.BackdropDto
import com.example.filmsapp.data.remote.response.films.BackdropsDto
import com.example.filmsapp.data.remote.response.films.FilmDto
import com.example.filmsapp.data.remote.response.films.FilmsDto
import com.example.filmsapp.domain.exceptions.RetrofitException
import okhttp3.internal.http.RealResponseBody
import okio.Buffer
import retrofit2.Response

class FakeFilmsApi(
    private val latestFilm: FilmDto,
    private val populars: List<FilmDto>,
    private val topRated: List<FilmDto>,
    private val upcomings: List<FilmDto>,
    private val needFailureResult: (() -> Boolean)? = null
) : FilmsApi {

    override suspend fun getLatestFilm(): Response<FilmDto> {
        return if (needFailureResult?.invoke() == true) getError() else Response.success(latestFilm)
    }

    override suspend fun getPopularList(page: Int): Response<FilmsDto> {
        if (page < 0 || needFailureResult?.invoke() == true) return getError()
        val indexRange = getIndexRangeForPage(page)
        val dto = FilmsDto(page, populars.subList(indexRange.first, indexRange.last), 100, 100)
        return Response.success(dto)
    }

    override suspend fun getTopRatedList(page: Int): Response<FilmsDto> {
        if (page < 0 || needFailureResult?.invoke() == true) return getError()
        val indexRange = getIndexRangeForPage(page)
        val dto = FilmsDto(page, topRated.subList(indexRange.first, indexRange.last), 100, 100)
        return Response.success(dto)
    }

    override suspend fun getUpcomingList(page: Int): Response<FilmsDto> {
        if (page < 0 || needFailureResult?.invoke() == true) return getError()
        val indexRange = getIndexRangeForPage(page)
        val dto = FilmsDto(page, upcomings.subList(indexRange.first, indexRange.last), 100, 100)
        return Response.success(dto)
    }

    override suspend fun getFilm(id: String): Response<FilmDto> =
        if (needFailureResult?.invoke() == true) throw RetrofitException(404, "wrong id")
        else Response.success(
            populars.find { it.id.toString() == id } ?: throw RetrofitException(404, "wrong id")
        )

    override suspend fun getBackdrops(id: String): BackdropsDto {
        if (needFailureResult?.invoke() == true) throw RetrofitException(404, "wrong id")
        val dto =
            populars.find { it.id.toString() == id } ?: throw RetrofitException(404, "wrong id")
        return BackdropsDto(
            listOf(BackdropDto(0.0, dto.backdropPath ?: "", 1, 1, 0.0, 0.0, 0)),
            dto.id
        )
    }

    private fun getIndexRangeForPage(page: Int): IntRange =
        if (page == 1) 0..2
        else page..(page + 2)

    companion object {
        fun <T> getError() = Response.error<T>(404, RealResponseBody("", 0, Buffer()))
    }
}
