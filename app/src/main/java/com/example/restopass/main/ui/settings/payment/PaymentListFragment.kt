package com.example.restopass.main.ui.settings.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.restopass.R
import com.example.restopass.common.orElse
import com.example.restopass.connection.Api4xxException
import com.example.restopass.main.MainActivity
import com.example.restopass.main.common.AlertBody
import com.example.restopass.main.common.AlertDialog
import com.example.restopass.utils.AlertDialogUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_payment_list.*
import kotlinx.coroutines.*
import timber.log.Timber

class PaymentListFragment : Fragment() {
    private lateinit var paymentViewModel: PaymentViewModel

    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentViewModel = ViewModelProvider(requireActivity()).get(PaymentViewModel::class.java)

        paymentListComponent.visibility = View.GONE
        paymentListLoader.visibility = View.VISIBLE

        if (paymentViewModel.creditCard == null) {
            coroutineScope.launch {
                getCreditCard()
            }
        } else {
            onPaymentResponse()
        }
    }

    private fun setView() {
        (activity as MainActivity).topAppBar?.apply {
            setTitle(R.string.payment_methods)
            visibility = View.VISIBLE
        }

        addCreditCardButton.apply {
            isEnabled = false
            setOnClickListener {
                findNavController().navigate(R.id.paymentFragment)
            }
        }

        deleteCreditCardButton.setOnClickListener {
            AlertDialog.getActionDialog(context, layoutInflater,
                paymentFragmentList, ::deleteCreditCard,  AlertBody(getString(R.string.deleteCreditCardTitle))).show()

        }
    }

    private fun deleteCreditCard() {
        paymentListComponent.visibility = View.GONE
        paymentListLoader.visibility = View.VISIBLE
        coroutineScope.launch {
            try {
                paymentViewModel.delete()

                findNavController().navigate(PaymentListFragmentDirections.actionPaymentListFragmentToEmptyPaymentFragment())
            } catch (e: Exception) {
                if (isActive) {
                    Timber.e(e)
                    paymentListLoader.visibility = View.GONE
                    paymentListComponent.visibility = View.VISIBLE

                    AlertDialogUtils.buildAlertDialog(e, layoutInflater,
                        paymentFragmentList, view).show()
                }
            }
        }
    }

    private suspend fun getCreditCard() {
        withContext(coroutineScope.coroutineContext) {
            try {
                paymentViewModel.get()
                onPaymentResponse()

            } catch (e: Api4xxException) {
                if (e.error?.code == 40405) onPaymentResponse()
                else resolveException(e)
            } catch (e: Exception) {
                if (isActive) {
                    resolveException(e)
                }
            }
            return@withContext
        }
    }

    private fun onPaymentResponse() {
        paymentViewModel.creditCard?.let {
            creditCardOwner?.text = it.holderName
            creditCardDescription?.text =
                resources.getString(R.string.creditCardDescription, it.lastFourDigits)
            creditCardImage?.setImageResource(it.image())

            setView()
            paymentListComponent.visibility = View.VISIBLE
            paymentListLoader.visibility = View.GONE
        }.orElse {
            findNavController().navigate(PaymentListFragmentDirections.actionPaymentListFragmentToEmptyPaymentFragment())
        }
    }

    private fun resolveException(e: Exception) {
        Timber.e(e)
        paymentListLoader.visibility = View.GONE

        AlertDialogUtils.buildAlertDialog(e, layoutInflater, container, view).show()
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
        (activity as MainActivity).setBackBehaviour()
    }

}