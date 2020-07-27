package com.example.restopass.main.ui.settings.communications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.restopass.R
import com.example.restopass.main.ui.settings.personalInfo.PersonalInfoViewModel
import kotlinx.android.synthetic.main.secondary_email_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SecondaryEmailAdapter(val listener: SecondaryEmailListener, private val personalInfoViewModel: PersonalInfoViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val job = Job()
    val coroutineScope = CoroutineScope(job + Dispatchers.Main)


    class EmailViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = personalInfoViewModel.personalInfo?.secondaryEmails!!.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.secondary_email_item, parent, false)
        return EmailViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val email = personalInfoViewModel.personalInfo!!.secondaryEmails[position]

        holder.itemView.apply {
            secondaryEmailTextView.text = email.email
            toConfirmTextView.visibility =  if (email.confirmed) View.GONE else View.VISIBLE
            deleteEmailButton.setOnClickListener {
                coroutineScope.launch {
                    listener.onDeleteEmailClick(email.email)
                }
            }
        }
    }
    
    interface SecondaryEmailListener {
        suspend fun onDeleteEmailClick(email: String)
    }

}