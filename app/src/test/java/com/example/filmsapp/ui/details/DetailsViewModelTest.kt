package com.example.filmsapp.ui.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.filmsapp.CoroutinesTestRule
import com.example.filmsapp.FilmsTestApp
import com.example.filmsapp.R
import com.example.filmsapp.data.datasource.latest
import com.example.filmsapp.data.datasource.youtubeDataModel
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.domain.repos.FilmsRepository
import com.example.filmsapp.domain.repos.YoutubeRepository
import com.example.filmsapp.getOrAwaitValue
import com.example.filmsapp.ui.base.Event
import com.example.filmsapp.ui.base.models.FilmModel
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(application = FilmsTestApp::class, sdk = [28])
@RunWith(AndroidJUnit4::class)
class DetailsViewModelTest {

    @get:Rule
    var instantexecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRule = CoroutinesTestRule()

    private lateinit var viewModel: DetailsViewModel
    private lateinit var repository: FilmsRepository
    private lateinit var youtubeRepository: YoutubeRepository
    private val creds = GoogleAccountCredential(ApplicationProvider.getApplicationContext(), "1234")
    private val connectionStatusCode = 1
    private val errorMessage = "Error"
    private val savedMessageId: Int = R.string.film_saved
    private val removedMessageId: Int = R.string.film_removed
    private val errMessage: Int = R.string.error

    @Before
    fun setUp() {
        repository = Mockito.mock(FilmsRepository::class.java)
        youtubeRepository = Mockito.mock(YoutubeRepository::class.java)

        coroutinesRule.testCoroutineDispatcher.runBlockingTest {
            Mockito.`when`(repository.getFilm(anyString(), anyBoolean())).thenReturn(Resource.SUCCESS(latest.toModel()))
            Mockito.`when`(repository.isFilmStoredInDb(latest.id.toString())).thenReturn(true)
            Mockito.`when`(repository.isFilmStoredInDb(anyString())).thenReturn(false)
            Mockito.`when`(youtubeRepository.getTrailerForFilm("trailer", creds)).thenReturn(youtubeDataModel)
        }

        viewModel = DetailsViewModel(coroutinesRule.testDispatcherProvider, repository, youtubeRepository)
    }

    @Test
    fun `when getFilm should return film model`() {
        viewModel.loadFilm("trailer", false)
        val origin: Resource<FilmModel> = Resource.SUCCESS(latest.toModel())

        val value = viewModel.film.getOrAwaitValue()

        assertThat(value, IsEqual(origin))
    }

    @Test
    fun `when requestFilmTrailer should return youtube film model`() {
        viewModel.requestFilmTrailer("trailer", creds)

        val value = viewModel.youtubeMovieSearchResult.getOrAwaitValue()

        assertThat(value, IsEqual(youtubeDataModel))
    }

    @Test
    fun `given film model don't place in db, bookmark false when storeFilm should pass success message`() =
        coroutinesRule.testCoroutineDispatcher.runBlockingTest {
            viewModel.loadFilm("any", false)

            viewModel.favoriteClicked()
            val message = viewModel.showSnackbar.getOrAwaitValue()

            Mockito.verify(repository, Mockito.times(1)).saveFilm(latest.toModel())
            assertThat(message, IsEqual(Event(savedMessageId)))
        }

    @Test
    fun `given film model don't place in db, bookmark true when removeFilm should pass success message`() =
        coroutinesRule.testCoroutineDispatcher.runBlockingTest {
            viewModel.loadFilm("any", true)

            viewModel.favoriteClicked()
            val message = viewModel.showSnackbar.getOrAwaitValue()

            Mockito.verify(repository, Mockito.times(1)).deleteFilm(latest.toModel())
            assertThat(message, IsEqual(Event(removedMessageId)))
        }
}
