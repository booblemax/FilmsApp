package com.example.data.di

import com.example.data.repos.FilmsRepositoryImpl
import com.example.data.repos.YoutubeRepositoryImpl
import com.example.domain.ConstUtil
import com.example.domain.repos.FilmsRepository
import com.example.domain.repos.YoutubeRepository
import org.koin.dsl.module

val dataModule = module {

    single<FilmsRepository> { FilmsRepositoryImpl(get(), get(), get(), ConstUtil.PAGE_SIZE) }

    factory<YoutubeRepository> { YoutubeRepositoryImpl() }
} + dbModule + netModule
