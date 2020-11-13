package com.example.restopass.login.domain

import androidx.lifecycle.ViewModel
import com.example.restopass.service.LoginService

class SignInViewModel(var email: String = "", var password: String = "") : ViewModel() {

    suspend  fun recoverPassword() = LoginService.recoverPassword(email)
    suspend fun verifyRecoverPassword(token: String) = LoginService.verifyRecoverPassword(email, token)
    suspend fun changePassword(email: String, password: String) = LoginService.changePassword(email, password)

}
