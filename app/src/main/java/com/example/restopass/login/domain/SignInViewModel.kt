package com.example.restopass.login.domain

import androidx.lifecycle.ViewModel
import com.example.restopass.service.LoginService

class SignInViewModel(var email: String = "prueba@prueba.com", var password: String = "prueba") : ViewModel() {

    suspend  fun recoverPassword() = LoginService.recoverPassword(email)
    suspend fun verifyRecoverPassword(code: String) = LoginService.verifyRecoverPassword(email, code)

}