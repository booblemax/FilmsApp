package com.example.filmsapp.splash

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseFragment
import com.example.filmsapp.base.prefs.SPreferences
import com.example.filmsapp.common.GoogleAccountManager
import com.example.filmsapp.common.GoogleAccountManager.Companion.REQUEST_ACCOUNT_PICKER
import com.example.filmsapp.common.GoogleAccountManager.Companion.REQUEST_AUTHORIZATION
import com.example.filmsapp.common.GoogleAccountManager.Companion.REQUEST_GOOGLE_PLAY_SERVICES
import com.example.filmsapp.common.GoogleAccountManager.Companion.REQUEST_PERMISSION_GET_ACCOUNTS
import com.example.filmsapp.databinding.SplashFragmentBinding
import com.example.filmsapp.util.GSUtils
import com.example.filmsapp.util.snack
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

@ExperimentalCoroutinesApi
class SplashFragment :
    BaseFragment<SplashViewModel, SplashFragmentBinding, SplashState, SplashIntents>(),
    EasyPermissions.PermissionCallbacks {

    override val viewModel: SplashViewModel by viewModel()
    override val layoutRes: Int = R.layout.splash_fragment

    private val prefs: SPreferences by inject()
    private val googleAccountManager: GoogleAccountManager by inject()

    override fun render(state: SplashState) {
        Timber.i(state.toString())
        with(state) {
            try {
                when {
                    error is GooglePlayServicesAvailabilityIOException ->
                        GSUtils.showGooglePlayServicesAvailabilityErrorDialog(
                            requireContext(),
                            error.connectionStatusCode
                        )
                    error is UserRecoverableAuthIOException ->
                        startActivityForResult(
                            error.intent,
                            REQUEST_AUTHORIZATION
                        )
                    error != null -> Timber.e(getString(R.string.error_occur, state.toString()))
                }
                errorMessage?.let { view?.snack(it) }

                if (loading) getResultsFromApi()
                else viewModel.runDelayed(action = viewModel::openLists)
            } catch (exception: Exception) {
                viewModel.pushIntent(SplashIntents.Exception(exception))
            }
        }
    }

    override fun init() { /* nothing to init in ui */ }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getResultsFromApi()
    }

    private fun getResultsFromApi() {
        try {
            when {
                !GSUtils.isGooglePlayServicesAvailable(requireContext()) ->
                    GSUtils.acquireGooglePlayServices(requireContext())
                !googleAccountManager.hasAccountName() ->
                    chooseAccount()
                else -> viewModel.pushIntent(SplashIntents.Loaded)
            }
        } catch (exception: Exception) {
            viewModel.pushIntent(SplashIntents.Exception(exception))
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private fun chooseAccount() {
        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.GET_ACCOUNTS)) {
            val accountName = prefs.getAccountName()
            if (accountName != null) {
                googleAccountManager.setAccountName(accountName)
                getResultsFromApi()
            } else {
                startActivityForResult(
                    googleAccountManager.getChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER
                )
            }
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.dialog_permission_accounts),
                REQUEST_PERMISSION_GET_ACCOUNTS,
                Manifest.permission.GET_ACCOUNTS
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_GOOGLE_PLAY_SERVICES -> {
                if (resultCode != Activity.RESULT_OK) {
                    view?.snack(R.string.error_require_gpservices)
                } else {
                    getResultsFromApi()
                }
            }
            REQUEST_ACCOUNT_PICKER -> {
                if (resultCode == Activity.RESULT_OK && data != null && data.extras != null) {
                    val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                    if (accountName != null) {
                        prefs.saveAccountName(accountName)
                        googleAccountManager.setAccountName(accountName)
                        getResultsFromApi()
                    }
                }
            }
            REQUEST_AUTHORIZATION -> {
                if (resultCode == Activity.RESULT_OK) {
                    getResultsFromApi()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(
            requestCode, permissions, grantResults, this
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Timber.e("OnPermissionDenied")
        // do nothing
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Timber.e("onPermissionsGranted")
        // do nothing
    }

    companion object {
        const val TAG = "SplashFragment"
    }
}
