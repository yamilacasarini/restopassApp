package com.example.restopass.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.restopass.main.MainActivity
import com.example.restopass.R
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

    fun login(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
