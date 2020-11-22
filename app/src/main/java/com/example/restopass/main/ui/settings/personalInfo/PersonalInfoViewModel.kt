package com.example.restopass.main.ui.settings.personalInfo

import androidx.lifecycle.ViewModel
import com.example.restopass.common.md5
import com.example.restopass.domain.PersonalInfo
import com.example.restopass.domain.PersonalInfoRequest
import com.example.restopass.domain.SecondaryEmail
import com.example.restopass.service.CommunicationsService
import com.example.restopass.service.PersonalInfoService
import org.androidannotations.annotations.rest.Delete

class PersonalInfoViewModel : ViewModel() {
    var personalInfo: PersonalInfo? = null

    suspend fun get() {
        PersonalInfoService.get().let {
            this.personalInfo = it
        }
    }

    suspend fun update(firstName: String, lastName: String, password: String?) {
        PersonalInfoService.update(PersonalInfoRequest(firstName, lastName, password = if (password.isNullOrBlank()) null else password.md5()))

        this.personalInfo = this.personalInfo!!.copy(
            name = this.personalInfo!!.name,
            lastName = this.personalInfo!!.lastName
        )
    }

    suspend fun deleteAccount(password: String) {
        PersonalInfoService.deleteAccount(DeleteUserRequest(password))
    }

    suspend fun deleteSecondaryEmail(email: String) {
        CommunicationsService.delete(email)

        personalInfo?.let {
            val emails = it.secondaryEmails.filter { se -> se.email != email }
            this.personalInfo = this.personalInfo!!.copy(secondaryEmails = emails.toMutableList())
        }
    }

    suspend fun addSecondaryEmail(email: String) {
        CommunicationsService.add(email)

        personalInfo?.let {
            val emails = it.secondaryEmails
            emails.add(SecondaryEmail( email, false))

            this.personalInfo = this.personalInfo!!.copy(secondaryEmails = emails)
        }
    }
}

class DeleteUserRequest(
    val password: String
)