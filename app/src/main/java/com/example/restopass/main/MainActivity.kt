package com.example.restopass.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.firebase.NotificationType.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppPreferences.setup(applicationContext)

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        intent.getStringExtra("fcmNotification")?.let {
            val bundle = bundleOf("reservationId" to intent.getStringExtra("reservationId"))

            val fragment = intent.getStringExtra("notificationType")?.run {
                if (values().map { it.name }.contains(this) && fragments.containsKey(valueOf(this)))
                    fragments[valueOf(this)] else R.id.navigation_home
            }

            navController.navigate(fragment ?: R.id.navigation_home, bundle)
        }
    }

    companion object {
        val fragments = mapOf(
            INVITE_RESERVATION to R.id.navigation_reservations,
            CANCEL_RESERVATION to R.id.navigation_reservations,
            CONFIRMED_RESERVATION to R.id.navigation_reservations,
            SCORE_EXPERIENCE to R.id.navigation_settings
        )
    }

}
