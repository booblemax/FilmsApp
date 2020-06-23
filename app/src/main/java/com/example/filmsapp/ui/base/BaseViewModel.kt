package com.example.filmsapp.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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
}
