package com.example.filmsapp.imagesCarousel

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.SharedElementCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseFragment
import com.example.filmsapp.common.SharedViewModel
import com.example.filmsapp.databinding.ImageCarouselFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.collections.set

@ExperimentalCoroutinesApi
class ImagesCarouselFragment :
    BaseFragment<ImagesCarouselViewModel, ImageCarouselFragmentBinding, ImageCarouselState, ImageCarouselIntents>() {

    private val sharedViewModel: SharedViewModel by sharedViewModel()
    override val viewModel: ImagesCarouselViewModel by viewModel()
    override val layoutRes: Int = R.layout.image_carousel_fragment

    private val carouselAdapter = ImagesCarouselAdapter { startPostponedEnterTransition() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_carousel_transition)

        prepareTransition()
    }

    override fun render(state: ImageCarouselState) {
        Timber.i(state.toString())
        with(state) {
            carouselAdapter.submitList(urls)
            binding.imagesCarousel.scrollToPosition(position)
        }
    }

    override fun init() {
        with(binding.imagesCarousel) {
            layoutManager =
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = carouselAdapter
            PagerSnapHelper().apply { attachToRecyclerView(this@with) }
            postponeEnterTransition()
        }
        ImagesCarouselFragmentArgs.fromBundle(requireArguments()).let {
            viewModel.pushIntent(ImageCarouselIntents.Initial(it.urls.asList(), it.position))
        }
    }

    override fun onBackPressed(popTo: Int?) {
        storeImagePosition()
        super.onBackPressed(popTo)
    }

    private fun storeImagePosition() {
        val layoutManager = binding.imagesCarousel.layoutManager as LinearLayoutManager
        val position = layoutManager.findFirstCompletelyVisibleItemPosition()
        sharedViewModel.backdropCarouselPosition = position
    }

    private fun prepareTransition() {
        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val position = sharedViewModel.backdropCarouselPosition
                val viewHolder = binding.imagesCarousel.findViewHolderForAdapterPosition(position)

                viewHolder?.itemView?.findViewById<AppCompatImageView>(R.id.image_carousel)?.let {
                    sharedElements?.let { elements ->
                        names?.let { ns ->
                            elements[ns[0]] = it
                        }
                    }
                }
            }
        })
    }
}
