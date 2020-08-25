package com.example.filmsapp.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.SharedElementCallback
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import com.example.domain.Resource
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseFragment
import com.example.filmsapp.base.EventObserver
import com.example.filmsapp.common.GoogleAccountManager
import com.example.filmsapp.common.SharedViewModel
import com.example.filmsapp.databinding.DetailsFragmentBinding
import com.example.filmsapp.details.transformers.OffsetTransformer
import com.example.filmsapp.details.transformers.ScaleTransformer
import com.example.filmsapp.util.makeStatusBarTransparent
import com.example.filmsapp.util.makeStatusBarVisible
import com.example.filmsapp.util.setMarginTop
import com.example.filmsapp.util.snack
import com.example.filmsapp.util.visible
import com.example.filmsapp.util.waitForTransition
import kotlinx.android.synthetic.main.item_backdrop.view.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment :
    BaseFragment<DetailsViewModel, DetailsFragmentBinding>() {

    private val sharedViewModel: SharedViewModel by sharedViewModel()
    override val viewModel: DetailsViewModel by viewModel()
    override val layoutRes: Int = R.layout.details_fragment
    private val googleAccountManager: GoogleAccountManager by inject()

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
                resource.data?.let {
                    binding.model = it
                    adapter.submitList(it.backdrops?.backdrops)
                    viewModel.requestFilmTrailer(it.title, googleAccountManager.getCredential())
                }
                binding.detailsBackdrops.setCurrentItem(
                    sharedViewModel.backdropCarouselPosition,
                    false
                )
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

    companion object {
        const val VISIBLE_PAGE_LIMIT = 3
    }
}
