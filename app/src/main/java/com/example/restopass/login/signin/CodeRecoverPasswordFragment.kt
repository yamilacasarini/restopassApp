package com.example.restopass.login.signin

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.restopass.R
import com.example.restopass.login.domain.SignInViewModel
import kotlinx.android.synthetic.main.fragment_code_recover_password.*


class CodeRecoverPasswordFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_code_recover_password, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener?.changeToolbar(TITLE)

        viewModel = ViewModelProvider(requireActivity()).get(SignInViewModel::class.java)

        emailSentTitle.text = Html.fromHtml(getString(R.string.codeRecoverPasswordTitle, viewModel.email))

        DIGITS_INPUT.forEachIndexed { index, id ->
            view.findViewById<EditText>(id).addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    if (!s.isBlank() && index != DIGITS_INPUT.size - 1) {
                        val nextInputId = DIGITS_INPUT[index + 1]
                        view.findViewById<EditText>(nextInputId).requestFocus()
                    }
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