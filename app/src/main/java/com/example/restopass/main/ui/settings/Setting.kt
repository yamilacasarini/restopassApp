package com.example.restopass.main.ui.settings

enum class ButtonSettingType {
    PLAN,
    PERSONAL_INFO,
    PAYMENT_METHODS,
    BOOKING_HISTORY,
    NOTIFICATIONS,
    SESSION
}

class Setting(val title: Int, val image: Int? = null, val typeButton: ButtonSettingType? = null)