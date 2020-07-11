package com.example.restopass.main.ui.settings.payment

import androidx.lifecycle.ViewModel
import com.example.restopass.domain.CreditCard
import com.example.restopass.service.CreditCardService

class PaymentViewModel: ViewModel() {
    var creditCard: CreditCard? = null

    suspend fun insert(creditCard: CreditCard) {
        this.creditCard = creditCard.copy("123", number = creditCard.number.takeLast(4))
//        CreditCardService.insert(creditCard).let {
//            this.creditCard = creditCard
//        }
    }

    suspend fun delete() {
        this.creditCard = null
//        CreditCardService.delete(id).let {
//            this.creditCard = null
//        }
    }
}