package com.example.filmsapp.domain.di

import com.example.filmsapp.domain.FilmsRepository
import org.koin.dsl.module

val domainModule = module {

    factory { FilmsRepository(get()) }

}

