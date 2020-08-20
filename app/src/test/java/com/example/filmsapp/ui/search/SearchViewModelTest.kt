package com.example.filmsapp.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.filmsapp.CoroutinesTestRule
import com.example.filmsapp.FilmsTestApp
import com.example.filmsapp.data.datasource.populars
import com.example.filmsapp.data.datasource.upcoming
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.getOrAwaitValue
import com.example.filmsapp.ui.base.Event
import com.example.filmsapp.ui.base.models.FilmModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@FlowPreview
@ExperimentalCoroutinesApi
@Config(application = FilmsTestApp::class, sdk = [28])
@RunWith(RobolectricTestRunner::class)
class SearchViewModelTest {

    @get:Rule
    var instantexecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesDispatcher = CoroutinesTestRule()

    private lateinit var viewModel: SearchViewModel
    private lateinit var repository: FilmsRepository

    private val query1 = "1"
    private val query2 = "2"

    @Before
    fun setUp() {
        repository = Mockito.mock(FilmsRepository::class.java)
        coroutinesDispatcher.dispatcher.runBlockingTest {
            Mockito.`when`(repository.searchFilms(query1, 1, false)).thenReturn(
                Resource.SUCCESS(
                    populars.take(2).map { it.toModel() }
                )
            )
            Mockito.`when`(repository.searchFilms(query1, 2, false)).thenReturn(
                Resource.SUCCESS(
                    populars.subList(2, 4).map { it.toModel() }
                )
            )
            Mockito.`when`(repository.searchFilms(query2, 1, false)).thenReturn(
                Resource.SUCCESS(
                    upcoming.take(2).map { it.toModel() }
                )
            )
            Mockito.`when`(repository.searchFilms(query2, 2, false)).thenReturn(
                Resource.SUCCESS(
                    upcoming.subList(2, 4).map { it.toModel() }
                )
            )
        }

        viewModel = SearchViewModel(coroutinesDispatcher.testDispatcherProvider, repository)
    }

    @Test
    fun `given query1 page 1 when callLoad should return first page populars`() {
        val origin: Resource<List<FilmModel>> =
            Resource.SUCCESS(populars.take(2).map { it.toModel() })

        viewModel.loadFilms(query1)
        val result = viewModel.films.getOrAwaitValue()

        coroutinesDispatcher.dispatcher.runBlockingTest {
            Mockito.verify(repository, times(1)).searchFilms(query1, 1, false)
        }
        assertThat(origin, IsEqual(result))
    }

    @Test
    fun `given query1 when callLoad twice should return first and second page populars`() {
        val origin: Resource<List<FilmModel>> =
            Resource.SUCCESS(populars.subList(2, 4).map { it.toModel() })

        viewModel.loadFilms(query1)
        viewModel.loadFilms(query1)
        val result = viewModel.films.getOrAwaitValue()

        coroutinesDispatcher.dispatcher.runBlockingTest {
            Mockito.verify(repository, times(1)).searchFilms(query1, 1, false)
            Mockito.verify(repository, times(1)).searchFilms(query1, 2, false)
        }
        assertThat(origin, IsEqual(result))
    }

    @Test
    fun `given page 0 when incPage should pageNumber not be first page`() {
        coroutinesDispatcher.dispatcher.runBlockingTest {
            viewModel.incPageNumber()

            assertThat(viewModel.isFirstPageLoading(), Is.`is`(true))
        }
    }

    @Test
    fun `given page 1 when decPage should pageNumber be first page`() {
        coroutinesDispatcher.dispatcher.runBlockingTest {
            viewModel.incPageNumber()
            viewModel.decPageNumber()

            assertThat(viewModel.isFirstPageLoading(), Is.`is`(false))
        }
    }

    @Test
    fun `given page 3 when resetPageIndex should pageNumber be first page`() {
        coroutinesDispatcher.dispatcher.runBlockingTest {
            viewModel.incPageNumber()
            viewModel.incPageNumber()
            viewModel.incPageNumber()

            viewModel.resetPageNumber()

            assertThat(viewModel.isFirstPageLoading(), Is.`is`(false))
        }
    }

    @Test
    fun `when fetchedDataIsEmpty pass true should pass Event with true`() {
        coroutinesDispatcher.dispatcher.runBlockingTest {
            viewModel.incPageNumber()
            viewModel.fetchedDataIsEmpty(true)

            val event = viewModel.emptyData.getOrAwaitValue()

            assertThat(event, IsEqual(Event(true)))
        }
    }

    @Test
    fun `when fetchedDataIsEmpty pass false should pass Event with true`() {
        coroutinesDispatcher.dispatcher.runBlockingTest {
            viewModel.incPageNumber()
            viewModel.fetchedDataIsEmpty(false)

            val event = viewModel.emptyData.getOrAwaitValue()

            assertThat(event, IsEqual(Event(false)))
        }
    }
}
