package com.example.restopass.main.ui.settings

enum class SettingType {
    PLAN,
    CREDIT_CARD
}

class Setting(val type: SettingType? = null, val title: String, val image: Int? = null, val isCategory: Boolean)