package com.example.filmsapp.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.filmsapp.domain.DispatcherProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import timber.log.Timber

abstract class BaseViewModel(protected val dispatcherProvider: DispatcherProvider) : ViewModel() {

    private val _showSnackbar = MutableLiveData<String>()
    val showSnackbar: LiveData<String> = _showSnackbar

    private val job: Job = Job()
    private val defaultExceptionHandler = CoroutineExceptionHandler { _, exception ->
        handleException(exception)
    }
    protected val baseContext = CoroutineScope(job + dispatcherProvider.main() + defaultExceptionHandler)

    protected open fun handleException(exception: Throwable) {
        Timber.e(exception)
    }

    override fun onCleared() {
        baseContext.cancel()
    }

    protected fun postMessage(message: String?) {
        if (message != null)_showSnackbar.value = message
    }
}
