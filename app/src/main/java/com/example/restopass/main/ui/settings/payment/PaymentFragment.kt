package com.example.restopass.main.ui.settings.payment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.restopass.R
import io.stormotion.creditcardflow.CardFlowState
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.fragment_payment.*


class PaymentFragment : Fragment() {
    lateinit var imgr: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imgr = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        (activity as PaymentActivity).paymentTopAppBar.setNavigationOnClickListener {
            if (creditCardComponent.currentState() in listOf(CardFlowState.INACTIVE_CARD_NUMBER, CardFlowState.ACTIVE_CARD_NUMBER)) {
                imgr.hideSoftInputFromWindow(view.windowToken, 0)
                findNavController().popBackStack()
            } else {
               creditCardComponent.previousState()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (creditCardComponent.currentState() in listOf(CardFlowState.INACTIVE_CARD_NUMBER, CardFlowState.ACTIVE_CARD_NUMBER)) {
                findNavController().popBackStack()
            } else {
                creditCardComponent.previousState()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as PaymentActivity).setBackBehaviour()
    }
}