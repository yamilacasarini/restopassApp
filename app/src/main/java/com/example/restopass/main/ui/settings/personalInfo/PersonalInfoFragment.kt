package com.example.restopass.main.ui.settings.personalInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.restopass.R
import com.example.restopass.login.domain.Validation
import com.example.restopass.login.domain.ValidationFactory
import com.example.restopass.main.MainActivity
import com.example.restopass.utils.AlertDialogUtils
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_personal_info.*
import kotlinx.coroutines.*
import timber.log.Timber

class PersonalInfoFragment : Fragment() {

    private lateinit var viewModel: PersonalInfoViewModel

    private val firstNameRegexes = ValidationFactory.firstNameValidations
    private val lastNameRegexes = ValidationFactory.lastNameValidations
    private val passwordRegexes = ValidationFactory.newPasswordValidations

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_personal_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(PersonalInfoViewModel::class.java)

        saveButton.setOnClickListener {
            onSaveClick()
        }

        (activity as MainActivity).topAppBar?.apply {
            setTitle(R.string.personal_info)
            visibility = View.VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()

        personalInfoLoader.visibility = View.VISIBLE
        personalInfoSection.visibility = View.GONE
        coroutineScope.launch {
            try {
                viewModel.get()

                fillLayout()

                personalInfoLoader.visibility = View.GONE
                personalInfoSection.visibility = View.VISIBLE
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    personalInfoLoader.visibility = View.GONE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, personalInfoContainer, view).show()
                }
            }
        }

    }

    private fun onSaveClick() {
        if (isValidForm()) {
            personalInfoLoader.visibility = View.VISIBLE
            coroutineScope.launch {
                try {
                    viewModel.update(firstNameInput.text.toString(), lastNameInput.text.toString(), newPasswordInput.text.toString())

                    personalInfoLoader.visibility = View.GONE

                    AlertDialogUtils.buildAlertDialog(null,
                        layoutInflater,
                        personalInfoContainer,
                        view,
                        getString(R.string.savePersonalInfoSuccess)
                    ).show()

                } catch (e: Exception) {
                    if (isActive) {
                        Timber.e(e)
                        personalInfoLoader.visibility = View.GONE
                        AlertDialogUtils.buildAlertDialog(
                            e,
                            layoutInflater,
                            personalInfoContainer,
                            view
                        ).show()
                    }
                }
            }
        }
    }

    private fun fillLayout() {
        viewModel.personalInfo?.let {
            firstNameInput.setText(it.name)
            lastNameInput.setText(it.lastName)
            principalEmailInput.setText(it.email)
        }
    }

    private fun isValidForm(): Boolean{
        val firstName = validate(firstNameRegexes, firstNameInputLayout)
        val lastName = validate(lastNameRegexes, lastNameInputLayout)
        val passwordValidation = validate(passwordRegexes, newPasswordInputLayout)
        return firstName &&  lastName && passwordValidation
    }

    private fun validate(validations: List<Validation>, layout: TextInputLayout) : Boolean {
        validations.find {
            !it.regex.matches(layout.editText?.text.toString())
        }?.let {
            layout.error = it.errorMessage
            return false
        }

        layout.error = null
        return true
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }
}