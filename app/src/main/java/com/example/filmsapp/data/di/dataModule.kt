package com.example.filmsapp.data.di

import com.example.filmsapp.BuildConfig
import com.example.filmsapp.data.remote.FilmsApi
import com.example.filmsapp.domain.interceptor.AuthHeaderInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

val dataModule = module {

    factory { Gson() }

    single { configClient(get(), get()) }

    // interceptors

    factory {
        HttpLoggingInterceptor(
            object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Timber.tag("OkHttp").d(message)
                }
            }
        ).setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    factory { AuthHeaderInterceptor() }

    // retrofit

    single { configRetrofit(get<OkHttpClient>(), get<Gson>()) }

    factory { createGithubService(get()) }
}

private fun configRetrofit(client: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
    .baseUrl(BuildConfig.BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

private fun configClient(httpInterceptor: HttpLoggingInterceptor, authHeaderInterceptor: AuthHeaderInterceptor): OkHttpClient =
    OkHttpClient.Builder()
        .addInterceptor(httpInterceptor)
        .addInterceptor(authHeaderInterceptor)
        .build()

private fun createGithubService(retrofit: Retrofit) =
    retrofit.create(FilmsApi::class.java)
