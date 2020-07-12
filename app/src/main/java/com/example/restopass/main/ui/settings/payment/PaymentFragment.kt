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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.airbnb.paris.extensions.style
import com.example.restopass.R
import com.example.restopass.domain.CreditCard
import com.example.restopass.main.MainActivity
import com.example.restopass.main.common.AlertDialog
import io.stormotion.creditcardflow.CardFlowState
import io.stormotion.creditcardflow.CreditCardFlowListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.coroutines.*
import timber.log.Timber


class PaymentFragment : Fragment() {
    lateinit var imgr: InputMethodManager
    private lateinit var paymentViewModel: PaymentViewModel

    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)

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

        paymentViewModel = ViewModelProvider(requireActivity()).get(PaymentViewModel::class.java)

        imgr = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        (activity as MainActivity).topAppBar?.apply {
            setTitle(R.string.payment_methods)
            visibility = View.VISIBLE
            setNavigationOnClickListener {
                if (creditCardComponent.currentState() in listOf(CardFlowState.INACTIVE_CARD_NUMBER, CardFlowState.ACTIVE_CARD_NUMBER)) {
                    imgr.hideSoftInputFromWindow(view.windowToken, 0)
                    findNavController().popBackStack()
                } else {
                   creditCardComponent.previousState()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (creditCardComponent.currentState() in listOf(CardFlowState.INACTIVE_CARD_NUMBER, CardFlowState.ACTIVE_CARD_NUMBER)) {
                findNavController().popBackStack()
            } else {
                creditCardComponent.previousState()
            }
        }

        creditCardComponent.apply {
            getCardNumberInputEditText().style(R.style.paymentInputText)
            getCardExpiryDateInputEditText().style(R.style.paymentInputText)
            getCardHolderInputEditText().style(R.style.paymentInputText)
            getCvvInputEditText().style(R.style.paymentInputText)
        }


        creditCardComponent.setCreditCardFlowListener(object : CreditCardFlowListener {
            override fun onActiveCardNumberBeforeChangeToNext() {
            }

            override fun onActiveCardNumberBeforeChangeToPrevious() {

            }

            override fun onCardCvvBeforeChangeToNext() {

            }

            override fun onCardCvvBeforeChangeToPrevious() {

            }


            override fun onCardNumberValidatedSuccessfully(cardNumber: String) {
               // AlertDialog.getAndroidAlertDialog(context,  layoutInflater.inflate(R.layout.alert_dialog_title, container, false))
            }

            override fun onCardNumberValidationFailed(cardNumber: String) {
                //AlertDialog.getAndroidAlertDialog(context,  layoutInflater.inflate(R.layout.alert_dialog_title, container, false)).show()
            }

            override fun onCardHolderValidatedSuccessfully(cardHolder: String) {
            }

            override fun onCardHolderValidationFailed(cardholder: String) {
            }

            override fun onCardExpiryDateValidatedSuccessfully(expiryDate: String) {
            }

            override fun onCardExpiryDateValidationFailed(expiryDate: String) {
            }

            override fun onCardHolderBeforeChangeToNext() {

            }

            override fun onCardHolderBeforeChangeToPrevious() {

            }

            override fun onCardExpiryDateInThePast(expiryDate: String) {
            }

            override fun onCardCvvValidatedSuccessfully(cvv: String) {
            }

            override fun onCardCvvValidationFailed(cvv: String) {
            }

            override fun onCardExpiryDateBeforeChangeToNext() {

            }

            override fun onCardExpiryDateBeforeChangeToPrevious() {
            }

            override fun onCreditCardFlowFinished(creditCard: io.stormotion.creditcardflow.CreditCard) {
                imgr.hideSoftInputFromWindow(view.windowToken, 0)
                val holderName = creditCard.cvc //increíblemente está mal matcheado
                insert(CreditCard(holderName = holderName!!, number = creditCard.number!!))
            }

            override fun onFromActiveToInactiveAnimationStart() {

            }

            override fun onFromInactiveToActiveAnimationStart() {

            }

            override fun onInactiveCardNumberBeforeChangeToNext() {

            }

            override fun onInactiveCardNumberBeforeChangeToPrevious() {

            }
        })
    }

    private fun insert(creditCard: CreditCard) {
        creditCardComponent.visibility = View.GONE
        creditCardLoader.visibility = View.VISIBLE
        coroutineScope.launch {
            try {
                paymentViewModel.insert(creditCard)

                findNavController().navigate(R.id.paymentListFragment)
            } catch (e: Exception) {
                if(isActive) {
                    Timber.e(e)
                    creditCardLoader.visibility = View.GONE
                    creditCardComponent.visibility = View.VISIBLE

                    val titleView: View =
                        layoutInflater.inflate(R.layout.alert_dialog_title, container, false)
                    AlertDialog.getAndroidAlertDialog(
                        context,
                        titleView
                    ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        (activity as MainActivity).setBackBehaviour()
    }
}