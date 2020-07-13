package com.example.restopass.main.ui.settings.payment

import androidx.lifecycle.ViewModel
import com.example.restopass.domain.CreditCard
import com.example.restopass.service.PaymentService

class PaymentViewModel: ViewModel() {
    var creditCard: CreditCard? = null

    suspend fun get() {
        PaymentService.get().let {
            this.creditCard = it
        }
    }

    suspend fun insert(creditCard: CreditCard) {
        PaymentService.insert(creditCard).let {
            this.creditCard = creditCard
        }
    }

    suspend fun delete() {
        PaymentService.delete().let {
            this.creditCard = null
        }
    }
}