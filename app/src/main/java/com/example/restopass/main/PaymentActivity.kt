package com.example.restopass.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.restopass.R
import com.example.restopass.main.ui.settings.payment.PaymentFragment

class PaymentActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_payment)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.paymentActivityFragment,
                    PaymentFragment()
                )
                .commit()
        }
    }


}