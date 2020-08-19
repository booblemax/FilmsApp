package com.example.filmsapp.ui.splash

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.filmsapp.R
import com.example.filmsapp.data.prefs.SPreferences
import com.example.filmsapp.databinding.SplashFragmentBinding
import com.example.filmsapp.ui.base.BaseFragment
import com.example.filmsapp.ui.splash.GoogleAccountManager.Companion.REQUEST_ACCOUNT_PICKER
import com.example.filmsapp.ui.splash.GoogleAccountManager.Companion.REQUEST_AUTHORIZATION
import com.example.filmsapp.ui.splash.GoogleAccountManager.Companion.REQUEST_GOOGLE_PLAY_SERVICES
import com.example.filmsapp.ui.splash.GoogleAccountManager.Companion.REQUEST_PERMISSION_GET_ACCOUNTS
import com.example.filmsapp.util.snack
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

class SplashFragment : BaseFragment<SplashViewModel, SplashFragmentBinding>(),
    EasyPermissions.PermissionCallbacks {

    override val viewModel: SplashViewModel by viewModel()
    override val layoutRes: Int = R.layout.splash_fragment

    private val prefs: SPreferences by inject()
    private val googleAccountManager: GoogleAccountManager by inject()

    override fun init() {
        viewModel.requestAuthorizationPermission.observe(viewLifecycleOwner) {
            startActivityForResult(it.intent, REQUEST_AUTHORIZATION)
        }
        viewModel.displayGpsUnavailable.observe(
            viewLifecycleOwner, googleAccountManager::showGooglePlayServicesAvailabilityErrorDialog
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getResultsFromApi()
    }

    private fun getResultsFromApi() {
        try {
            when {
                !googleAccountManager.isGooglePlayServicesAvailable() ->
                    googleAccountManager.acquireGooglePlayServices()
                !googleAccountManager.hasAccountName() -> chooseAccount()
                else -> viewModel.runDelayed { navigateToLists() }
            }
        } catch (exception: Exception) {
            viewModel.handleException(exception)
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private fun chooseAccount() {
        if (EasyPermissions.hasPermissions(requireContext(), Manifest.permission.GET_ACCOUNTS)) {
            googleAccountManager.requestOrSetupAccountName(this::getResultsFromApi) {
                startActivityForResult(it, REQUEST_ACCOUNT_PICKER)
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

    private fun navigateToLists() {
        context?.let {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToListsFragment())
        }
    }
}
