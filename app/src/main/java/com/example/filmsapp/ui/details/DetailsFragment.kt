package com.example.filmsapp.ui.details

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.filmsapp.R
import com.example.filmsapp.databinding.DetailsFragmentBinding
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class DetailsFragment : BaseFragment<DetailsViewModel, DetailsFragmentBinding>() {

    override val viewModel: DetailsViewModel by viewModel()
    override val layoutRes: Int = R.layout.details_fragment

    private val onItemClickListener = { position: Int ->
        findNavController().navigate(
            DetailsFragmentDirections.actionDetailsFragmentToImagesCarouselFragment(
                adapter.currentList.map { it.filePath }.toTypedArray(), position)
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
    }

    override fun init() {
        binding.detailsBack.setOnClickListener { onBackPressed() }
        initListener()

        arguments?.let {
            val args = DetailsFragmentArgs.fromBundle(it)
            with(args) {
                binding.posterUrl = posterUrl
                binding.backdropUrl = backdropUrl
            }
        }

        initViewPager()
    }

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

    private fun initListener() {
        viewModel.film.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.SUCCESS) {
                binding.model = resource.data
                adapter.submitList(resource.data?.backdrops?.backdrops)
            }
        }
    }

    override fun onBackPressed() {
        findNavController().navigateUp()
    }
}