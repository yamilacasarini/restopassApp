package com.example.restopass.main.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import kotlinx.android.synthetic.main.fragment_membership.*

class MembershipFragment : Fragment(), MembershipListener {
    private lateinit var recyclerView: RecyclerView

    private val viewModel: MembershipViewModel = MembershipViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_membership, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewAdapter = MembershipAdapter(viewModel.membershipsList, this)

        recyclerView = membershipRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = viewAdapter
        }
    }

    override fun onClick(membership: Membership) {
        Log.i("H", "holanda")
    }
}