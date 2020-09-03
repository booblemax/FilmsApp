package com.example.filmsapp.settings

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.filmsapp.R
import com.example.filmsapp.base.BaseFragment
import com.example.filmsapp.base.mvi.IState
import com.example.filmsapp.base.mvi.Intention
import com.example.filmsapp.databinding.SettingsFragmentBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class SettingsFragment : BaseFragment<SettingsViewModel, SettingsFragmentBinding, IState, Intention>() {

    override val viewModel: SettingsViewModel by viewModel()
    override val layoutRes: Int = R.layout.settings_fragment

    override fun render(state: IState) {
        TODO("Not yet implemented")
    }

    override fun init() {
        with(binding) {
            (activity as AppCompatActivity).setSupportActionBar(settingsToolbar)
            settingsToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            settingsToolbar.setNavigationOnClickListener { onBackPressed() }
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
