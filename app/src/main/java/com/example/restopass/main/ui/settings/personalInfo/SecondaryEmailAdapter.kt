package com.example.restopass.main.ui.settings.personalInfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import kotlinx.android.synthetic.main.secondary_email_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SecondaryEmailAdapter(val listener: SecondaryEmailListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var emails: List<String> = listOf()

    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)


    class EmailViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = emails.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.secondary_email_item, parent, false)
        return EmailViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val email = emails[position]

        holder.itemView.apply {
            secondaryEmailInput.text = email
            deleteEmailButton.setOnClickListener {
                coroutineScope.launch {
                    listener.onDeleteEmailClick(email)
                }
            }
        }
    }
    
    interface SecondaryEmailListener {
        suspend fun onDeleteEmailClick(email: String)
    }

}