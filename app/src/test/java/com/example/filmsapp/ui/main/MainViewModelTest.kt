package com.example.filmsapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.mapper.toModel
import com.example.domain.Resource
import com.example.domain.models.FilmModel
import com.example.domain.repos.FilmsRepository
import com.example.filmsapp.CoroutinesTestRule
import com.example.filmsapp.data.datasource.populars
import com.example.filmsapp.data.datasource.toprated
import com.example.filmsapp.data.datasource.upcoming
import com.example.filmsapp.getOrAwaitValue
import com.example.filmsapp.ui.base.Event
import com.example.filmsapp.ui.base.ListType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
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

    @Before
    fun setUp() {
        repository = Mockito.mock(FilmsRepository::class.java)
        coroutinesDispatcher.testCoroutineDispatcher.runBlockingTest {
            Mockito.`when`(repository.getPopularFilms(1)).thenReturn(
                Resource.SUCCESS(
                    populars.take(2).map { it.toModel() }
                )
            )
            Mockito.`when`(repository.getPopularFilms(2)).thenReturn(
                Resource.SUCCESS(
                    populars.subList(2, 4).map { it.toModel() }
                )
            )
            Mockito.`when`(repository.getTopRatedFilms(1)).thenReturn(
                Resource.SUCCESS(
                    toprated.take(2).map { it.toModel() }
                )
            )
            Mockito.`when`(repository.getTopRatedFilms(2)).thenReturn(
                Resource.SUCCESS(
                    toprated.subList(2, 4).map { it.toModel() }
                )
            )
            Mockito.`when`(repository.getUpcomingFilms(1)).thenReturn(
                Resource.SUCCESS(
                    upcoming.take(2).map { it.toModel() }
                )
            )
            Mockito.`when`(repository.getUpcomingFilms(2)).thenReturn(
                Resource.SUCCESS(
                    upcoming.subList(2, 4).map { it.toModel() }
                )
            )
        }

        viewModel = MainViewModel(coroutinesDispatcher.testDispatcherProvider, repository)
    }

    @Test
    fun `given type popular when loadFilms should return films`() {
        viewModel.listType = ListType.POPULAR
        val origin: Resource<List<FilmModel>> = Resource.SUCCESS(populars.take(2).map { it.toModel() })

        viewModel.loadFilms()
        val value = viewModel.films.getOrAwaitValue()

        coroutinesDispatcher.testCoroutineDispatcher.runBlockingTest {
            Mockito.verify(repository, Mockito.times(1)).getPopularFilms(1, false)
        }
        assertThat(value, IsEqual(origin))
    }

    @Test
    fun `given type popular and page 2 when twice loadFilms should call getPopularFilms with page 2`() {
        viewModel.listType = ListType.POPULAR

        viewModel.loadFilms()
        viewModel.loadFilms()

        coroutinesDispatcher.testCoroutineDispatcher.runBlockingTest {
            Mockito.verify(repository, Mockito.times(1)).getPopularFilms(2, false)
        }
    }

    @Test
    fun `given type toprated when loadFilms should return films`() {
        viewModel.listType = ListType.TOP_RATED
        val origin: Resource<List<FilmModel>> = Resource.SUCCESS(toprated.take(2).map { it.toModel() })

        viewModel.loadFilms()
        val value = viewModel.films.getOrAwaitValue()

        coroutinesDispatcher.testCoroutineDispatcher.runBlockingTest {
            Mockito.verify(repository, Mockito.times(1)).getTopRatedFilms(1, false)
        }
        assertThat(value, IsEqual(origin))
    }

    @Test
    fun `given type toprated and page 2 when twice loadFilms should call getTopRatedFilms with page 2`() {
        viewModel.listType = ListType.TOP_RATED

        viewModel.loadFilms()
        viewModel.loadFilms()

        coroutinesDispatcher.testCoroutineDispatcher.runBlockingTest {
            Mockito.verify(repository, Mockito.times(1)).getTopRatedFilms(2, false)
        }
    }

    @Test
    fun `given type upcoming when loadFilms should return films`() {
        viewModel.listType = ListType.UPCOMING
        val origin: Resource<List<FilmModel>> = Resource.SUCCESS(upcoming.take(2).map { it.toModel() })

        viewModel.loadFilms()
        val value = viewModel.films.getOrAwaitValue()

        coroutinesDispatcher.testCoroutineDispatcher.runBlockingTest {
            Mockito.verify(repository, Mockito.times(1)).getUpcomingFilms(1, false)
        }
        assertThat(value, IsEqual(origin))
    }

    @Test
    fun `given type upcoming and page 2 when twice loadFilms should call getUpcomingFilms with page 2`() {
        viewModel.listType = ListType.UPCOMING

        viewModel.loadFilms()
        viewModel.loadFilms()

        coroutinesDispatcher.testCoroutineDispatcher.runBlockingTest {
            Mockito.verify(repository, Mockito.times(1)).getUpcomingFilms(2, false)
        }
    }

    @Test
    fun `given page 0 when incPage should pageNumber not be first page`() {
        viewModel.incPageNumber()

        assertThat(viewModel.isFirstPageLoading(), `is`(false))
    }

    @Test
    fun `given page 1 when decPage should pageNumber be first page`() {
        viewModel.incPageNumber()
        viewModel.decPageNumber()

        assertThat(viewModel.isFirstPageLoading(), `is`(true))
    }

    @Test
    fun `given page 3 when resetPageIndex should pageNumber be first page`() {
        viewModel.incPageNumber()
        viewModel.incPageNumber()
        viewModel.incPageNumber()

        viewModel.resetPageNumber()

        assertThat(viewModel.isFirstPageLoading(), `is`(true))
    }

    @Test
    fun `when fetchedDataIsEmpty pass true should pass Event with true`() {
        viewModel.fetchedDataIsEmpty(true)

        val event = viewModel.emptyData.getOrAwaitValue()

        assertThat(event, IsEqual(Event(true)))
    }

    @Test
    fun `when fetchedDataIsEmpty pass false should pass Event with true`() {
        viewModel.fetchedDataIsEmpty(false)

        val event = viewModel.emptyData.getOrAwaitValue()

        assertThat(event, IsEqual(Event(false)))
    }
}
