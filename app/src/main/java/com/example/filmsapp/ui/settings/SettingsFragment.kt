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
        binding.materialToolbar.setNavigationOnClickListener { onBackPressed() }
        binding.settingsSwitch.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}