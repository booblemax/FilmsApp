package com.example.domain.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    fun main(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
    fun io(): CoroutineDispatcher
    fun unconfined(): CoroutineDispatcher
}
