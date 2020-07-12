package com.example.restopass.main.ui.settings.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.restopass.R
import com.example.restopass.common.orElse
import com.example.restopass.main.MainActivity
import com.example.restopass.main.common.AlertDialog
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

        (activity as MainActivity).topAppBar?.apply {
            setTitle(R.string.payment_methods)
            visibility = View.VISIBLE
            setNavigationOnClickListener {
                view.findNavController().navigate(R.id.navigation_settings)
            }
        }

        paymentViewModel = ViewModelProvider(requireActivity()).get(PaymentViewModel::class.java)

        paymentViewModel.creditCard?.let {
            creditCardOwner.text = it.holderName
            creditCardDescription.text =
                resources.getString(R.string.creditCardDescription, it.number.takeLast(4))

            addCreditCardButton.isEnabled = false
        }.orElse {
            findNavController().navigate(PaymentListFragmentDirections.actionPaymentListFragmentToEmptyPaymentFragment())
        }

        addCreditCardButton.setOnClickListener {
            findNavController().navigate(R.id.paymentFragment)
        }

        deleteCreditCardButton.setOnClickListener {
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
    }

    override fun onStop() {
        super.onStop()
        job.cancel()
        (activity as MainActivity).setBackBehaviour()
    }

}