package com.example.filmsapp.ui.lists

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.filmsapp.CoroutinesTestRule
import com.example.filmsapp.data.datasource.FakeFilmsApi
import com.example.filmsapp.data.datasource.latest
import com.example.filmsapp.data.datasource.populars
import com.example.filmsapp.data.datasource.toprated
import com.example.filmsapp.data.datasource.upcoming
import com.example.filmsapp.data.repos.FakeFilmsRepository
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.getOrAwaitValue
import com.example.filmsapp.ui.base.models.FilmModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListsViewModelTest {

    @get:Rule
    var instantexecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesDispatcher = CoroutinesTestRule()

    private lateinit var viewModel: ListsViewModel
    private lateinit var repository: FilmsRepository

    private var needFailure: Boolean = false
    private val pageSize = 2

    @Before
    fun setUp() {
        val fakeApi = FakeFilmsApi(latest, populars, toprated, upcoming) { needFailure }
        repository = FakeFilmsRepository(fakeApi)

        viewModel = ListsViewModel(coroutinesDispatcher.testDispatcherProvider, repository)
    }

    @Test
    fun `when loadFilms should return all data`() {
        val originLatest: Resource<FilmModel> = Resource.SUCCESS(latest.toModel())
        val pagePopular: Resource<List<FilmModel>> = Resource.SUCCESS(populars.subList(0, pageSize).map { it.toModel() })
        val pageToprated: Resource<List<FilmModel>> = Resource.SUCCESS(toprated.subList(0, pageSize).map { it.toModel() })
        val pageUpcoming: Resource<List<FilmModel>> = Resource.SUCCESS(upcoming.subList(0, pageSize).map { it.toModel() })
        val viewModel = ListsViewModel(coroutinesDispatcher.testDispatcherProvider, repository)

        val ldLatestFilm = viewModel.latestFilm.getOrAwaitValue()
        val ldPopulars = viewModel.popularFilms.getOrAwaitValue()
        val ldToprated = viewModel.topRatedFilms.getOrAwaitValue()
        val ldUpcoming = viewModel.upcomingFilms.getOrAwaitValue()

        assertThat(ldLatestFilm, IsEqual(originLatest))
        assertThat(pagePopular, IsEqual(ldPopulars))
        assertThat(pageToprated, IsEqual(ldToprated))
        assertThat(pageUpcoming, IsEqual(ldUpcoming))
    }

    @Test
    fun `when loadFilms should return error`() {
        needFailure = true

        val viewModel = ListsViewModel(coroutinesDispatcher.testDispatcherProvider, repository)

        val ldLatestFilm = viewModel.latestFilm.getOrAwaitValue()
        val ldPopulars = viewModel.popularFilms.getOrAwaitValue()
        val ldToprated = viewModel.topRatedFilms.getOrAwaitValue()
        val ldUpcoming = viewModel.upcomingFilms.getOrAwaitValue()

        assertThat(ldLatestFilm, IsInstanceOf(Resource.ERROR::class.java))
        assertThat(ldPopulars, IsInstanceOf(Resource.ERROR::class.java))
        assertThat(ldToprated, IsInstanceOf(Resource.ERROR::class.java))
        assertThat(ldUpcoming, IsInstanceOf(Resource.ERROR::class.java))
    }
}
