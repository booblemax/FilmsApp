package com.example.filmsapp.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import com.example.filmsapp.R
import com.example.filmsapp.databinding.SettingsFragmentBinding
import com.example.filmsapp.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<SettingsViewModel, SettingsFragmentBinding>() {

    override val viewModel: SettingsViewModel by viewModel()
    override val layoutRes: Int = R.layout.settings_fragment

    override fun init() {
        with(binding) {
            materialToolbar.setNavigationOnClickListener { onBackPressed() }
            settingsSwitch.setOnCheckedChangeListener { _, isChecked ->
                val theme =
                    if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                viewModel.saveChosenTheme(theme)
                AppCompatDelegate.setDefaultNightMode(theme)
            }
            settingsSwitch.isChecked =
                viewModel.getCurrentTheme() == AppCompatDelegate.MODE_NIGHT_YES
        }
    }
}
