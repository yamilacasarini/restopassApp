package com.example.restopass.main.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.restopass.R
import com.example.restopass.common.AppPreferences
import com.example.restopass.common.orElse

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onStart() {
        super.onStart()
        AppPreferences.user.actualMembership?.let{
            view?.findNavController()?.navigate(R.id.enrolledHomeFragment)
        }.orElse {
            view?.findNavController()?.navigate(R.id.notEnrolledHomeFragment)
        }
    }
}

