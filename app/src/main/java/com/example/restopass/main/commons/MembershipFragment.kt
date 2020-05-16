package com.example.restopass.main.commons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.restopass.R
import kotlinx.android.synthetic.main.fragment_membership.*

class MembershipFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_membership, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expandButton.setOnClickListener {
            restaurantsList.let {
                if (it.visibility == View.VISIBLE) it.visibility = View.GONE
                else it.visibility = View.VISIBLE
            }
        }

        expandButton2.setOnClickListener {
            restaurantsList2.let {
                if (it.visibility == View.VISIBLE) it.visibility = View.GONE
                else it.visibility = View.VISIBLE
            }
        }
    }
}