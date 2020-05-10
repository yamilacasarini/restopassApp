package com.example.restopass.login.signin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.restopass.R
import com.example.restopass.databinding.FragmentSigninBinding
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.android.synthetic.main.fragment_signin.*
import kotlinx.android.synthetic.main.fragment_signin.view.*

class SignInFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null

    private lateinit var viewModel: SignInViewModel
    private lateinit var binding: FragmentSigninBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate view and obtain an instance of the binding class
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_signin,
            container,
            false
        )

        viewModel = ViewModelProvider(requireActivity()).get(SignInViewModel::class.java)

        binding.signInViewModel = viewModel

        binding.restoPassSignInButton.setOnClickListener {
            updateViewModel()
        }


        return binding.root
    }

    private fun updateViewModel() {
        binding.apply {
            viewModel.email = emailInput.text.toString()
            viewModel.password = passwordInput.text.toString()
            invalidateAll()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.changeToolbar(TITLE)

        forgotPasswordButton.setOnClickListener {
            listener?.showFragment(ForgotPasswordFragment())
        }
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
        fun signIn()
    }

    companion object {
        const val TITLE = "Iniciar Sesión"
    }
}