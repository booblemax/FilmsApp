package com.example.filmsapp.base

import com.example.domain.dispatchers.DispatcherProvider

abstract class PagedViewModel(
    dispatcherProvider: DispatcherProvider
) : BaseViewModel(dispatcherProvider) {

    protected var pageNumber = DEFAULT_PAGE_NUMBER

    fun resetPageNumber() {
        pageNumber = DEFAULT_PAGE_NUMBER
    }

    fun incPageNumber() {
        pageNumber++
    }

    fun decPageNumber() {
        pageNumber--
    }

    fun isFirstPageLoading() = pageNumber == FIRST_PAGE_NUMBER

    companion object {
        private const val DEFAULT_PAGE_NUMBER = 0
        private const val FIRST_PAGE_NUMBER = 1
    }
}
