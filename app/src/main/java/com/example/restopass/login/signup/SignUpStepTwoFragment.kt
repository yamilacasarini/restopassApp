package com.example.restopass.login.signup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.restopass.R
import com.example.restopass.connection.RestoPassException
import com.example.restopass.databinding.FragmentSignupStepTwoBinding
import com.example.restopass.login.domain.*
import com.example.restopass.service.LoginService
import com.example.restopass.utils.AlertDialogUtils
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_signup_step_two.*
import kotlinx.android.synthetic.main.fragment_signup_step_two.emailInputLayout
import kotlinx.android.synthetic.main.fragment_signup_step_two.passwordInputLayout
import kotlinx.android.synthetic.main.fragment_signup_step_two.progressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SignUpStepTwoFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: FragmentSignupStepTwoBinding

    private val emailRegexes = ValidationFactory.emailValidations
    private val passwordRegexes = ValidationFactory.passwordValidations

    private lateinit var touchables: List<View>

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_signup_step_two,
            container,
            false
        )

        viewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)

        binding.signUpViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        touchables = view.touchables

        binding.confirmSignUpButton.setOnClickListener {
            if (isValidForm()) {
                toggleLoader()

                coroutineScope.launch {
                    try {
                        val user = LoginService.signUp(viewModel)
                        listener?.signUp(user)
                    } catch (e: Exception) {
                        toggleLoader()
                        AlertDialogUtils.buildAlertDialog(e, layoutInflater, loginContainer).show()
                    }
                }

            }
        }
    }

    private fun toggleLoader() {
        progressBar.visibility = if (progressBar.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        touchables.forEach {
            it.isEnabled = progressBar.visibility != View.VISIBLE
        }
    }

    private fun isValidForm(): Boolean{
        val emailValidation = validate(emailRegexes, emailInputLayout)
        val passwordValidation = validate(passwordRegexes, passwordInputLayout)
        return emailValidation && passwordValidation
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

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    interface OnFragmentInteractionListener {
        fun changeToolbar(fragmentName: String)
        fun signUp(loginResponse: LoginResponse)
    }
}