package com.example.restopass.common

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.restopass.login.domain.User

object AppPreferences {
    private var sharedPreferences: SharedPreferences? = null

    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences("com.example.restopass.sharedprefs", MODE_PRIVATE)
    }

    var accessToken: String?
        get() = Key.ACCESS_TOKEN.getString()
        set(value) = Key.ACCESS_TOKEN.setString(value)

    var refreshToken: String?
        get() = Key.REFRESH_TOKEN.getString()
        set(value) = Key.REFRESH_TOKEN.setString(value)

    var user: User?
        get() = Key.USER.getString()?.fromJson()
        set(value) = Key.USER.setString(value?.toJson())

    private enum class Key {
        ACCESS_TOKEN, REFRESH_TOKEN, USER;

        fun getString(): String? {
            return if (sharedPreferences!!.contains(name)) sharedPreferences!!.getString(name, "") else null
        }
        fun setString(value: String?) = value?.let { sharedPreferences!!.edit { putString(name, value) } } ?: remove()

        fun remove() = sharedPreferences!!.edit { remove(name) }
    }
}