package com.example.restopass.main.ui.settings.myvisits

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.domain.MembershipsViewModel
import com.example.restopass.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_my_visits.view.*
import java.time.LocalDateTime

class MyVisitsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_my_visits, container, false)
    }

    private lateinit var membershipsViewModel: MembershipsViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        membershipsViewModel =
            ViewModelProvider(requireActivity()).get(MembershipsViewModel::class.java)

        (activity as MainActivity).topAppBar?.apply {
            setTitle(R.string.myVisitsTitle)
            visibility = View.VISIBLE
        }

        view.apply {
            visitsAmount.text = AppPreferences.user.visits.toString()
            myVisitsExpiration.text = context.getString(R.string.myVisitsExpiration, calculateExpirationDate(AppPreferences.user.membershipEnrolledDate!!))
            myVisitsMembershipText.text = Html.fromHtml(context.getString(R.string.myVisitsMembership, membershipsViewModel.actualMembership!!.name, membershipsViewModel.actualMembership!!.visits.toString()))
            myVisitsMembershipBtn.setOnClickListener{
                findNavController().navigate(R.id.membershipsFragment)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateExpirationDate(date: String): String? {
        val dt = LocalDateTime.parse(date).plusDays(30)
        return "${dt.dayOfMonth}/${dt.monthValue}/${dt.year}"
    }

}