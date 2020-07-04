package com.example.filmsapp.ui.details

import android.Manifest
import android.accounts.AccountManager
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.SharedElementCallback
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.filmsapp.R
import com.example.filmsapp.databinding.DetailsFragmentBinding
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.BaseFragment
import com.example.filmsapp.ui.base.common.networkinfo.NetworkStateHolder
import com.example.filmsapp.ui.main.SharedViewModel
import com.example.filmsapp.util.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.util.ExponentialBackOff
import com.google.api.services.youtube.YouTubeScopes
import kotlinx.android.synthetic.main.item_backdrop.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import kotlin.math.abs

class DetailsFragment :
    BaseFragment<DetailsViewModel, DetailsFragmentBinding>(),
    EasyPermissions.PermissionCallbacks {

    private val sharedViewModel: SharedViewModel by sharedViewModel()
    override val viewModel: DetailsViewModel by viewModel()
    override val layoutRes: Int = R.layout.details_fragment

    private lateinit var credential: GoogleAccountCredential
    private val onItemClickListener = { itemView: View, position: Int ->
        sharedViewModel.backdropCarouselPosition = position

        val extras =
            FragmentNavigatorExtras(
                itemView.image_backdrop to itemView.image_backdrop.transitionName
            )
        findNavController().navigate(
            DetailsFragmentDirections.actionDetailsFragmentToImagesCarouselFragment(
                adapter.currentList.map { it.filePath }.toTypedArray(), position
            ), extras
        )
    }

    private val adapter: BackdropsViewPagerAdapter = BackdropsViewPagerAdapter(onItemClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            DetailsFragmentArgs.fromBundle(it).run {
                viewModel.loadFilm(filmId)
            }
        }
        requireActivity().makeStatusBarTransparent()
    }

    override fun init() {
        binding.detailsBack.setOnClickListener { onBackPressed() }
        initObservers()
        initImages()
        initViewPager()
        initCredentials()
        registerInsetsListener()
    }

    private fun initObservers() {
        viewModel.film.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.SUCCESS) {
                binding.model = resource.data
                adapter.submitList(resource.data?.backdrops?.backdrops)
                binding.detailsBackdrops.setCurrentItem(
                    sharedViewModel.backdropCarouselPosition,
                    false
                )
                getResultsFromApi()
            }
        }
        viewModel.youtubeMovieSearchResult.observe(viewLifecycleOwner) { model ->
            binding.detailsPlay.setOnClickListener {
                findNavController().navigate(
                    DetailsFragmentDirections.actionDetailsFragmentToPlayerFragment(model.videoId)
                )
            }
            binding.detailsPlay.visible()
        }
        viewModel.requestAuthorizationPermission.observe(viewLifecycleOwner) {
            startActivityForResult(it.intent, REQUEST_AUTHORIZATION)
        }
        viewModel.displayGpsUnavailable.observe(
            viewLifecycleOwner, ::showGooglePlayServicesAvailabilityErrorDialog
        )
        viewModel.showSnackbar.observe(viewLifecycleOwner) {
            view?.snack(
                getString(
                    R.string.error_occur,
                    it
                )
            )
        }
    }

    private fun initImages() {
        arguments?.let {
            val args = DetailsFragmentArgs.fromBundle(it)
            with(args) {
                binding.posterUrl = posterUrl
                binding.backdropUrl = backdropUrl
            }
        }
    }

    private fun initCredentials() {
        credential = GoogleAccountCredential.usingOAuth2(
            requireContext(), SCOPES
        ).setBackOff(ExponentialBackOff())
    }


    private fun registerInsetsListener() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            binding.detailsBack.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareTransition()
        waitForTransition(binding.detailsBackdrops)
    }

    //region view_pager

    private fun initViewPager() {
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.size_24)
        with(binding.detailsBackdrops) {
            adapter = this@DetailsFragment.adapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            setPageTransformer(CompositePageTransformer().apply {
                addTransformer(getOffsetTransformer(offsetPx))
                addTransformer(this@DetailsFragment::getScaleTransformer)
            })
        }
    }

    private fun getScaleTransformer(page: View, position: Float) {
        page.apply {
            translationY = abs(position) * 20f
            scaleX = 1.1f
        }
    }

    private fun getOffsetTransformer(
        offsetPx: Int
    ): ViewPager2.PageTransformer {
        return ViewPager2.PageTransformer { page, position ->
            val offset = -2.0f * offsetPx.toFloat() * position
            val viewPager = page.parent.parent as ViewPager2
            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewPager) ==
                    ViewCompat.LAYOUT_DIRECTION_RTL
                ) {
                    page.translationX = -offset
                } else {
                    page.translationX = offset
                }
            } else {
                page.translationY = offset
            }
        }
    }

    private fun prepareTransition() {
        setExitSharedElementCallback(object : SharedElementCallback() {

            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val position = sharedViewModel.backdropCarouselPosition
                val viewHolder = (binding.detailsBackdrops[0] as RecyclerView)
                    .findViewHolderForAdapterPosition(position)

                viewHolder?.itemView?.findViewById<AppCompatImageView>(R.id.image_backdrop)?.let {
                    sharedElements?.let { elements ->
                        names?.let { ns ->
                            elements[ns[0]] = it
                        }
                    }
                }
            }
        })
    }

    //endregion

    override fun onBackPressed() {
        sharedViewModel.clearBackdropCarouselPosition()
        requireActivity().makeStatusBarVisible()
        super.onBackPressed()
    }

    //region google_api

    private fun getResultsFromApi() {
        when {
            !isGooglePlayServicesAvailable() -> acquireGooglePlayServices()
            credential.selectedAccountName == null -> chooseAccount()
            !NetworkStateHolder.isConnected -> view?.snack("No network connection available :(")
            else -> binding.model?.let { viewModel.requestFilmTrailer(it.title, credential) }
        }
    }

    private fun isGooglePlayServicesAvailable(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(requireContext())
        return connectionStatusCode == ConnectionResult.SUCCESS
    }

    private fun acquireGooglePlayServices() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(requireContext())
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
        }
    }

    private fun showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode: Int) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        apiAvailability.getErrorDialog(
            requireActivity(),
            connectionStatusCode,
            REQUEST_GOOGLE_PLAY_SERVICES
        ).show()
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private fun chooseAccount() {
        if (EasyPermissions.hasPermissions(
                requireContext(), Manifest.permission.GET_ACCOUNTS
            )
        ) {
            val accountName =
                activity?.getPreferences(Context.MODE_PRIVATE)?.getString(PREF_ACCOUNT_NAME, null)
            if (accountName != null) {
                credential.selectedAccountName = accountName
                getResultsFromApi()
            } else {
                startActivityForResult(
                    credential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER
                )
            }
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs to access your Google account (via contacts)",
                REQUEST_PERMISSION_GET_ACCOUNTS,
                Manifest.permission.GET_ACCOUNTS
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GOOGLE_PLAY_SERVICES -> {
                if (resultCode != RESULT_OK) {
                    view?.snack("This app requires Google Play Services")
                } else {
                    getResultsFromApi()
                }
            }
            REQUEST_ACCOUNT_PICKER -> {
                if (resultCode == RESULT_OK && data != null && data.extras != null) {
                    val accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
                    if (accountName != null) {
                        activity?.getPreferences(Context.MODE_PRIVATE)?.edit {
                            putString(PREF_ACCOUNT_NAME, accountName)
                        }
                        credential.selectedAccountName = accountName
                        getResultsFromApi()
                    }
                }
            }
            REQUEST_AUTHORIZATION -> {
                if (resultCode == RESULT_OK) {
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
        // do nothing
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        // do nothing
    }

    //endregion

    companion object {
        const val REQUEST_ACCOUNT_PICKER = 1000
        const val REQUEST_AUTHORIZATION = 1001
        const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
        const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
        const val PREF_ACCOUNT_NAME = "accountName"

        val SCOPES = listOf(YouTubeScopes.YOUTUBE_READONLY)
    }
}
