package com.example.restopass.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.restopass.R
import com.example.restopass.login.signin.ForgotPasswordFragment
import com.example.restopass.login.signin.SignInFragment
import com.example.restopass.login.signup.SignUpFragment
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber

class LoginActivity : AppCompatActivity(),
    LoginFragment.OnFragmentInteractionListener,
    SignInFragment.OnFragmentInteractionListener,
    SignUpFragment.OnFragmentInteractionListener,
    ForgotPasswordFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate started")
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    LoginFragment()
                )
                .commit()
        }
    }

    override fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun changeToolbar(fragmentName: String) {
        toolbar.title = fragmentName
    }
}
