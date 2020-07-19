package com.example.restopass.main.ui.settings.personalInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.main.common.restaurant.DishAdapter
import kotlinx.android.synthetic.main.secondary_email_item.view.*

class SecondaryEmailAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var emails: List<String> = listOf()

    class EmailViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = emails.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.secondary_email_item, parent, false)
        return EmailViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val email = emails[position]

        holder.itemView.secondaryEmailInput.setText(email)
    }

}