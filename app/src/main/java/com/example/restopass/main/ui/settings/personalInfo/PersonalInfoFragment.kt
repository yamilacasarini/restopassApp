package com.example.restopass.main.ui.settings.personalInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.restopass.R
import com.example.restopass.databinding.FragmentPersonalInfoBinding

class PersonalInfoFragment : Fragment() {

    private lateinit var viewModel: PersonalInfoViewModel
    private lateinit var binding: FragmentPersonalInfoBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_personal_info,
            container,
            false
        )

        viewModel = ViewModelProvider(requireActivity()).get(PersonalInfoViewModel::class.java)

        binding.personalInfoViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}