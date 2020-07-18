package com.example.restopass.main.ui.settings.personalInfo

import androidx.lifecycle.ViewModel
import com.example.restopass.domain.PersonalInfo
import com.example.restopass.domain.PersonalInfoRequest
import com.example.restopass.service.UserService

class PersonalInfoViewModel : ViewModel() {
    var personalInfo: PersonalInfo? = null

    suspend fun update(personalInfo: PersonalInfoRequest) {
        UserService.update(personalInfo)

        personalInfo.let {
            this.personalInfo = this.personalInfo!!.copy(name = it.name,
                lastName = it.lastName, secondaryEmails = it.secondaryEmails)
        }
    }
}