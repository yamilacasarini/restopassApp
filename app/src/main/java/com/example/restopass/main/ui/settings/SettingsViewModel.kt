package com.example.restopass.main.ui.settings

import androidx.lifecycle.ViewModel
import com.example.restopass.R

class SettingsViewModel : ViewModel() {
    val settingsItems: List<Setting> = listOf(
        Setting(title = "Cuenta", isCategory = true),
        Setting(SettingType.PLAN, "Plan", R.drawable.ic_settings_24dp, false),
        Setting(SettingType.CREDIT_CARD, "Sarasa", R.drawable.ic_settings_24dp, false)
    )
}