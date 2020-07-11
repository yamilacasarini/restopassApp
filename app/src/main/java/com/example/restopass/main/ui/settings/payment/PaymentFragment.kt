package com.example.restopass.main.ui.settings.payment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.restopass.R
import com.example.restopass.main.common.AlertDialog
import io.stormotion.creditcardflow.CardFlowState
import io.stormotion.creditcardflow.CreditCardFlowListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.fragment_payment.*
import timber.log.Timber


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


        creditCardComponent.setCreditCardFlowListener(object : CreditCardFlowListener {
            override fun onActiveCardNumberBeforeChangeToNext() {
                Timber.i("VEamos este que onda")
                Toast.makeText(context, "RestaurantId not found", Toast.LENGTH_LONG).show()
                Timber.i("VEamos este que onda")
            }

            override fun onCardExpiryDateBeforeChangeToNext() {
            }

            override fun onCardHolderBeforeChangeToNext() {
            }

            override fun onFromActiveToInactiveAnimationStart() {

            }

            override fun onFromInactiveToActiveAnimationStart() {

            }

            override fun onCardCvvBeforeChangeToNext() {
            }

            override fun onActiveCardNumberBeforeChangeToPrevious() {

            }

            override fun onInactiveCardNumberBeforeChangeToNext() {
            }

            override fun onInactiveCardNumberBeforeChangeToPrevious() {

            }

            override fun onCardExpiryDateBeforeChangeToPrevious() {
            }

            override fun onCardHolderBeforeChangeToPrevious() {
            }

            override fun onCardCvvBeforeChangeToPrevious() {
            }

            override fun onCardNumberValidatedSuccessfully(cardNumber: String) {
                AlertDialog.getAndroidAlertDialog(context,  layoutInflater.inflate(R.layout.alert_dialog_title, container, false))
            }

            override fun onCardNumberValidationFailed(cardNumber: String) {
                AlertDialog.getAndroidAlertDialog(context,  layoutInflater.inflate(R.layout.alert_dialog_title, container, false)).show()
            }

            override fun onCardHolderValidatedSuccessfully(cardHolder: String) {
            }

            override fun onCardHolderValidationFailed(cardholder: String) {
            }

            override fun onCardExpiryDateValidatedSuccessfully(expiryDate: String) {
            }

            override fun onCardExpiryDateValidationFailed(expiryDate: String) {
            }

            override fun onCardExpiryDateInThePast(expiryDate: String) {
            }

            override fun onCardCvvValidatedSuccessfully(cvv: String) {
            }

            override fun onCardCvvValidationFailed(cvv: String) {
            }

            override fun onCreditCardFlowFinished(creditCard: io.stormotion.creditcardflow.CreditCard) {
                AlertDialog.getAndroidAlertDialog(context,  layoutInflater.inflate(R.layout.alert_dialog_title, container, false)).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as PaymentActivity).setBackBehaviour()
    }
}