package com.example.restopass.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.restopass.R
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = TITLE
            show()
        }

        Glide.with(this).load(R.drawable.restopass).into(restoPassImage)


        googleSignInButton.setOnClickListener {
                listener?.onGoogleSignInClick()
        }

        restoPassSignInButton.setOnClickListener {
            findNavController().navigate(R.id.signInFragment)
        }
        signUpButton.setOnClickListener {
            findNavController().navigate(R.id.signUpStepOneFragment)
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
        fun onGoogleSignInClick()
    }

    companion object {
        const val TITLE = "RestoPass"
    }
}