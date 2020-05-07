package com.example.restopass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class LoginFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//
//        // Snippet from "Navigate to the next Fragment" section goes here.
//
//        //val view = inflater.inflate(R.layout.shr_login_fragment, container, false)
//
////        view.next_button.setOnClickListener {
////            if (!isPasswordValid(password_edit_text.text!!)) {
////                password_text_input.error = getString(R.string.shr_error_password)
////            } else {
////                password_edit_text.error = null
////                // Navigate to the next Fragment.
////                (activity as NavigationHost).navigateTo(ProductGridFragment(), true)
////            }
////        }
////
////        // Clear the error once more than 8 characters are typed.
////        view.password_edit_text.setOnKeyListener { _, _, _ ->
////            if (isPasswordValid(password_edit_text.text!!)) {
////                // Clear the error.
////                password_text_input.error = null
////            }
////            false
//        //}
//        //return view
//    }

    interface OnFragmentInteractionListener {
        fun showFragment(fragment: Fragment)
    }
}