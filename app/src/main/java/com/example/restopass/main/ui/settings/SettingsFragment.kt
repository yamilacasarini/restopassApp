package com.example.restopass.main.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.domain.CreditCard
import com.example.restopass.main.ui.settings.payment.PaymentViewModel
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment(), SettingAdapterListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var paymentViewModel: PaymentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(this.context)
        val viewAdapter =
            SettingsAdapter(SettingsList.settingsItems, this)

        paymentViewModel = ViewModelProvider(requireActivity()).get(PaymentViewModel::class.java)
        paymentViewModel.creditCard = CreditCard(holderName = "Juaniyo", number = "123456789")

        logoutButton.setOnClickListener {
            settingsRecyclerView.visibility = View.GONE
            logoutButton.visibility = View.GONE
            notEnrolledLoader.visibility = View.VISIBLE

            FirebaseMessaging.getInstance().unsubscribeFromTopic(AppPreferences.user.firebaseTopic)

            AppPreferences.logout()
        }

        recyclerView = settingsRecyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onClick(type: ButtonSettingType) {
        settingsLayout[type]?.let {
            findNavController().navigate(it)
        }
    }

    companion object {
        val settingsLayout = mapOf(
            ButtonSettingType.PLAN to R.id.membershipsFragment,
            ButtonSettingType.PAYMENT_METHODS to R.id.paymentListFragment
        )
    }

}
