package com.example.restopass.main.ui.settings.payment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.airbnb.paris.extensions.style
import com.example.restopass.R
import com.example.restopass.common.orElse
import com.example.restopass.domain.CreditCard
import com.example.restopass.domain.Membership
import com.example.restopass.domain.MembershipsViewModel
import com.example.restopass.main.MainActivity
import com.example.restopass.main.common.AlertDialog
import com.example.restopass.utils.AlertDialogUtils
import io.stormotion.creditcardflow.CardFlowState
import io.stormotion.creditcardflow.CreditCardFlowListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_payment.*
import kotlinx.coroutines.*
import timber.log.Timber


class PaymentFragment : Fragment() {
    lateinit var imgr: InputMethodManager

    private lateinit var paymentViewModel: PaymentViewModel
    private lateinit var membershipsViewModel: MembershipsViewModel

    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentViewModel = ViewModelProvider(requireActivity()).get(PaymentViewModel::class.java)

        membershipsViewModel = ViewModelProvider(requireActivity()).get(MembershipsViewModel::class.java)

        imgr = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)

        (activity as MainActivity).topAppBar?.apply {
            setTitle(R.string.payment_methods)
            visibility = View.VISIBLE
            setNavigationOnClickListener {
                if (creditCardComponent.currentState() in listOf(CardFlowState.INACTIVE_CARD_NUMBER, CardFlowState.ACTIVE_CARD_NUMBER)) {
                    imgr.hideSoftInputFromWindow(view.windowToken, 0)
                    view.findNavController().popBackStack()
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

            }

            override fun onCardNumberValidationFailed(cardNumber: String) {

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
                insert(CreditCard(holderName = holderName!!, lastFourDigits = creditCard.number!!.takeLast(4), type = creditCardComponent.creditCardType().toString()))
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

                membershipsViewModel.selectedUpdateMembership?.let {
                    updateMembership(it)
                }.orElse {
                    findNavController().navigate(PaymentFragmentDirections.actionPaymentFragmentToPaymentListFragment())
                }
            } catch (e: Exception) {
                if(isActive) {
                    Timber.e(e)
                    creditCardLoader.visibility = View.GONE
                    creditCardComponent.visibility = View.VISIBLE

                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container).show()
                }
            }
        }
    }

    private fun updateMembership(membership: Membership) {
        creditCardComponent.visibility = View.GONE
        creditCardLoader.visibility = View.VISIBLE
        coroutineScope.launch {
            try {
                membershipsViewModel.update(membership)
                findNavController().navigate(R.id.navigation_enrolled_home)
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    creditCardLoader.visibility = View.GONE
                    creditCardComponent.visibility = View.VISIBLE
                    AlertDialogUtils.buildAlertDialog(e, layoutInflater, container).show()
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
        imgr.hideSoftInputFromWindow(requireView().windowToken, 0)
       (activity as MainActivity).setBackBehaviour()
    }
}