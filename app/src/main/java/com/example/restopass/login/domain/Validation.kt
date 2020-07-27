package com.example.restopass.login.domain

class Validation(stringRegex: String, val errorMessage: String?) {
    val regex: Regex = stringRegex.toRegex()
}

object ValidationFactory {

        val emailValidations = listOf(
            Validation("^.+$", "Este campo es obligatorio"),
            Validation("(^$)|^[\\w\\.-]+@([a-zA-Z0-9-]+\\.)+[a-zA-Z0-9]{2,4}$", "Revisá el formato del email")
        )

        val passwordValidations = listOf(
            Validation("^.+$", "Este campo es obligatorio")
            //Validation("^{1,8}\$", "Máximo 8 caracteres")
        )

        val firstNameValidations = listOf(
            Validation("^.+$", "Este campo es obligatorio")
        )

        val lastNameValidations = listOf(
            Validation("^.+$", "Este campo es obligatorio")
        )

        val newPasswordValidations: List<Validation> = listOf(
           // Validation("^{1,8}", "Máximo 8 caracteres")
        )
}