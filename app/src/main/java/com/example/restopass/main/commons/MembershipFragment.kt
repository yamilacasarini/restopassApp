package com.example.restopass.main.commons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import kotlinx.android.synthetic.main.fragment_membership.*
import kotlinx.android.synthetic.main.view_membership_item.view.*

class MembershipFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    private val viewModel: MembershipViewModel = MembershipViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_membership, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewManager = LinearLayoutManager(this.context)
        val viewAdapter = MembershipAdapter(viewModel.membershipsList, MembershipListener { membership ->
            Toast.makeText(context, "Se tocó a: ${membership!!.type}", Toast.LENGTH_LONG).show()
            membership.restaurantsVisibility = if (membership.restaurantsVisibility == View.GONE) View.VISIBLE else View.GONE
            membershipRecyclerView.card.restaurantsList.visibility = membership.restaurantsVisibility

        })

        recyclerView = membershipRecyclerView.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}