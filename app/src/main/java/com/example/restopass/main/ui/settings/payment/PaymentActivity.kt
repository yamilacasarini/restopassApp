package com.example.restopass.main.ui.settings.payment

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.restopass.R
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_payment)

        setSupportActionBar(paymentTopAppBar)
        setBackBehaviour()
    }

    fun setBackBehaviour() {
        paymentTopAppBar.setNavigationOnClickListener {
            val navController = findNavController(R.id.paymentActivityFragment)
            if (R.id.paymentListFragment == navController.currentDestination?.id) finish()
            else navController.popBackStack()
        }

        this.onBackPressedDispatcher.addCallback {
            val navController = findNavController(R.id.paymentActivityFragment)
            if (R.id.paymentListFragment == navController.currentDestination?.id) finish()
            else navController.popBackStack()
        }
    }

}