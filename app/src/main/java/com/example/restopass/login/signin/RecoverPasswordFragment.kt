package com.example.restopass.login.signin


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restopass.R
import com.example.restopass.login.domain.SignInViewModel
import com.example.restopass.login.domain.Validation
import com.example.restopass.login.domain.ValidationFactory
import com.example.restopass.main.common.AlertBody
import com.example.restopass.main.common.AlertDialog
import com.example.restopass.service.LoginService
import com.example.restopass.utils.AlertDialogUtils
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_recover_password.*
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

class RecoverPasswordFragment : Fragment() {
    private val passwordRegexes = ValidationFactory.passwordValidations

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_recover_password, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = TITLE
            show()
        }

        viewModel = ViewModelProvider(requireActivity()).get(SignInViewModel::class.java)

        recoverPasswordButton.setOnClickListener {
            if (isValidForm()) {
                recoverPasswordLoader.visibility = View.VISIBLE
                recoverPasswordButton.isEnabled = false
                toggleEnabledInputs()
                coroutineScope.launch {
                    try {
                        LoginService.changePassword(viewModel.email, recoverPasswordInput.text.toString())

                        recoverPasswordLoader.visibility = View.GONE

                        val alertBody = AlertBody(
                            title = getString(R.string.recoverPasswordTitleSuccess),
                            description = getString(R.string.recoverPasswordDescriptionSuccess),
                            positiveActionText = R.string.accept)

                        AlertDialog.getActionDialog(
                            context,
                            layoutInflater,
                            recoverPasswordContainer,
                            ::popStack,
                            alertBody,
                            ::popStack).show()

                    } catch (e: Exception) {
                        if (isActive) {
                            Timber.e(e)
                            recoverPasswordLoader.visibility = View.GONE
                            AlertDialogUtils.buildAlertDialog(
                                e,
                                layoutInflater,
                                recoverPasswordContainer
                            ).show()
                            recoverPasswordButton.isEnabled = true
                            toggleEnabledInputs()
                        }
                    }
                }
            }
        }

    }

    private fun popStack() {
        findNavController().navigate(RecoverPasswordFragmentDirections.actionRecoverPasswordFragmentToLoginFragment())
    }


    private fun isValidForm(): Boolean {
        val passwordValidation = validate(passwordRegexes, recoverPasswordInputLayout)
        val repeatPasswordValidation = validate(passwordRegexes, repeatRecoverPasswordInputLayout)
        val isValid = passwordValidation && repeatPasswordValidation
        if (isValid) {
            if (recoverPasswordInput.text.toString() != repeatRecoverPasswordInput.text.toString()) {
                recoverPasswordInputLayout.error = getString(R.string.passwordsNotEqual)
                repeatRecoverPasswordInputLayout.error = getString(R.string.passwordsNotEqual)
                return false
            }

            return true
        }
        return false
    }

    private fun toggleEnabledInputs() {
        VIEW_ELEMENTS.forEach {
            val input = view?.findViewById<EditText>(it)
            input!!.isEnabled = !input.isEnabled
        }
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

    companion object {
        const val TITLE = "Recuperar contraseña"
        val VIEW_ELEMENTS = listOf(
            R.id.recoverPasswordInput,
            R.id.repeatRecoverPasswordInput
        )
    }
}