package com.example.filmsapp.domain.di

import com.example.filmsapp.data.repos.FilmsRepositoryImpl
import com.example.filmsapp.data.repos.YoutubeRepositoryImpl
import com.example.filmsapp.domain.DefaultDispatcherProvider
import com.example.filmsapp.domain.DispatcherProvider
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.domain.repos.YoutubeRepository
import com.example.filmsapp.util.ConstUtil
import org.koin.dsl.module

val domainModule = module {

    single<FilmsRepository> { FilmsRepositoryImpl(get(), get(), get(), ConstUtil.PAGE_SIZE) }

    factory<YoutubeRepository> { YoutubeRepositoryImpl() }

    factory<DispatcherProvider> { DefaultDispatcherProvider() }
}
