package com.example.restopass.login.domain

class Validation(stringRegex: String, val errorMessage: String?) {
    val regex: Regex = stringRegex.toRegex()
}

class ValidationFactory {

    companion object {
        val emailValidations: List<Validation> = listOf(
            Validation("^.+$", "Este campo es obligatorio"),
            Validation("(^$)|^[\\w\\.-]+@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,4}$", "Revisá el formato del email")
        )

        val passwordValidations: List<Validation> = listOf(
            Validation("^.+$", "Este campo es obligatorio")
            //Validation("^{1,8}\$", "Máximo 8 caracteres")
        )
    }
}