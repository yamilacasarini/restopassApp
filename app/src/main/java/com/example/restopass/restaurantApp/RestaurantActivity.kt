package com.example.restopass.restaurantApp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.domain.DoneReservationViewModel
import com.example.restopass.utils.AlertDialogUtils
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_restaurant.*
import kotlinx.coroutines.*
import timber.log.Timber


class RestaurantActivity : AppCompatActivity() {

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)
    private val mapper = ObjectMapper()

    private lateinit var doneReservationViewModel: DoneReservationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)
        start()

        doneReservationViewModel =
            ViewModelProvider(this).get(DoneReservationViewModel::class.java)
    }

    private fun start() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_restaurant) as NavHostFragment?
        val navController = navHostFragment!!.navController
        val navView: BottomNavigationView = findViewById(R.id.nav_view_restaurant)

        val inflater = navController.navInflater
        val graph = inflater.inflate(R.navigation.restaurant_navigation)

        navController.graph = graph

        mapper.propertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE

        navView.setupWithNavController(navController)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(
            requestCode,
            resultCode, data
        )

        coroutineScope.launch {
            try {
                val resultJson = mapper.readValue(result.contents, QrData::class.java)
                doneReservationViewModel.done(
                    resultJson.reservationId,
                    resultJson.userId,
                    AppPreferences.restaurantUser!!.restaurant.restaurantId
                )

                findNavController(R.id.nav_host_fragment_restaurant).navigate(R.id.restaurantDishesFragment)
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container).show()
                }
            }
        }
    }

}

class QrData{
    lateinit var reservationId : String
    lateinit var userId: String
}
