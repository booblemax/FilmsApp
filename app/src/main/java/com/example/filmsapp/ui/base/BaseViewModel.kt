package com.example.filmsapp.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import timber.log.Timber

abstract class BaseViewModel : ViewModel() {

    protected val job: Job = Job()

    private val defaultExceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }

    protected val baseContext = CoroutineScope(job + Dispatchers.Main + defaultExceptionHandler)

    protected open fun handleException(exception: Throwable) {
        Timber.e(exception)
    }

    override fun onCleared() {
        baseContext.cancel()
    }
}
