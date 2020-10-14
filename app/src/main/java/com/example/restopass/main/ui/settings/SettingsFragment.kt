package com.example.restopass.main.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment(), SettingAdapterListener {
    private lateinit var recyclerView: RecyclerView

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
        val viewAdapter : SettingsAdapter = if(AppPreferences.user.actualMembership != null) {
            SettingsAdapter(SettingsList.settingsItems, this)
        } else {
            SettingsAdapter(SettingsList.settingsItems.minusSetting(ButtonSettingType.MY_VISITS), this)
        }

        logoutButton.setOnClickListener {
            settingsRecyclerView.visibility = View.GONE
            logoutButton.visibility = View.GONE
            membershipLoader.visibility = View.VISIBLE

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
            ButtonSettingType.MY_VISITS to R.id.myVisitsFragment,
            ButtonSettingType.PAYMENT_METHODS to R.id.paymentListFragment,
            ButtonSettingType.PERSONAL_INFO to R.id.personalInfoFragment,
            ButtonSettingType.COMMUNICATIONS_SETTINGS to R.id.communicationsFragment,
            ButtonSettingType.BOOKING_HISTORY to R.id.historyReservationFragment,
            ButtonSettingType.NOTIFICATIONS to R.id.notificationsFragment
        )
    }

}
