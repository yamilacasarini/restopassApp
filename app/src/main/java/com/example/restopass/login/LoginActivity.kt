package com.example.restopass.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.login.domain.LoginResponse
import com.example.restopass.login.signin.ForgotPasswordFragment
import com.example.restopass.login.signin.SignInFragment
import com.example.restopass.login.signup.SignUpStepOneFragment
import com.example.restopass.login.signup.SignUpStepTwoFragment
import com.example.restopass.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber


class LoginActivity : AppCompatActivity(),
    LoginFragment.OnFragmentInteractionListener,
    SignInFragment.OnFragmentInteractionListener,
    SignUpStepOneFragment.OnFragmentInteractionListener,
    SignUpStepTwoFragment.OnFragmentInteractionListener,
    ForgotPasswordFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppPreferences.setup(applicationContext)

        if (userIsLogged()) {
            startMainActicity()
        }

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

    override fun signUp(loginResponse: LoginResponse) {
        AppPreferences.apply {
            accessToken = loginResponse.xAuthToken
            refreshToken = loginResponse.xRefreshToken
            user = loginResponse.user
        }

        startMainActicity()
    }

    override fun signIn(loginResponse: LoginResponse) {
        AppPreferences.apply {
            accessToken = loginResponse.xAuthToken
            refreshToken = loginResponse.xRefreshToken
            user = loginResponse.user
        }

       startMainActicity()
    }

    private fun startMainActicity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun userIsLogged(): Boolean {
        AppPreferences.apply {
            val accessToken = this.accessToken
            val refreshToken = this.refreshToken

            return accessToken != null && refreshToken != null
        }
    }

}
