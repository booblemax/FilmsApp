package com.example.data

import com.example.data.db.FilmsDao
import com.example.data.mapper.toDataModel
import com.example.data.mapper.toModel
import com.example.data.remote.FilmsApi
import com.example.data.remote.response.films.BackdropsDto
import com.example.data.remote.response.films.FilmsDto
import com.example.data.repos.FilmsRepositoryImpl
import com.example.domain.Resource
import com.example.domain.models.FilmModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(minSdk = 28, manifest = Config.NONE)
class FilmsRepositoryImplTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private lateinit var repositoryImpl: FilmsRepositoryImpl
    private lateinit var mockedRepositoryImpl: FilmsRepositoryImpl
    private var needFailure = false
    private val fakeFilmsApi = FakeFilmsApi(latest, populars, toprated, upcoming) { needFailure }
    private val fakeFilmsDao = FakeFilmsDao(favorites.toMutableList())
    private val mockedApi = Mockito.mock(FilmsApi::class.java)
    private val mockedDao = Mockito.mock(FilmsDao::class.java)
    private val pageSize = 2

    @Before
    fun setUp() {
        needFailure = false
        repositoryImpl =
            FilmsRepositoryImpl(
                fakeFilmsApi,
                fakeFilmsDao,
                coroutinesTestRule.testDispatcherProvider,
                pageSize
            )

        mockedRepositoryImpl =
            FilmsRepositoryImpl(
                mockedApi,
                mockedDao,
                coroutinesTestRule.testDispatcherProvider,
                pageSize
            )
    }

    @Test
    fun `when getLatestFilm should return film`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val latestFilmResource = repositoryImpl.getLatestFilm()
            val origin: Resource<FilmModel> = Resource.SUCCESS(latest.toModel())

            assertThat(latestFilmResource, IsEqual(origin))
        }

    @Test
    fun `when getPopularFilm should return first page of film models`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val popularFilmsRes = repositoryImpl.getPopularFilms()
            val origin: Resource<List<FilmModel>> =
                Resource.SUCCESS(populars.map { it.toModel() }.take(pageSize))

            assertThat(popularFilmsRes, IsEqual(origin))
        }

    @Test
    fun `when getPopularFilm should return second page of film models`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val popularFilmsRes = repositoryImpl.getPopularFilms(page = 2)
            val originSecondPage: Resource<List<FilmModel>> =
                Resource.SUCCESS(populars.map { it.toModel() }.subList(2, 4))

            assertThat(popularFilmsRes, IsEqual(originSecondPage))
        }

    @Test
    fun `when getPopularFilm should first call from api second from cache`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            Mockito.`when`(mockedApi.getPopularList(1))
                .thenReturn(Response.success(FilmsDto(1, populars.subList(0, 2), 10, 10)))

            mockedRepositoryImpl.getPopularFilms()
            mockedRepositoryImpl.getPopularFilms()

            Mockito.verify(mockedApi, Mockito.times(1)).getPopularList(1)
        }

    @Test
    fun `when getPopularFilm should first and second call performs by api`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            Mockito.`when`(mockedApi.getPopularList(1))
                .thenReturn(Response.success(FilmsDto(1, populars.subList(0, 2), 10, 10)))

            mockedRepositoryImpl.getPopularFilms()
            mockedRepositoryImpl.getPopularFilms(forceUpdate = true)

            Mockito.verify(mockedApi, Mockito.times(2)).getPopularList(1)
        }

    @Test
    fun `when getPopularFilm should return error`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            needFailure = true

            val popularFilmsRes = repositoryImpl.getPopularFilms()

            assertThat(
                popularFilmsRes,
                IsInstanceOf(Resource.ERROR::class.java)
            )
        }

    // Top Rated

    @Test
    fun `when getTopRatedFilms should return first page of film models`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val topratedFilmsRes = repositoryImpl.getTopRatedFilms()
            val origin: Resource<List<FilmModel>> =
                Resource.SUCCESS(toprated.map { it.toModel() }.take(pageSize))

            assertThat(topratedFilmsRes, IsEqual(origin))
        }

    @Test
    fun `when getTopRatedFilms should return second page of film models`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val topratedFilmsRes = repositoryImpl.getTopRatedFilms(page = 2)
            val originSecondPage: Resource<List<FilmModel>> =
                Resource.SUCCESS(toprated.map { it.toModel() }.subList(2, 4))

            assertThat(topratedFilmsRes, IsEqual(originSecondPage))
        }

    @Test
    fun `when getTopRatedFilms should first call from api second from cache`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            Mockito.`when`(mockedApi.getTopRatedList(1))
                .thenReturn(Response.success(FilmsDto(1, toprated.subList(0, 2), 10, 10)))

            mockedRepositoryImpl.getTopRatedFilms()
            mockedRepositoryImpl.getTopRatedFilms()

            Mockito.verify(mockedApi, Mockito.times(1)).getTopRatedList(1)
        }

    @Test
    fun `when getTopRatedFilms should first and second call performs by api`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            Mockito.`when`(mockedApi.getTopRatedList(1))
                .thenReturn(Response.success(FilmsDto(1, populars.subList(0, 2), 10, 10)))

            mockedRepositoryImpl.getTopRatedFilms()
            mockedRepositoryImpl.getTopRatedFilms(forceUpdate = true)

            Mockito.verify(mockedApi, Mockito.times(2)).getTopRatedList(1)
        }

    @Test
    fun `when getTopRatedFilms should return error`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            needFailure = true

            val topratedFilmsRes = repositoryImpl.getTopRatedFilms()

            assertThat(
                topratedFilmsRes,
                IsInstanceOf(Resource.ERROR::class.java)
            )
        }

    // Upcoming

    @Test
    fun `when getUpcomingFilms should return first page of film models`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val upcomingFilmsRes = repositoryImpl.getUpcomingFilms()
            val origin: Resource<List<FilmModel>> =
                Resource.SUCCESS(upcoming.map { it.toModel() }.take(pageSize))

            assertThat(upcomingFilmsRes, IsEqual(origin))
        }

    @Test
    fun `when getUpcomingFilms should return second page of film models`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val upcomingFilmsRes = repositoryImpl.getUpcomingFilms(page = 2)
            val originSecondPage: Resource<List<FilmModel>> =
                Resource.SUCCESS(upcoming.map { it.toModel() }.subList(2, 4))

            assertThat(upcomingFilmsRes, IsEqual(originSecondPage))
        }

    @Test
    fun `when getUpcomingFilms should first call from api second from cache`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            Mockito.`when`(mockedApi.getUpcomingList(1))
                .thenReturn(Response.success(FilmsDto(1, upcoming.subList(0, 2), 10, 10)))

            mockedRepositoryImpl.getUpcomingFilms()
            mockedRepositoryImpl.getUpcomingFilms()

            Mockito.verify(mockedApi, Mockito.times(1)).getUpcomingList(1)
        }

    @Test
    fun `when getUpcomingFilms should first and second call performs by api`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            Mockito.`when`(mockedApi.getUpcomingList(1))
                .thenReturn(Response.success(FilmsDto(1, upcoming.subList(0, 2), 10, 10)))

            mockedRepositoryImpl.getUpcomingFilms()
            mockedRepositoryImpl.getUpcomingFilms(forceUpdate = true)

            Mockito.verify(mockedApi, Mockito.times(2)).getUpcomingList(1)
        }

    @Test
    fun `when getUpcomingFilms should return error`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            needFailure = true

            val upcomingFilmsRes = repositoryImpl.getUpcomingFilms()

            assertThat(
                upcomingFilmsRes,
                IsInstanceOf(Resource.ERROR::class.java)
            )
        }

    @Test
    fun `given need update true when getFilm should return from api and save in db`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val id = populars[1].id.toString()
            val backdrops = BackdropsDto(listOf(), 0)
            val model = populars[1].toModel(backdrops)
            val checkingFilm = Resource.SUCCESS(model) as Resource<FilmModel>
            val dataModel = model.toDataModel()
            Mockito.`when`(mockedDao.getFilm(anyString())).thenReturn(null)
            Mockito.`when`(mockedApi.getFilm(id))
                .thenReturn(Response.success(populars[1]))
            Mockito.`when`(mockedApi.getBackdrops(anyString()))
                .thenReturn(backdrops)

            val film = mockedRepositoryImpl.getFilm(id, true)

            Mockito.verify(mockedApi, Mockito.times(1)).getFilm(id)
            Mockito.verify(mockedDao, Mockito.times(1)).insert(dataModel)

            assertThat(film, IsEqual(checkingFilm))
        }

    @Test
    fun `given need update false when getFilm should return from dao`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val id = populars[1].id.toString()
            Mockito.`when`(mockedDao.getFilm(anyString()))
                .thenReturn(populars[1].toModel().toDataModel())
            Mockito.`when`(mockedApi.getFilm(id))
                .thenReturn(Response.success(populars[1]))

            val film = mockedRepositoryImpl.getFilm(id, false)

            Mockito.verify(mockedDao, Mockito.times(1)).getFilm(id)

            val checkingFilm = Resource.SUCCESS(populars[1].toModel()) as Resource<FilmModel>
            assertThat(film, IsEqual(checkingFilm))
        }

    @Test
    fun `given need update true, need failure true when getFilm should return error`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            needFailure = true

            val filmRes = repositoryImpl.getFilm("P7kJTqFA", true)

            assertThat(filmRes, IsInstanceOf(Resource.ERROR::class.java))
        }

    @Test
    fun `given film didn't save in db when isFilmStoredInDb should return false`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val result = repositoryImpl.isFilmStoredInDb("123")
            assertThat(result, `is`(false))
        }

    @Test
    fun `given film saved in db when isFilmStoredInDb should return true`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val result = repositoryImpl.isFilmStoredInDb(favorites[0].id)
            assertThat(result, `is`(false))
        }

    @Test
    fun `when saveFilm should film saved in db`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val model = populars.last().toModel()
            val before = (repositoryImpl.getFavouritesFilms() as Resource.SUCCESS).data?.size ?: 0
            repositoryImpl.saveFilm(model)
            val after = (repositoryImpl.getFavouritesFilms() as Resource.SUCCESS).data?.size ?: 0
            val diff = after - before
            assertThat(diff, `is`(1))
        }

    @Test
    fun `when deleteFilm should film removed from db`() =
        coroutinesTestRule.dispatcher.runBlockingTest {
            val model = favorites.last().toModel()
            val before = (repositoryImpl.getFavouritesFilms() as Resource.SUCCESS).data?.size ?: 0
            repositoryImpl.deleteFilm(model)
            val after = (repositoryImpl.getFavouritesFilms() as Resource.SUCCESS).data?.size ?: 0
            val diff = after - before
            assertThat(diff, `is`(-1))
        }
}
