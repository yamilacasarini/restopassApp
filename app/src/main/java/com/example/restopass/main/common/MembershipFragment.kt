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
import com.example.restopass.service.RestopassApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_membership.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MembershipFragment : Fragment(), MembershipListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var membershipAdapter: MembershipAdapter

    //La tarea entera es cancelada cuando se destruye el fragmentt
    private var job = Job()
    //Se crea una coroutine como hija, por lo que la cancelación externa va a propagarse en los hijos
    private val coroutineScope = CoroutineScope(job + Main)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_membership, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        membershipAdapter = MembershipAdapter(this)
        recyclerView = membershipRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = membershipAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        loader.visibility = View.VISIBLE
        coroutineScope.launch {
            try {
                val listResult = RestopassApi.retrofitService.getMembershipsAsync().await()
                membershipAdapter.memberships = listResult.memberships
                membershipAdapter.notifyDataSetChanged()
                loader.visibility = View.GONE
                membershipRecyclerView.visibility = View.VISIBLE
            } catch (e: Exception) {
                loader.visibility = View.GONE

                val titleView: View = layoutInflater.inflate(R.layout.alert_dialog_title, container, false)
                AlertDialog.getAlertDialog(context, titleView, view).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onClick(membership: Membership) {
        Log.i("H", "holanda")
    }


}