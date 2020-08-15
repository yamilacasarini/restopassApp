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
import com.example.restopass.databinding.FragmentForgotPasswordBinding
import com.example.restopass.login.domain.SignInViewModel
import com.example.restopass.utils.AlertDialogUtils
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import kotlinx.coroutines.*
import timber.log.Timber

class ForgotPasswordFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var viewModel: SignInViewModel
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_forgot_password,
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

        sendEmailButton.setOnClickListener {
            viewModel.email = forgotPasswordEmailInput.text.toString()
            recoverPassword()
        }

    }

    private fun recoverPassword() {
        forgotPasswordLoader.visibility = View.VISIBLE
        forgotPasswordEmailInput.isEnabled = false
        sendEmailButton.isEnabled = false
        coroutineScope.launch {
            try {
                viewModel.recoverPassword()
                listener?.showFragment(CodeRecoverPasswordFragment())
            } catch (e: Exception) {
                if (isActive) {
                    forgotPasswordLoader.visibility = View.GONE
                    forgotPasswordEmailInput.isEnabled = true
                    sendEmailButton.isEnabled = true
                    Timber.e(e)
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, forgotPasswordContainer).show()
                }
            }
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
        fun changeToolbar(fragmentName: String)
        fun showFragment(fragment: Fragment)
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

    companion object {
        const val TITLE = "Olvidé mi contraseña"
    }
}