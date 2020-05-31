package com.example.restopass.login.domain

import androidx.lifecycle.ViewModel

class SignUpViewModel(
    var name: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = "")
    : ViewModel()