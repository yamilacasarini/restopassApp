package com.example.restopass.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.connection.RestoPassException
import com.example.restopass.login.domain.LoginResponse
import com.example.restopass.login.signin.ForgotPasswordFragment
import com.example.restopass.login.signin.SignInFragment
import com.example.restopass.login.signup.SignUpStepOneFragment
import com.example.restopass.login.signup.SignUpStepTwoFragment
import com.example.restopass.main.MainActivity
import com.example.restopass.main.common.AlertDialog
import com.example.restopass.service.LoginService
import com.example.restopass.utils.AlertDialogUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_signup_step_two.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.lang.RuntimeException


class LoginActivity : AppCompatActivity(),
    LoginFragment.OnFragmentInteractionListener,
    SignInFragment.OnFragmentInteractionListener,
    SignUpStepOneFragment.OnFragmentInteractionListener,
    SignUpStepTwoFragment.OnFragmentInteractionListener,
    ForgotPasswordFragment.OnFragmentInteractionListener {

    private val job = Job()
    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var touchables: List<View>

    lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        AppPreferences.setup(this)

        if (userIsLogged()) {
            startMainActicity()
        }


        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(SERVER_CLIENT_ID)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions)

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

    override fun onGoogleSignInClick() {
        mGoogleSignInClient.signOut()
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            account?.idToken?.let {
                googleSignIn(it, this)
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.w("signInResult:failed code=${e.statusCode}")
        }
    }

    private fun googleSignIn(token: String, activity: LoginActivity) {
        toggleButtons()
        loginLoader.visibility = View.VISIBLE

        coroutineScope.launch {
            try {
                val user = LoginService.googleSignIn(token)
                onSignIn(user)
            } catch (e: Exception) {
                toggleButtons()
                loginLoader.visibility = View.GONE

                AlertDialogUtils.buildAlertDialog(e, layoutInflater, loginContainer).show()
            }
        }
    }

    private fun toggleButtons() {
        buttons.forEach {
            findViewById<View>(it).isEnabled = !findViewById<View>(it).isEnabled
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
        attachInformation(loginResponse)
        startMainActicity(loginResponse.creation)
    }

    override fun onSignIn(loginResponse: LoginResponse) {
        attachInformation(loginResponse)
        startMainActicity(loginResponse.creation)
    }

    private fun attachInformation(loginResponse: LoginResponse) {
        AppPreferences.apply {
            accessToken = loginResponse.xAuthToken
            refreshToken = loginResponse.xRefreshToken
            user = loginResponse.user
        }

        FirebaseMessaging.getInstance().subscribeToTopic(loginResponse.user.firebaseTopic)
            .addOnCompleteListener { task ->
                val email = loginResponse.user.email
                if (!task.isSuccessful) {
                    Timber.e("The user $email could not be subscribed to own topic")
                } else {
                    Timber.i("The user $email was successfully subscribed to own topic")
                }
            }
    }

    private fun startMainActicity(signUp: Boolean = false) {
        val intent = Intent(this, MainActivity::class.java)
        if (signUp) {
            intent.putExtra("signUp", true)
        }
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

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

    companion object {
        private const val RC_SIGN_IN = 100;
        private const val SERVER_CLIENT_ID =
            "166101057214-5br79m1tuvgfudrrmanmpo5l4se67q6s.apps.googleusercontent.com"

        private val buttons =
            listOf(R.id.googleSignInButton, R.id.restoPassSignInButton, R.id.signUpButton)
    }
}
