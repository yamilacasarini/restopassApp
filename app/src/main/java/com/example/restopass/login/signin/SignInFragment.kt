package com.example.restopass.login.signin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restopass.R
import com.example.restopass.databinding.FragmentSigninBinding
import com.example.restopass.login.domain.*
import com.example.restopass.service.LoginService
import com.example.restopass.utils.AlertDialogUtils
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_signin.*
import kotlinx.android.synthetic.main.fragment_signin.emailInput
import kotlinx.android.synthetic.main.fragment_signin.emailInputLayout
import kotlinx.android.synthetic.main.fragment_signin.passwordInputLayout
import kotlinx.android.synthetic.main.fragment_signin.progressBar
import kotlinx.android.synthetic.main.fragment_signup_step_two.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.Exception


class SignInFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var viewModel: SignInViewModel
    private lateinit var binding: FragmentSigninBinding

    private val emailRegexes = ValidationFactory.emailValidations
    private val passwordRegexes = ValidationFactory.passwordValidations

    private lateinit var touchables: List<View>

    val job = Job()
    val coroutineScope = CoroutineScope(job + Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_signin,
            container,
            false
        )

        viewModel = ViewModelProvider(requireActivity()).get(SignInViewModel::class.java)

        binding.signInViewModel = viewModel

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        touchables = view.touchables

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = TITLE
            show()
        }

        forgotPasswordButton.setOnClickListener {
            findNavController().navigate(R.id.forgotPasswordFragment)
        }

        binding.restoPassSignInButton.setOnClickListener {
            if (isValidForm()) {
                toggleLoader()

                coroutineScope.launch {
                    try {
                        val loginRequest =
                            Login(
                                emailInput.text.toString(),
                                passwordInput.text.toString()
                            )
                        if(emailInput.text.toString().contains("@restopass.com")) {
                           listener?.onRestaurantSignIn(LoginService.signRestaurantIn(loginRequest))
                       } else {
                        listener?.onSignIn(LoginService.signIn(loginRequest))
                       }
                    } catch (e: Exception) {
                        toggleLoader()
                        AlertDialogUtils.buildAlertDialog(e, layoutInflater, loginContainer).show()
                    }
                }
            }
        }

    }

    private fun toggleLoader() {
        progressBar.visibility =
            if (progressBar.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        touchables.forEach {
            it.isEnabled = progressBar.visibility != View.VISIBLE
        }
    }

    private fun isValidForm(): Boolean {
        val emailValidation = validate(emailRegexes, emailInputLayout)
        val passwordValidation = validate(passwordRegexes, passwordInputLayout)
        return emailValidation && passwordValidation
    }

    private fun validate(validations: List<Validation>, layout: TextInputLayout): Boolean {
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
        fun onSignIn(loginResponse: LoginResponse)
        fun onRestaurantSignIn(loginResponse: LoginRestaurantResponse)
    }

    companion object {
        const val TITLE = "Iniciar Sesión"
    }
}