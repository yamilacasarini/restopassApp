package com.example.restopass.main.common.restaurant.comment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.domain.RestaurantViewModel
import com.example.restopass.main.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_comment.*
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.roundToInt


class CommentsFragment : Fragment(), AdapterView.OnItemSelectedListener {
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        restaurantViewModel = ViewModelProvider(requireActivity()).get(RestaurantViewModel::class.java)

        commentAdapter = CommentAdapter(restaurantViewModel.restaurant.comments!!)
        commentAdapter.notifyDataSetChanged()

        recyclerView = commentsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this.context)
            adapter = commentAdapter
        }

        val rating = restaurantViewModel.restaurant.comments!!.map { it.restaurantStars }.sum() / restaurantViewModel.restaurant.comments!!.size
        val ratingText = resources.getStringArray(R.array.commentsRatingDescriptions)[rating.roundToInt() - 1]
        val bestPlate  = restaurantViewModel.restaurant.comments!!.groupBy { it.dish.name }
            .mapValues { it.value.map { it.dishStars }.average()}.maxBy { it.value }?.key
        val mostOrderPlate = restaurantViewModel.restaurant.comments!!.groupBy { it.dish.name }.mapValues { it.value.size }.maxBy { it.value }?.key

        view.apply {
            Glide.with(this).load(restaurantViewModel.restaurant.img).into(commentsRestaurantImg)
            commentsRestaurantTitle.text = restaurantViewModel.restaurant.name
            commentsTitle.text = context.getString(R.string.commentsTitle)
            commentsSubtitle.text = Html.fromHtml(context.getString(R.string.commentsSubtitle,ratingText,restaurantViewModel.restaurant.comments!!.size.toString()))
            commentsRating.text = String.format("%.1f", rating)
            commentsBestPlate.text = bestPlate
            commentsMostOrderPlate.text = mostOrderPlate
            commentsProgressCircle.setProgress(applySpinnerRange(rating).toInt(), true)

            ArrayAdapter.createFromResource(
                context,
                R.array.commentsSpinnerList,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                commentsSpinner.adapter = adapter
            }

            commentsSpinner.onItemSelectedListener = this@CommentsFragment

        }

        (activity as MainActivity).mainBackButton.visibility = View.VISIBLE

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when(pos) {
           0 -> commentAdapter.comments = restaurantViewModel.restaurant.comments!!.sortedBy { LocalDate.parse(it.date) }.reversed()
            1 -> commentAdapter.comments = restaurantViewModel.restaurant.comments!!.sortedBy { LocalDate.parse(it.date) }
            2 -> commentAdapter.comments = restaurantViewModel.restaurant.comments!!.sortedBy { it.dishStars }.reversed()
            3 -> commentAdapter.comments = restaurantViewModel.restaurant.comments!!.sortedBy { it.restaurantStars }.reversed()
        }
        commentAdapter.notifyDataSetChanged()
    }

    private fun applySpinnerRange(oldValue: Float) : Float {
        return oldValue * 20
    }
}