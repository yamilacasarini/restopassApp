package com.example.restopass.main.ui.settings.payment

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.restopass.R
import kotlinx.android.synthetic.main.activity_payment.*

class PaymentActivity : AppCompatActivity() {

    private lateinit var paymentViewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_payment)

        setSupportActionBar(paymentTopAppBar)
        setBackBehaviour()

        paymentViewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)
        if (paymentViewModel.creditCard == null) {
            val navController = findNavController(R.id.paymentActivityFragment)
            val inflater = navController.navInflater
            val graph = inflater.inflate(R.navigation.payment_navigation)
            graph.startDestination = R.id.emptyPaymentFragment

            navController.graph = graph
        }

    }

    fun setBackBehaviour() {
        paymentTopAppBar.setNavigationOnClickListener {
            goBackOrFinish()
        }

        this.onBackPressedDispatcher.addCallback {
           goBackOrFinish()
        }
    }

    private fun goBackOrFinish() {
        val navController = findNavController(R.id.paymentActivityFragment)
        if (navController.currentDestination?.id in startDestinations) finish()
        else navController.popBackStack()
    }

    companion object {
        private val startDestinations = listOf(R.id.paymentListFragment, R.id.emptyPaymentFragment)
    }

}