package com.example.filmsapp.base

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.filmsapp.BR
import com.example.filmsapp.base.mvi.IState
import com.example.filmsapp.base.mvi.Intention
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@ExperimentalCoroutinesApi
abstract class BaseFragment<VM : BaseViewModel<S, I>, B : ViewDataBinding, S : IState, I : Intention> : Fragment() {

    protected abstract val viewModel: VM

    @get:LayoutRes
    protected abstract val layoutRes: Int

    protected lateinit var binding: B

    private val dispatcher by lazy { requireActivity().onBackPressedDispatcher }

    private lateinit var onBackPressedCallback: OnBackPressedCallback

    abstract fun render(state: S)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun stop() {
                Timber.wtf("${this::class.java.simpleName} onStop")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun destroy() {
                Timber.wtf("${this::class.java.simpleName} onDestroy")
            }
        })

        init(inflater, container)
        init()

        viewModel.state
            .onEach { state -> render(state) }
            .launchIn(lifecycleScope)

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
    abstract fun init()

    open fun onBackPressed(@IdRes popTo: Int? = null) {
        if (!(popTo?.let { findNavController().popBackStack(it, false) }
                ?: findNavController().popBackStack())) {
            requireActivity().finish()
        }
    }
}
