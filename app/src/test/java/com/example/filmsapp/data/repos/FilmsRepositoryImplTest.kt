package com.example.filmsapp.data.repos

import android.os.Build
import com.example.filmsapp.CoroutinesTestRule
import com.example.filmsapp.FilmsTestApp
import com.example.filmsapp.data.datasource.FakeFilmsApi
import com.example.filmsapp.data.remote.FilmsApi
import com.example.filmsapp.data.remote.response.films.FilmDto
import com.example.filmsapp.data.remote.response.films.FilmsDto
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.models.FilmModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response

val latest = FilmDto()
val populars = listOf(
    FilmDto(id = 591, title = "g4T095W", backdropPath = "JYDPCx5"),
    FilmDto(id = 212, title = "Q9jK0Cr8", backdropPath = "mw3Qf3l6"),
    FilmDto(id = 978, title = "o10h", backdropPath = "9y8ReDT"),
    FilmDto(id = 811, title = "s6nn", backdropPath = "NY8nDGyP")
)
val toprated = listOf(
    FilmDto(id = 325, title = "1ov", backdropPath = "4zCSA"),
    FilmDto(id = 959, title = "rM6bG", backdropPath = "sMN"),
    FilmDto(id = 830, title = "eycobJY", backdropPath = "T8luQ"),
    FilmDto(id = 221, title = "VoXP2", backdropPath = "5lCNCQ"),
    FilmDto(id = 303, title = "4CYBATJ", backdropPath = "0l9e25")
)
val upcoming = listOf(
    FilmDto(id = 61, title = "FB3Q4X", backdropPath = "Rbw"),
    FilmDto(id = 402, title = "Z9qmruv", backdropPath = "yt3L1"),
    FilmDto(id = 798, title = "6hED2", backdropPath = "t6NVfMSR"),
    FilmDto(id = 632, title = "2rT9", backdropPath = "hM7dO3i"),
    FilmDto(id = 360, title = "rqt7O", backdropPath = "cdtM")
)

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(application = FilmsTestApp::class, sdk = [Build.VERSION_CODES.P])
class FilmsRepositoryImplTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var repositoryImpl: FilmsRepositoryImpl
    private lateinit var mockedRepositoryImpl: FilmsRepositoryImpl
    private var needFailure = false
    private val fakeFilmsApi = FakeFilmsApi(latest, populars, toprated, upcoming) { needFailure }
    private val mockedApi = Mockito.mock(FilmsApi::class.java)
    private val pageSize = 2

    @Before
    fun setUp() {
        needFailure = false
        repositoryImpl =
            FilmsRepositoryImpl(fakeFilmsApi, coroutinesTestRule.testDispatcherProvider, pageSize)

        mockedRepositoryImpl =
            FilmsRepositoryImpl(mockedApi, coroutinesTestRule.testDispatcherProvider, pageSize)
    }

    @Test
    fun `getLatestFilm return film`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            val latestFilmResource = repositoryImpl.getLatestFilm()
            val origin: Resource<FilmModel> = Resource.SUCCESS(latest.toModel())

            assertThat(latestFilmResource, IsEqual(origin))
        }

    @Test
    fun `getPopularFilm return first page of film models`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            val popularFilmsRes = repositoryImpl.getPopularFilms()
            val origin: Resource<List<FilmModel>> =
                Resource.SUCCESS(populars.map { it.toModel() }.take(pageSize))

            assertThat(popularFilmsRes, IsEqual(origin))
        }

    @Test
    fun `getPopularFilm return second page of film models`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            val popularFilmsRes = repositoryImpl.getPopularFilms(page = 2)
            val originSecondPage: Resource<List<FilmModel>> =
                Resource.SUCCESS(populars.map { it.toModel() }.subList(2, 4))

            assertThat(popularFilmsRes, IsEqual(originSecondPage))
        }

    @Test
    fun `getPopularFilm first call from api second from cache`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            Mockito.`when`(mockedApi.getPopularList(1))
                .thenReturn(Response.success(FilmsDto(1, populars.subList(0, 2), 10, 10)))

            mockedRepositoryImpl.getPopularFilms()
            mockedRepositoryImpl.getPopularFilms()

            Mockito.verify(mockedApi, Mockito.times(1)).getPopularList(1)
        }

    @Test
    fun `getPopularFilm first and second call performs by api`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            Mockito.`when`(mockedApi.getPopularList(1))
                .thenReturn(Response.success(FilmsDto(1, populars.subList(0, 2), 10, 10)))

            mockedRepositoryImpl.getPopularFilms()
            mockedRepositoryImpl.getPopularFilms(forceUpdate = true)

            Mockito.verify(mockedApi, Mockito.times(2)).getPopularList(1)
        }

    @Test
    fun `getPopularFilm return error`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            needFailure = true

            val popularFilmsRes = repositoryImpl.getPopularFilms()

            assertThat(
                popularFilmsRes,
                IsInstanceOf(Resource.ERROR::class.java)
            )
        }

    //Top Rated

    @Test
    fun `getTopRatedFilms return first page of film models`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            val topratedFilmsRes = repositoryImpl.getTopRatedFilms()
            val origin: Resource<List<FilmModel>> =
                Resource.SUCCESS(toprated.map { it.toModel() }.take(pageSize))

            assertThat(topratedFilmsRes, IsEqual(origin))
        }

    @Test
    fun `getTopRatedFilms return second page of film models`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            val topratedFilmsRes = repositoryImpl.getTopRatedFilms(page = 2)
            val originSecondPage: Resource<List<FilmModel>> =
                Resource.SUCCESS(toprated.map { it.toModel() }.subList(2, 4))

            assertThat(topratedFilmsRes, IsEqual(originSecondPage))
        }

    @Test
    fun `getTopRatedFilms first call from api second from cache`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            Mockito.`when`(mockedApi.getTopRatedList(1))
                .thenReturn(Response.success(FilmsDto(1, toprated.subList(0, 2), 10, 10)))

            mockedRepositoryImpl.getTopRatedFilms()
            mockedRepositoryImpl.getTopRatedFilms()

            Mockito.verify(mockedApi, Mockito.times(1)).getTopRatedList(1)
        }

    @Test
    fun `getTopRatedFilms first and second call performs by api`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            Mockito.`when`(mockedApi.getTopRatedList(1))
                .thenReturn(Response.success(FilmsDto(1, populars.subList(0, 2), 10, 10)))

            mockedRepositoryImpl.getTopRatedFilms()
            mockedRepositoryImpl.getTopRatedFilms(forceUpdate = true)

            Mockito.verify(mockedApi, Mockito.times(2)).getTopRatedList(1)
        }

    @Test
    fun `getTopRatedFilms return error`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            needFailure = true

            val topratedFilmsRes = repositoryImpl.getTopRatedFilms()

            assertThat(
                topratedFilmsRes,
                IsInstanceOf(Resource.ERROR::class.java)
            )
        }

    //Upcoming

    @Test
    fun `getUpcomingFilms return first page of film models`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            val upcomingFilmsRes = repositoryImpl.getUpcomingFilms()
            val origin: Resource<List<FilmModel>> =
                Resource.SUCCESS(upcoming.map { it.toModel() }.take(pageSize))

            assertThat(upcomingFilmsRes, IsEqual(origin))
        }

    @Test
    fun `getUpcomingFilms return second page of film models`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            val upcomingFilmsRes = repositoryImpl.getUpcomingFilms(page = 2)
            val originSecondPage: Resource<List<FilmModel>> =
                Resource.SUCCESS(upcoming.map { it.toModel() }.subList(2, 4))

            assertThat(upcomingFilmsRes, IsEqual(originSecondPage))
        }

    @Test
    fun `getUpcomingFilms first call from api second from cache`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            Mockito.`when`(mockedApi.getUpcomingList(1))
                .thenReturn(Response.success(FilmsDto(1, upcoming.subList(0, 2), 10, 10)))

            mockedRepositoryImpl.getUpcomingFilms()
            mockedRepositoryImpl.getUpcomingFilms()

            Mockito.verify(mockedApi, Mockito.times(1)).getUpcomingList(1)
        }

    @Test
    fun `getUpcomingFilms first and second call performs by api`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            Mockito.`when`(mockedApi.getUpcomingList(1))
                .thenReturn(Response.success(FilmsDto(1, upcoming.subList(0, 2), 10, 10)))

            mockedRepositoryImpl.getUpcomingFilms()
            mockedRepositoryImpl.getUpcomingFilms(forceUpdate = true)

            Mockito.verify(mockedApi, Mockito.times(2)).getUpcomingList(1)
        }

    @Test
    fun `getUpcomingFilms return error`() =
        coroutinesTestRule.testCoroutineDispatcher.runBlockingTest {
            needFailure = true

            val upcomingFilmsRes = repositoryImpl.getUpcomingFilms()

            assertThat(
                upcomingFilmsRes,
                IsInstanceOf(Resource.ERROR::class.java)
            )
        }
}