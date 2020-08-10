package com.example.filmsapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.filmsapp.domain.DispatcherProvider
import com.example.filmsapp.ui.base.BaseViewModel
import com.example.filmsapp.ui.base.Event

class SearchViewModel(
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    private var pageNumber = FIRST_PAGE_NUMBER

    private val _emptyData = MutableLiveData<Event<Boolean>>()
    val emptyData: LiveData<Event<Boolean>> get() = _emptyData

    var lastKnownPosition = -1

    fun fetchedDataIsEmpty(isEmpty: Boolean) {
        _emptyData.value = Event(isEmpty)
    }

    fun resetPageNumber() {
        pageNumber = FIRST_PAGE_NUMBER
    }

    fun incPageNumber() {
        pageNumber++
    }

    fun decPageNumber() {
        pageNumber--
    }

    companion object {
        private const val FIRST_PAGE_NUMBER = 0
    }
}
