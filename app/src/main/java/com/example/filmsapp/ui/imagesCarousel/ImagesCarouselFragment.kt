package com.example.filmsapp.ui.imagesCarousel

import androidx.navigation.fragment.findNavController
import com.example.filmsapp.R
import com.example.filmsapp.databinding.ImageCarouselFragmentBinding
import com.example.filmsapp.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ImagesCarouselFragment : BaseFragment<ImagesCarouselViewModel, ImageCarouselFragmentBinding>() {

    override val viewModel: ImagesCarouselViewModel by viewModel()
    override val layoutRes: Int = R.layout.image_carousel_fragment

    private val adapter = ImagesCarouselAdapter()

    override fun init() {
        binding.imagesViewPager.adapter = adapter
        ImagesCarouselFragmentArgs.fromBundle(requireArguments()).let {
            adapter.submitList(it.urls.asList())
            binding.imagesViewPager.setCurrentItem(it.position, false)
        }
    }

    override fun onBackPressed() {
        findNavController().navigateUp()
    }
}