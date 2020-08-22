package com.example.restopass.login.signin

import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restopass.R
import com.example.restopass.login.domain.SignInViewModel
import com.example.restopass.utils.AlertDialogUtils
import kotlinx.android.synthetic.main.fragment_token_recover_password.*
import kotlinx.coroutines.*
import timber.log.Timber


class TokenRecoverPasswordFragment : Fragment() {

    var job = Job()
    var coroutineScope = CoroutineScope(job + Dispatchers.Main)

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_token_recover_password, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = TITLE
            show()
        }

        viewModel = ViewModelProvider(requireActivity()).get(SignInViewModel::class.java)

        emailSentTitle.text = Html.fromHtml(getString(R.string.tokenRecoverPasswordTitle, viewModel.email))

        verifyTokenButton.setOnClickListener {
            val digits = DIGITS_INPUT.joinToString("") {
                view.findViewById<EditText>(it).text.toString()
            }
            this.verifyRecoverPassword(digits)
        }

        DIGITS_INPUT.forEachIndexed { index, id ->
            view.findViewById<EditText>(id).addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    if (s.isNotBlank() && index != DIGITS_INPUT.size - 1) {
                        val nextInputId = DIGITS_INPUT[index + 1]
                        view.findViewById<EditText>(nextInputId).requestFocus()
                    }

                    verifyTokenButton.isEnabled = areInputsFilled()
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }
            })
        }
    }

    private fun areInputsFilled(): Boolean {
        return DIGITS_INPUT.all {
            requireView().findViewById<EditText>(it).text.isNotBlank()
        }

    }

    private fun verifyRecoverPassword(token: String) {
        tokenRecoverPasswordLoader.visibility = View.VISIBLE
        verifyTokenButton.isEnabled = false
        toggleEnabledInputs()
        coroutineScope.launch {
            try {
                viewModel.verifyRecoverPassword(token)
                findNavController().navigate(R.id.recoverPasswordFragment)
            } catch (e: Exception) {
                if (isActive) {
                    tokenRecoverPasswordLoader.visibility = View.GONE
                    verifyTokenButton.isEnabled = true
                    toggleEnabledInputs()
                    Timber.e(e)
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, tokenRecoverPasswordContainer).show()
                }
            }
        }
    }

    private fun toggleEnabledInputs() {
        DIGITS_INPUT.forEach {
            val input = view?.findViewById<EditText>(it)
            input!!.isEnabled = !input.isEnabled
        }
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
    }

    companion object {
        const val TITLE = "Recuperar contraseña"
        val DIGITS_INPUT = listOf(
            R.id.digitOne,
            R.id.digitTwo,
            R.id.digitThree,
            R.id.digitFour
        )
    }
}