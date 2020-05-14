package com.example.restopass.main.ui.reservations

import androidx.lifecycle.ViewModel
import com.example.restopass.R
import com.example.restopass.main.ui.settings.Setting

class SettingsViewModel : ViewModel() {
    val settingsItems: List<Setting> = listOf(
        Setting("Cuenta", isCategory = true),
        Setting("Plan", R.drawable.ic_settings_24dp, false),
        Setting("Sarasa", R.drawable.ic_settings_24dp, false)
    )
}