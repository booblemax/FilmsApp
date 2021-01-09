package com.example.filmsapp.settings

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseFragment
import com.example.filmsapp.base.Event
import com.example.filmsapp.databinding.SettingsFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalCoroutinesApi
class SettingsFragment :
    BaseFragment<SettingsViewModel, SettingsFragmentBinding, SettingsState, SettingsIntents>() {

    override val viewModel: SettingsViewModel by viewModel()
    override val layoutRes: Int = R.layout.settings_fragment

    override fun render(state: SettingsState) {
        Timber.i(state.toString())
        with(state) {
            uiEvent?.let { processUiEvent(it) }

            binding.settingsSwitch.isChecked = currentTheme == AppCompatDelegate.MODE_NIGHT_YES
        }
    }

    private fun processUiEvent(it: Event<SettingsUiEvent>) {
        it.getContentIfNotHandled()?.let { uiEvent ->
            when (uiEvent) {
                is SettingsUiEvent.ChangeTheme ->
                    AppCompatDelegate.setDefaultNightMode(uiEvent.themeId)
                is SettingsUiEvent.Back -> onBackPressed()
            }
        }
    }

    override fun init() {
        initToolbar()
        initSwitch()
    }

    private fun initSwitch() {
        with(binding) {
            settingsSwitch.setOnCheckedChangeListener { _, isChecked ->
                val theme =
                    if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO

                viewModel.pushIntent(SettingsIntents.SaveTheme(theme))
            }
        }
    }

    private fun initToolbar() {
        with(binding) {
            (activity as AppCompatActivity).setSupportActionBar(settingsToolbar)
            settingsToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            settingsToolbar.setNavigationOnClickListener {
                viewModel.pushIntent(SettingsIntents.Back)
            }
        }
    }

    companion object {
        const val TAG = "SettingsFragment"
    }
}
