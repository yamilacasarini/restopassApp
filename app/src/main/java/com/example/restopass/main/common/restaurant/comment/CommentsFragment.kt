package com.example.restopass.main.common.restaurant.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.domain.*
import kotlinx.android.synthetic.main.fragment_comment.*

class CommentsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter

    private lateinit var restaurantViewModel: RestaurantViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurantViewModel = ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        commentAdapter = CommentAdapter(restaurantViewModel.restaurant.comments)
        commentAdapter.notifyDataSetChanged()

        recyclerView = commentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = commentAdapter
        }

        (activity as AppCompatActivity).supportActionBar?.apply {
            title = resources.getString(R.string.commentsTitle)
            show()
        }

    }
}