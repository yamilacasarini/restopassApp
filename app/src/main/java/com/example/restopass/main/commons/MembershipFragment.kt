package com.example.restopass.main.commons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.R
import kotlinx.android.synthetic.main.fragment_membership.*

class MembershipFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    private val membershipViewModel: MembershipViewModel = MembershipViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_membership, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this).load(R.drawable.sushi).into(sushiImage1)
        Glide.with(this).load(R.drawable.sushi).into(sushiImage2)

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