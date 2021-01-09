package com.example.filmsapp.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseFragment
import com.example.filmsapp.base.Event
import com.example.filmsapp.common.GoogleAccountManager
import com.example.filmsapp.common.SharedViewModel
import com.example.filmsapp.databinding.DetailsFragmentBinding
import com.example.filmsapp.details.transformers.OffsetTransformer
import com.example.filmsapp.details.transformers.ScaleTransformer
import com.example.filmsapp.util.gone
import com.example.filmsapp.util.makeStatusBarTransparent
import com.example.filmsapp.util.makeStatusBarVisible
import com.example.filmsapp.util.setMarginTop
import com.example.filmsapp.util.snack
import com.example.filmsapp.util.visible
import com.example.filmsapp.util.waitForTransition
import kotlinx.android.synthetic.main.item_backdrop.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalCoroutinesApi
class DetailsFragment :
    BaseFragment<DetailsViewModel, DetailsFragmentBinding, DetailsState, DetailsIntents>() {

    private val sharedViewModel: SharedViewModel by sharedViewModel()
    override val viewModel: DetailsViewModel by viewModel()
    override val layoutRes: Int = R.layout.details_fragment

    private val filmId: String
        get() =
            requireArguments().getString(FILM_ID_ARG) ?: throw IllegalArgumentException("No such argument")
    private val posterUrl: String
        get() =
            requireArguments().getString(FILM_ID_ARG) ?: throw IllegalArgumentException("No such argument")
    private val backdropUrl: String
        get() =
            requireArguments().getString(FILM_ID_ARG) ?: throw IllegalArgumentException("No such argument")
    private val isFavorite: Boolean
        get() =
            requireArguments().getBoolean(FILM_ID_ARG)

    private val googleAccountManager: GoogleAccountManager by inject()

    private val adapter: BackdropsViewPagerAdapter = BackdropsViewPagerAdapter { _, position -> openBackdrop(position)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.pushIntent(DetailsIntents.Initial(filmId, isFavorite))
        requireActivity().makeStatusBarTransparent()
    }

    override fun render(state: DetailsState) {
        Timber.i(state.toString())
        with(state) {
            uiEvent?.let { processUiEvent(it) }

            if (loading) {
                binding.detailsProgress.visible()
            } else {
                binding.detailsProgress.gone()
            }

            filmModel?.let {
                binding.model = it
                adapter.submitList(it.backdrops?.backdrops)
                displayFilmViews()
            }

            youtubeModel?.let { model ->
                binding.detailsPlay.visible()
                binding.detailsPlay.setOnClickListener {
                    viewModel.pushIntent(DetailsIntents.OpenPlayer(model.videoId))
                }
            } ?: run {
                filmModel?.title?.let {
                    viewModel.pushIntent(
                        DetailsIntents.LoadTrailer(it, googleAccountManager.getCredential())
                    )
                }
            }

            val imageRes = if (isFavorite) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark
            binding.detailsBookmark.setImageResource(imageRes)

            errorMessage?.let { view?.snack(it) }
            errorRes?.let { view?.snack(it) }
        }
    }

    private fun processUiEvent(event: Event<DetailsUiEvent>) {
        event.getContentIfNotHandled()?.let { uiEvent ->
            when (uiEvent) {
                is DetailsUiEvent.OpenPlayer -> viewModel.openPlayer(uiEvent.videoId)
                is DetailsUiEvent.Back -> onBackPressed()
            }
        }
    }

    override fun init() {
        initListeners()
        initImages()
        initViewPager()
        registerInsetsListener()
    }

    private fun initListeners() {
        binding.detailsBack.setOnClickListener { viewModel.pushIntent(DetailsIntents.Back) }
        binding.detailsBookmark.setOnClickListener {
            viewModel.pushIntent(DetailsIntents.ChangeFavoriteState)
        }
    }

    private fun initImages() {
        binding.posterUrl = posterUrl
        binding.backdropUrl = backdropUrl
    }

    private fun registerInsetsListener() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            binding.detailsBack.setMarginTop(insets.systemWindowInsetTop)
            insets.consumeSystemWindowInsets()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.detailsBackdrops.setCurrentItem(
            sharedViewModel.backdropCarouselPosition,
            false
        )
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

    private fun displayFilmViews() {
        with(binding) {
            detailsTitle.visible()
            detailsGenres.visible()
            detailsFirstDivider.visible()
            detailsYear.visible()
            detailsYear.visible()
            detailsSecondDevider.visible()
            detailsLength.visible()
            detailsAverage.visible()
            detailsOverview.visible()
        }
    }

    private fun openBackdrop(position: Int) {
        sharedViewModel.backdropCarouselPosition = position
        viewModel.openBackdrop(
            adapter.currentList.map { it.filePath }, position
        )
    }

    override fun onBackPressed(popTo: Int?) {
        sharedViewModel.clearBackdropCarouselPosition()
        requireActivity().makeStatusBarVisible()
        super.onBackPressed(popTo)
    }

    companion object {

        const val TAG = "DetailsFragment"
        const val VISIBLE_PAGE_LIMIT = 3

        private const val FILM_ID_ARG = "FILM_ID_ARG"
        private const val POSTER_URL_ARG = "POSTER_URL_ARG"
        private const val BACKDROP_URL_ARG = "BACKDROP_URL_ARG"
        private const val IS_FAVORITE_ARG = "IS_FAVORITE_ARG"

        fun newInstance(filmId: String, posterUrl: String, backdropUrl: String, isFavorite: Boolean): Fragment =
            DetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(FILM_ID_ARG, filmId)
                    putString(POSTER_URL_ARG, posterUrl)
                    putString(BACKDROP_URL_ARG, backdropUrl)
                    putBoolean(IS_FAVORITE_ARG, isFavorite)
                }
            }
    }
}
