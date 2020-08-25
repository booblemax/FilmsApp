package com.example.filmsapp.details

import android.Manifest
import android.accounts.AccountManager
import android.annotation.SuppressLint
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
import com.example.domain.Resource
import com.example.filmsapp.R
import com.example.filmsapp.databinding.DetailsFragmentBinding
import com.example.filmsapp.base.BaseFragment
import com.example.filmsapp.base.EventObserver
import com.example.filmsapp.base.common.networkinfo.NetworkStateHolder
import com.example.filmsapp.common.GoogleAccountManager
import com.example.filmsapp.common.SharedViewModel
import com.example.filmsapp.details.transformers.OffsetTransformer
import com.example.filmsapp.details.transformers.ScaleTransformer
import com.example.filmsapp.util.GSUtils
import com.example.filmsapp.util.makeStatusBarTransparent
import com.example.filmsapp.util.makeStatusBarVisible
import com.example.filmsapp.util.setMarginTop
import com.example.filmsapp.util.snack
import com.example.filmsapp.util.visible
import com.example.filmsapp.util.waitForTransition
import kotlinx.android.synthetic.main.item_backdrop.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class DetailsFragment :
    BaseFragment<DetailsViewModel, DetailsFragmentBinding>(),
    EasyPermissions.PermissionCallbacks {

    private val sharedViewModel: SharedViewModel by sharedViewModel()
    override val viewModel: DetailsViewModel by viewModel()
    override val layoutRes: Int = R.layout.details_fragment

    private lateinit var googleAccountManager: GoogleAccountManager
    private val onItemClickListener = { itemView: View, position: Int ->
        sharedViewModel.backdropCarouselPosition = position

        val extras =
            FragmentNavigatorExtras(
                itemView.image_backdrop to itemView.image_backdrop.transitionName
            )
        findNavController().navigate(
            DetailsFragmentDirections.actionDetailsFragmentToImagesCarouselFragment(
                adapter.currentList.map { it.filePath }.toTypedArray(), position
            ),
            extras
        )
    }

    private val adapter: BackdropsViewPagerAdapter = BackdropsViewPagerAdapter(onItemClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            DetailsFragmentArgs.fromBundle(it).run {
                viewModel.loadFilm(filmId, isFavorites)
            }
        }
        requireActivity().makeStatusBarTransparent()
        googleAccountManager = GoogleAccountManager(requireContext())
    }

    override fun init() {
        initListeners()
        initObservers()
        initImages()
        initViewPager()
        registerInsetsListener()
    }

    private fun initListeners() {
        binding.detailsBack.setOnClickListener { onBackPressed() }
        binding.detailsBookmark.setOnClickListener {
            viewModel.favoriteClicked()
        }
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
            viewLifecycleOwner
        ) {
            GSUtils.showGooglePlayServicesAvailabilityErrorDialog(requireContext(), it)
        }
        viewModel.showSnackbar.observe(viewLifecycleOwner) { event ->
            view?.snack(getString(event.getContentIfNotHandled() ?: R.string.error))
        }
        viewModel.isFavorites.observe(viewLifecycleOwner, EventObserver {
            val imageRes = if (it) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark
            binding.detailsBookmark.setImageResource(imageRes)
        })
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

    @SuppressLint("WrongConstant")
    private fun initViewPager() {
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.size_24)
        with(binding.detailsBackdrops) {
            adapter = this@DetailsFragment.adapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = VISIBLE_PAGE_LIMIT
            setPageTransformer(
                CompositePageTransformer().apply {
                    addTransformer(OffsetTransformer(offsetPx.toFloat()))
                    addTransformer(ScaleTransformer())
                }
            )
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

    override fun onBackPressed(popTo: Int?) {
        sharedViewModel.clearBackdropCarouselPosition()
        requireActivity().makeStatusBarVisible()
        super.onBackPressed(popTo)
    }

    private fun getResultsFromApi() {
        when {
            !GSUtils.isGooglePlayServicesAvailable(requireContext()) ->
                GSUtils.acquireGooglePlayServices(requireContext())
            !googleAccountManager.hasAccountName() -> chooseAccount()
            !NetworkStateHolder.isConnected -> view?.snack("No network connection available :(")
            else -> binding.model?.let {
                viewModel.requestFilmTrailer(it.title, googleAccountManager.getCredential())
            }
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private fun chooseAccount() {
        if (EasyPermissions.hasPermissions(
                requireContext(), Manifest.permission.GET_ACCOUNTS
            )
        ) {
            val accountName = requireActivity().getPreferences(Context.MODE_PRIVATE)
                ?.getString(PREF_ACCOUNT_NAME, null)
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
                        googleAccountManager.setAccountName(accountName)
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

    companion object {
        const val REQUEST_ACCOUNT_PICKER = 1000
        const val REQUEST_AUTHORIZATION = 1001
        const val REQUEST_GOOGLE_PLAY_SERVICES = 1002
        const val REQUEST_PERMISSION_GET_ACCOUNTS = 1003
        const val PREF_ACCOUNT_NAME = "accountName"
        const val VISIBLE_PAGE_LIMIT = 3
    }
}
