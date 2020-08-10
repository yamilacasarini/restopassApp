package com.example.restopass.restaurantApp.ui.home

import android.app.Activity
import android.content.Intent
import androidx.navigation.findNavController
import com.example.restopass.R
import com.example.restopass.main.MainActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import kotlinx.android.synthetic.main.activity_restaurant.*


class QRActivity : CaptureActivity() {

    val activity : Activity? = null;

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        val result = IntentIntegrator.parseActivityResult(
            requestCode,
            resultCode, data
        )
            findNavController(R.id.nav_host_fragment_restaurant).navigate(R.id.restaurantDishesFragment)

    }

}