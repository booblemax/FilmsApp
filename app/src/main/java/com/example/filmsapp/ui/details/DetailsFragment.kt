package com.example.filmsapp.ui.details

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
import androidx.viewpager2.widget.ViewPager2
import com.example.filmsapp.R
import com.example.filmsapp.databinding.DetailsFragmentBinding
import com.example.filmsapp.domain.Resource
import com.example.filmsapp.ui.base.BaseFragment
import com.example.filmsapp.ui.main.SharedViewModel
import com.example.filmsapp.util.waitForTransition
import kotlinx.android.synthetic.main.item_backdrop.view.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class DetailsFragment : BaseFragment<DetailsViewModel, DetailsFragmentBinding>() {

    private val sharedViewModel: SharedViewModel by sharedViewModel()
    override val viewModel: DetailsViewModel by viewModel()
    override val layoutRes: Int = R.layout.details_fragment

    private val onItemClickListener = { itemView: View, position: Int ->
        sharedViewModel.backdropCarouselPosition = position

        val extras =
            FragmentNavigatorExtras(
                itemView.image_backdrop to itemView.image_backdrop.transitionName)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prepareTransition()
        waitForTransition(binding.detailsBackdrops)
    }

    private fun initListener() {
        viewModel.film.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.SUCCESS) {
                binding.model = resource.data
                adapter.submitList(resource.data?.backdrops?.backdrops)
                binding.detailsBackdrops.setCurrentItem(sharedViewModel.backdropCarouselPosition, false)
            }
        }
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

    override fun onBackPressed() {
        findNavController().navigateUp()
    }
}