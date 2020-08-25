package com.example.filmsapp.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.filmsapp.FilmsTestApp
import com.example.filmsapp.data.prefs.SPreferences
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.`is`
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.notNull
import org.mockito.Mockito.times
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(application = FilmsTestApp::class, sdk = [28])
@RunWith(RobolectricTestRunner::class)
class GoogleAccountManagerTest {

    @get:Rule
    var instantexecutorRule = InstantTaskExecutorRule()

    private val apiAvailability = mock(GoogleApiAvailability::class.java)
    private val prefs = SPreferences(ApplicationProvider.getApplicationContext())
    private lateinit var googleAccountManager: GoogleAccountManager
    private var needError = false

    @Before
    fun setUp() {
        needError = false
        googleAccountManager = GoogleAccountManager(
            ApplicationProvider.getApplicationContext(),
            prefs,
            apiAvailability
        )

        Mockito.`when`(apiAvailability.isGooglePlayServicesAvailable(notNull()))
            .thenAnswer { if (!needError) ConnectionResult.SUCCESS else ConnectionResult.CANCELED }

        Mockito.`when`(apiAvailability.isUserResolvableError(ConnectionResult.CANCELED))
            .thenReturn(true)
        Mockito.`when`(apiAvailability.isUserResolvableError(ConnectionResult.SUCCESS))
            .thenReturn(false)
    }

    @Test
    fun `given no need error when isGooglePlayServicesAvailable should return true`() {
        assertThat(googleAccountManager.isGooglePlayServicesAvailable(), `is`(true))
    }

    @Test
    fun `given has need error when isGooglePlayServicesAvailable should return false`() {
        needError = true
        assertThat(googleAccountManager.isGooglePlayServicesAvailable(), `is`(false))
    }

    @Test
    fun `given status code success when acquireGooglePlayServices should do nothing`() {
        googleAccountManager.acquireGooglePlayServices()

        Mockito.verify(apiAvailability, times(0)).getErrorDialog(notNull(), anyInt(), anyInt())
    }
}
