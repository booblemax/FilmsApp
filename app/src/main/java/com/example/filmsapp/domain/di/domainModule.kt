package com.example.filmsapp.domain.di

import com.example.filmsapp.data.repos.FilmsRepositoryImpl
import com.example.filmsapp.data.repos.YoutubeRepositoryImpl
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.domain.repos.YoutubeRepository
import org.koin.dsl.module

val domainModule = module {

    single<FilmsRepository> { FilmsRepositoryImpl(get()) }

    factory<YoutubeRepository> { YoutubeRepositoryImpl() }
}
