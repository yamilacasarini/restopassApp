package com.example.restopass.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.restopass.R
import com.example.restopass.login.signin.SignInFragment
import com.example.restopass.login.signup.SignUpFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.changeToolbar(TITLE)
        restoPassSignInButton.setOnClickListener {
            listener?.showFragment(SignInFragment())
        }
        signUpButton.setOnClickListener {
            listener?.showFragment(SignUpFragment())
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

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun showFragment(fragment: Fragment)
        fun changeToolbar(fragmentName: String)
    }

    companion object {
        const val TITLE = "RestoPass"
    }
}