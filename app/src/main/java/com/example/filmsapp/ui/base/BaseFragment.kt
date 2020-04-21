package com.example.filmsapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<VM : BaseViewModel, B: ViewDataBinding> : Fragment() {

    protected abstract val viewModel: VM

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected lateinit var binding: B

    private val dispatcher by lazy { requireActivity().onBackPressedDispatcher }

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init(inflater, container)
        init()
        return binding.root
    }

    private fun init(inflate: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflate, layoutRes, container, false)
        binding.lifecycleOwner = this

        onBackPressedCallback = dispatcher.addCallback(this) {
            onBackPressed()
        }
    }

    open fun init() {}

    open fun onBackPressed() { }

}