package com.example.restopass.main

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.domain.Restaurant
import com.example.restopass.firebase.NotificationType.*
import com.example.restopass.login.LoginActivity
import com.example.restopass.main.common.LocationService
import com.example.restopass.main.common.restaurant.Rating
import com.example.restopass.main.ui.home.notEnrolledHome.NotEnrolledFragmentListener
import com.example.restopass.service.RestaurantScore
import com.example.restopass.service.RestaurantService
import com.example.restopass.service.UserService
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber


class MainActivity : AppCompatActivity(), NotEnrolledFragmentListener {
    var home: Int = 0

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        AppPreferences.setup(this)

        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)

        setToolbarAndNavBar(navController)
        setHomeFragment()

        intent.getStringExtra("fcmNotification")?.let {
            val bundle = bundleOf("reservationId" to intent.getStringExtra("reservationId"))

            val fragment = intent.getStringExtra("notificationType")?.run {
                if (values().map { it.name }.contains(this) && fragments.containsKey(valueOf(this))) {
                    if (valueOf(this) == SCORE_EXPERIENCE) {
                        bundle.putString("restaurantId", intent.getStringExtra("restaurantId"))
                    }
                    fragments[valueOf(this)]
                } else home
            }

            navController.navigate(fragment ?: home, bundle)
        }


        LocationService.startLocationService(this.applicationContext, this)
    }

    fun setHomeFragment() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHostFragment!!.navController
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

        navView.setupWithNavController(navController)
    }

    private fun setToolbarAndNavBar(navController: NavController) {
        setSupportActionBar(topAppBar)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            topAppBar.visibility = View.GONE
            mainBackButton.visibility = View.GONE
            val children = navView.menu.children
            if (destination.id in children.map { it.itemId } || destination.id in showableNavBarFragments) {
                navView.visibility = View.VISIBLE
            }
            else {
                navView.visibility = View.GONE
            }
        }

        topAppBar.setNavigationOnClickListener {
            navController.popBackStack()
        }


        mainBackButton.apply {
            val animation =
                AnimationUtils.loadAnimation(applicationContext, R.anim.alpha)
            setOnClickListener {
                it.startAnimation(animation)
                navController.popBackStack()
            }
        }
    }

    fun setBackBehaviour() {
        topAppBar.setNavigationOnClickListener {
            findNavController(R.id.nav_host_fragment).popBackStack()
        }

        this.onBackPressedDispatcher.addCallback {
            findNavController(R.id.nav_host_fragment).popBackStack()
        }

    }


    fun unfavorite(restaurant: Restaurant) {
        coroutineScope.launch {
            UserService.unfavorite(restaurant.restaurantId)
        }
    }

    fun favorite(restaurant: Restaurant) {
        coroutineScope.launch {
            UserService.favorite(restaurant.restaurantId)
        }
    }

    fun scoreRestaurant(rating: Rating, restaurantId: String, dishId: String) {
        coroutineScope.launch {
            try {
                RestaurantService.scoreRestaurant(RestaurantScore(restaurantId, dishId, rating.resto, rating.dish))
            } catch (e: Exception) {
                Timber.i("Error while scoring restaurant for id: ${restaurantId}, dish: ${dishId}. Err: ${e.message}")
            }
        }
    }

    fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        this.finish()
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
            SCORE_EXPERIENCE to R.id.restaurantRatingFragment
        )

        val showableNavBarFragments = listOf(R.id.refreshErrorFragment, R.id.emptyReservationFragment)
    }

    override fun onEnrollClick() {
        setHomeFragment()
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

}
