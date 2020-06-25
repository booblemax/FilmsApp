package com.example.filmsapp.data.remote

import com.example.filmsapp.data.remote.response.BackdropsDto
import com.example.filmsapp.data.remote.response.FilmDto
import com.example.filmsapp.data.remote.response.FilmsDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmsApi {

    @GET("movie/latest")
    suspend fun getLatestFilm(): Response<FilmDto>

    @GET("movie/popular")
    suspend fun getPopularList(@Query("page") page: Int): Response<FilmsDto>

    @GET("movie/top_rated")
    suspend fun getTopRatedList(@Query("page") page: Int): Response<FilmsDto>

    @GET("movie/upcoming")
    suspend fun getUpcomingList(@Query("page") page: Int): Response<FilmsDto>

    @GET("movie/{id}")
    suspend fun getFilm(@Path("id") id: String): FilmDto

    @GET("movie/{id}/images")
    suspend fun getBackdrops(@Path("id") id: String): BackdropsDto
}
