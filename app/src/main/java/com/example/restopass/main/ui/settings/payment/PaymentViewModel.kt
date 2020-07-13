package com.example.restopass.main.ui.settings.payment

import androidx.lifecycle.ViewModel
import com.example.restopass.domain.CreditCard
import com.example.restopass.service.CreditCardService

class PaymentViewModel: ViewModel() {
    var creditCard: CreditCard? = null

    suspend fun get() {
        CreditCardService.get().let {
            this.creditCard = it
        }
    }

    suspend fun insert(creditCard: CreditCard) {
        CreditCardService.insert(creditCard).let {
            this.creditCard = creditCard
        }
    }

    suspend fun delete() {
        CreditCardService.delete().let {
            this.creditCard = null
        }
    }
}