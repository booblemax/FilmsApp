package com.example.filmsapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.filmsapp.BR

abstract class BaseFragment<VM : BaseViewModel, B : ViewDataBinding> : Fragment() {

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

    /**
     * calling in OnCreateView
     */
    private fun init(inflate: LayoutInflater, container: ViewGroup?) {
        binding = DataBindingUtil.inflate(inflate, layoutRes, container, false)
        binding.setVariable(BR.viewModel, viewModel)
        binding.lifecycleOwner = this

        onBackPressedCallback = dispatcher.addCallback(this) {
            onBackPressed()
        }
    }

    /**
     * calling in OnCreateView
     */
    open fun init() {}

    open fun onBackPressed(@IdRes popTo: Int? = null) {
        if (!(popTo?.let { findNavController().popBackStack(it, false) }
            ?: findNavController().popBackStack())) {
            requireActivity().finish()
        } else {
            findNavController().navigateUp()
        }
    }
}
