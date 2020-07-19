package com.example.restopass.main.ui.settings.personalInfo

import androidx.lifecycle.ViewModel
import com.example.restopass.domain.PersonalInfo
import com.example.restopass.domain.PersonalInfoRequest
import com.example.restopass.service.CommunicationsService
import com.example.restopass.service.PersonalInfoService

class PersonalInfoViewModel : ViewModel() {
    var personalInfo: PersonalInfo? = null

    suspend fun get() {
        PersonalInfoService.get().let {
            this.personalInfo = it
        }
    }

    suspend fun update(personalInfo: PersonalInfoRequest) {
        PersonalInfoService.update(personalInfo)

        personalInfo.let {
            this.personalInfo = this.personalInfo!!.copy(name = it.name,
                lastName = it.lastName, secondaryEmails = it.secondaryEmails)
        }
    }

    suspend fun deleteSecondaryEmail(email: String) {
        CommunicationsService.delete(email)

        personalInfo?.let {
            val emails = it.secondaryEmails
            emails?.remove(email)

            this.personalInfo = this.personalInfo!!.copy(secondaryEmails = emails)
        }
    }

    suspend fun addSecondaryEmail(email: String) {
        CommunicationsService.add(email)

        personalInfo?.let {
            val emails = it.secondaryEmails
            emails?.add(email)

            this.personalInfo = this.personalInfo!!.copy(secondaryEmails = emails)
        }
    }
}