package com.example.restopass.login.signin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.restopass.R
import com.example.restopass.domain.PersonalInfoRequest
import com.example.restopass.login.domain.Validation
import com.example.restopass.login.domain.ValidationFactory
import com.example.restopass.main.common.AlertBody
import com.example.restopass.service.PersonalInfoService
import com.example.restopass.utils.AlertDialogUtils
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_recover_password.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

class RecoverPasswordFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private val passwordRegexes = ValidationFactory.passwordValidations

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_recover_password, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.changeToolbar(TITLE)

        recoverPasswordButton.setOnClickListener {
            if (isValidForm()) {
                recoverPasswordLoader.visibility = View.VISIBLE
                coroutineScope.launch {
                    try {
                        PersonalInfoService.update(PersonalInfoRequest(
                            password = recoverPasswordInput.text.toString()
                        ))

                        recoverPasswordLoader.visibility = View.GONE

                        AlertDialogUtils.buildAlertDialog(null,
                            layoutInflater,
                            recoverPasswordContainer,
                            alertBody = AlertBody(description = getString(R.string.savePersonalInfoSuccess))
                        ).show()

                    } catch (e: Exception) {
                        if (isActive) {
                            Timber.e(e)
                            recoverPasswordLoader.visibility = View.GONE
                            AlertDialogUtils.buildAlertDialog(
                                e,
                                layoutInflater,
                                recoverPasswordContainer
                            ).show()
                        }
                    }
                }
            }
        }

    }

    private fun isValidForm(): Boolean {
        val passwordValidation = validate(passwordRegexes, recoverPasswordInputLayout)
        val repeatPasswordValidation = validate(passwordRegexes, repeatRecoverPasswordInputLayout)
        val isValid = passwordValidation && repeatPasswordValidation
        if (isValid) {
            if (recoverPasswordInputLayout.editText !== repeatRecoverPasswordInputLayout.editText) {
                recoverPasswordInputLayout.error = getString(R.string.passwordsNotEqual)
                repeatRecoverPasswordInputLayout.error = getString(R.string.passwordsNotEqual)
                return false
            }

            return true
        }
        return false
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    interface OnFragmentInteractionListener {
        fun changeToolbar(fragmentName: String)
        fun showFragment(fragment: Fragment)
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

    companion object {
        const val TITLE = "Recuperar contraseña"
    }
}