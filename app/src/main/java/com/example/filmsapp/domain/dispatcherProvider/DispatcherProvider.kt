package com.example.filmsapp.domain.dispatcherProvider

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    fun main(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun unconfined(): CoroutineDispatcher
}
