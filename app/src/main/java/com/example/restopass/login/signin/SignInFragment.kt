package com.example.restopass.login.signin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.restopass.R
import com.example.restopass.databinding.FragmentSigninBinding
import com.example.restopass.login.domain.Login
import com.example.restopass.login.domain.SignInViewModel
import com.example.restopass.login.domain.Validation
import com.example.restopass.login.domain.ValidationFactory
import com.example.restopass.main.common.AlertDialog
import com.example.restopass.service.LoginService
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_signin.*
import kotlinx.android.synthetic.main.fragment_signin.emailInputLayout
import kotlinx.android.synthetic.main.fragment_signin.passwordInputLayout
import kotlinx.android.synthetic.main.fragment_signin.progressBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception


class SignInFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var viewModel: SignInViewModel
    private lateinit var binding: FragmentSigninBinding

    private val emailRegexes = ValidationFactory.emailValidations
    private val passwordRegexes = ValidationFactory.passwordValidations

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

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
        listener?.changeToolbar(TITLE)

        forgotPasswordButton.setOnClickListener {
            listener?.showFragment(ForgotPasswordFragment())
        }

        binding.restoPassSignInButton.setOnClickListener {
           if (isValidForm()) {
               //toggleLoader()

               CoroutineScope(Main).launch {
                   try {
                       val response = LoginService.signIn(
                           Login(
                               emailInput.text.toString(),
                               passwordInput.text.toString()
                           )
                       )
                       if (response.code() != 200) {
                           AlertDialog.getAlertDialog(
                               context,
                               layoutInflater.inflate(R.layout.alert_dialog_title, container, false)
                           )
                               .show()
                           // toggleLoader()
                       } else {
                           listener?.signIn("An-Access-Token")
                       }
                   } catch (e: Exception) {
                       AlertDialog.getAlertDialog(
                           context,
                           layoutInflater.inflate(R.layout.alert_dialog_title, container, false)
                       ).show()
                   }
                  // toggleLoader()
               }

//               Handler().postDelayed({
//                   listener?.signIn("An-Access-Token")
//               }, 2000)
           }
        }

    }

    private fun toggleLoader() {
        progressBar.visibility = if (progressBar.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        view?.touchables?.forEach {
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

    interface OnFragmentInteractionListener {
        fun showFragment(fragment: Fragment)
        fun changeToolbar(fragmentName: String)
        fun signIn(accessToken: String)
    }

    companion object {
        const val TITLE = "Iniciar Sesión"
    }
}