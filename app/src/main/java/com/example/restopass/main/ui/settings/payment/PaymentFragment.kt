package com.example.restopass.main.ui.settings.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import com.example.restopass.R
import io.stormotion.creditcardflow.CardFlowState
import kotlinx.android.synthetic.main.fragment_payment.*


class PaymentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentTopAppBar.setNavigationOnClickListener {
            if (creditCardComponent.currentState() in listOf(CardFlowState.INACTIVE_CARD_NUMBER, CardFlowState.ACTIVE_CARD_NUMBER)) {
                activity?.finish()
            } else {
               creditCardComponent.previousState()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (creditCardComponent.currentState() in listOf(CardFlowState.INACTIVE_CARD_NUMBER, CardFlowState.ACTIVE_CARD_NUMBER)) {
                activity?.finish()
            } else {
                creditCardComponent.previousState()
            }
        }


        creditCardComponent.setInputCreditCardNumberStyle(R.style.creditCardInput)
    }
}