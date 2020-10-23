package com.example.restopass.main.common.restaurant.comment

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.domain.Comment
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.android.synthetic.main.welcome_membership_modal.view.*
import java.time.Duration
import java.time.LocalDateTime

class CommentAdapter(
    var comments: List<Comment> = listOf()
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    class CommentViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = comments.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]

        holder.itemView.apply {
            authorName.text = comment.user.name

            dishOrder.text =  Html.fromHtml(resources.getString(R.string.dishOrder, comment.dish.name))
            dishRating.rating = comment.dishStars

            commentDescription.text = comment.description
            restaurantRating.rating = comment.restaurantStars

            dateComment.text =  Html.fromHtml(resources.getString(R.string.sinceComment, Duration.between(LocalDateTime.parse(comment.date), LocalDateTime.now()).toDays().toString()))

            Glide.with(this).load(comment.dish.img).into(dishCommentImage)
        }
    }

}