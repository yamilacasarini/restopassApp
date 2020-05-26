package com.example.restopass.main.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.login.LoginActivity
import com.example.restopass.main.MainActivity
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    private val settingsViewModel: SettingsViewModel = SettingsViewModel()

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
            SettingsAdapter(settingsViewModel.settingsItems, SettingListener { settingType ->
                if (settingType === ButtonSettingType.PLAN) view.findNavController()
                    .navigate(R.id.membershipFragments)
            })

        logoutButton.setOnClickListener {
            my_recycler_view.visibility = View.GONE
            logoutButton.visibility = View.GONE
            loader.visibility = View.VISIBLE
            AppPreferences.removeAllPreferences()
            val intent = Intent(this.context, LoginActivity::class.java)
            this.requireContext().startActivity(intent)
            requireActivity().finish()
        }

        recyclerView = my_recycler_view.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

}
