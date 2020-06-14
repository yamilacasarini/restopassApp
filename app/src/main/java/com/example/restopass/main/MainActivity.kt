package com.example.restopass.main

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.firebase.NotificationType.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppPreferences.setup(applicationContext)

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        intent.getStringExtra("notificationType")?.let {
            val bundle = bundleOf("reservationId" to intent.getStringExtra("reservationId"))
            navController.navigate(fragments.getOrDefault(valueOf(it), R.id.navigation_home), bundle)
        }
    }

    companion object {
        val fragments = mapOf(
            INVITE_RESERVATION to R.id.navigation_reservations,
            CANCEL_RESERVATION to R.id.navigation_reservations,
            SCORE_EXPERIENCE to R.id.navigation_settings
            )
    }

}
