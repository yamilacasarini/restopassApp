package com.example.restopass.login.signup

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.restopass.R
import com.example.restopass.databinding.FragmentSignupBinding
import com.example.restopass.login.domain.SignUpViewModel
import com.example.restopass.login.domain.Validation
import com.example.restopass.login.domain.ValidationFactory
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_signup.*

class SignUpFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: FragmentSignupBinding

    private val emailRegexes = ValidationFactory.emailValidations
    private val passwordRegexes = ValidationFactory.passwordValidations

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_signup,
            container,
            false
        )

        viewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)

        binding.signUpViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.changeToolbar(TITLE)

        binding.signUpButton.setOnClickListener {
            if (isValidForm()) {
                setLoader()
                Handler().postDelayed({
                    listener?.signUp("SignUp-Access-Token")
                }, 2000)


            }
        }
    }

    private fun setLoader() {
        view?.touchables?.forEach {
            it.isEnabled = false
        }.run {
            progressBar.visibility = View.VISIBLE
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

    interface OnFragmentInteractionListener {
        fun changeToolbar(fragmentName: String)
        fun signUp(accessToken: String)
    }

    companion object {
        const val TITLE = "Crear Cuenta"
    }
}