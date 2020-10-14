package com.example.restopass.main.ui.settings

enum class ButtonSettingType {
    PLAN,
    PERSONAL_INFO,
    MY_VISITS,
    COMMUNICATIONS_SETTINGS,
    PAYMENT_METHODS,
    BOOKING_HISTORY,
    NOTIFICATIONS,
    SESSION
}

data class Setting(val title: Int, val image: Int? = null, val typeButton: ButtonSettingType? = null)