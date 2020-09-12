package com.example.restopass.common

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.restopass.login.LoginActivity
import com.example.restopass.login.domain.RestaurantUser
import com.example.restopass.login.domain.User
import com.example.restopass.main.MainActivity


object AppPreferences {
    private var sharedPreferences: SharedPreferences? = null
    lateinit var listener: Activity

    fun setup(activity: Activity) {
        sharedPreferences = activity.applicationContext.getSharedPreferences("com.example.restopass.sharedprefs", MODE_PRIVATE)
        this.listener = activity
    }

   private fun removeAllPreferences() {
        Key.values().forEach {
            it.remove()
        }
    }

    fun logout() {
        removeAllPreferences()
        listener.logout()
    }

    private fun Activity.logout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    var accessToken: String?
        get() = Key.ACCESS_TOKEN.getString()
        set(value) = Key.ACCESS_TOKEN.setString(value)

    var refreshToken: String?
        get() = Key.REFRESH_TOKEN.getString()
        set(value) = Key.REFRESH_TOKEN.setString(value)

    var user: User
        get() = Key.USER.getString()!!.fromJson()
        set(value) = Key.USER.setString(value.toJson())

    var restaurantUser: RestaurantUser?
        get() = Key.RESTAURANT_USER.getString()?.fromJson()
        set(value) = Key.RESTAURANT_USER.setString(value?.toJson())

    var firebaseToken: String?
        get() = Key.FIREBASE_TOKEN.getString()?.fromJson()
        set(value) = Key.FIREBASE_TOKEN.setString(value?.toJson())


    private enum class Key {
        ACCESS_TOKEN, REFRESH_TOKEN, USER, RESTAURANT_USER, FIREBASE_TOKEN;

        fun getString(): String? {
            return if (sharedPreferences!!.contains(name)) sharedPreferences!!.getString(name, "") else null
        }
        fun setString(value: String?) = value?.let { sharedPreferences!!.edit { putString(name, value) } } ?: remove()

        fun remove() = sharedPreferences!!.edit { remove(name) }
    }
}