package com.example.restopass.main

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.common.orElse
import com.example.restopass.domain.Restaurant
import com.example.restopass.firebase.NotificationType.*
import com.example.restopass.main.common.LocationService
import com.example.restopass.main.ui.home.notEnrolledHome.NotEnrolledFragmentListener
import com.example.restopass.service.UserService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), NotEnrolledFragmentListener {
    var home: Int = 0


    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppPreferences.setup(applicationContext)

        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        setHomeFragment(navController)

        intent.getStringExtra("fcmNotification")?.let {
            val bundle = bundleOf("reservationId" to intent.getStringExtra("reservationId"))

            val fragment = intent.getStringExtra("notificationType")?.run {
                if (values().map { it.name }
                        .contains(this) && fragments.containsKey(valueOf(this))) {
                    fragments[valueOf(this)]
                } else home
            }

            navController.navigate(fragment ?: home, bundle)
        }


        LocationService.startLocationService(this.applicationContext, this)
    }

    private fun setHomeFragment(navController: NavController) {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.mobile_navigation)

        if (AppPreferences.user.actualMembership != null) {
            home = R.id.navigation_enrolled_home
            navView.menu.findItem(R.id.navigation_not_enrolled_home).isVisible = false
        } else {
            home = R.id.navigation_not_enrolled_home
            navView.menu.findItem(R.id.navigation_enrolled_home).isVisible = false
        }


        navView.menu.findItem(home).isVisible = true
        graph.startDestination = home
        navController.graph = graph
        //val navArgument= NavArgument.Builder().setDefaultValue(arguments?.get("fromLogin")).build()
        //graph.addArgument(bundle)

        navView.setupWithNavController(navController)
    }

    fun unfavorite(restaurant: Restaurant) {
        coroutineScope.launch {
            UserService.unfavorite(restaurant.restaurantId)
            AppPreferences.user.apply {
                val restaurants = this.favoriteRestaurants
                restaurants?.remove(restaurant.restaurantId)
                AppPreferences.user = this.copy(favoriteRestaurants = restaurants)
            }

        }
    }

    fun favorite(restaurant: Restaurant) {
        coroutineScope.launch {
            UserService.favorite(restaurant.restaurantId)
            AppPreferences.user.apply {
                var restaurants = this.favoriteRestaurants
                restaurants?.add(restaurant.restaurantId)
                    .orElse { restaurants = mutableListOf(restaurant.restaurantId) }
                AppPreferences.user = this.copy(favoriteRestaurants = restaurants)
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LocationService.permissionCode -> if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                LocationService.isLocationGranted(true)
            }
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

    override fun onEnrollClick() {
        setHomeFragment(findNavController(R.id.nav_host_fragment))
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

}
