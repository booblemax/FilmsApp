package com.example.domain.di

import com.example.domain.dispatchers.DefaultDispatcherProvider
import com.example.domain.dispatchers.DispatcherProvider
import org.koin.dsl.module

val domainModule = module {

    factory<DispatcherProvider> { DefaultDispatcherProvider() }
}
