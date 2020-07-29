package com.example.filmsapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.filmsapp.CoroutinesTestRule
import com.example.filmsapp.data.datasource.populars
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.getOrAwaitValue
import com.example.filmsapp.ui.base.models.FilmModel
import com.example.filmsapp.ui.base.models.ListType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var instantexecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesDispatcher = CoroutinesTestRule()

    private lateinit var viewModel: MainViewModel
    private lateinit var repository: FilmsRepository

    private var needFailure: Boolean = false
    private val pageSize = 2

    @Before
    fun setUp() {
        repository = Mockito.mock(FilmsRepository::class.java)

        viewModel = MainViewModel(coroutinesDispatcher.testDispatcherProvider, repository)
    }

    @Test
    fun `loadFilms type Popular return films`() {
        viewModel.listType = ListType.POPULAR
        val origin: Resource<List<FilmModel>> = Resource.SUCCESS(populars.take(2).map { it.toModel() })

        viewModel.loadFilms()
        val value = viewModel.films.getOrAwaitValue()

        coroutinesDispatcher.testCoroutineDispatcher.runBlockingTest {
            Mockito.verify(repository).getPopularFilms(1, false)
        }
        assertThat(value, IsEqual(origin))
    }

    @Test
    fun `loadFilms type Toprated return films`() {

    }

    @Test
    fun `loadFilms type Upcoming return films`() {

    }

}