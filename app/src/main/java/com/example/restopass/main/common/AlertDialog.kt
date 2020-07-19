package com.example.restopass.main.common

import android.content.Context
import android.content.res.Resources
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.restopass.R
import com.example.restopass.domain.Membership
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.about_restopass_modal.view.*
import kotlinx.android.synthetic.main.about_restopass_modal.view.stepOne
import kotlinx.android.synthetic.main.action_alert_dialog.view.*
import kotlinx.android.synthetic.main.welcome_membership_modal.view.*

class AlertBody(
    val title: String? = null,
    val description: String? = null,
    val actionText: Int? = null
)

object AlertDialog {
    fun getAlertDialog(context: Context?, body: View, view: View? = null, withButton: Boolean = true) : MaterialAlertDialogBuilder {
       val dialog = MaterialAlertDialogBuilder(context)
            .setCustomTitle(body)
           .setOnCancelListener {
               view?.findNavController()?.popBackStack()
           }
        if (withButton)
            dialog.setPositiveButton(R.string.accept)
            { _, _ ->
                view?.findNavController()?.popBackStack()
            }
        return dialog
    }


    private fun getInformativeDialog(context: Context?, body: View) : MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context)
            .setView(body)
    }

    fun getActionDialog(context: Context?, layoutInflater: LayoutInflater, container: ViewGroup, action:  () -> Unit, alertBody: AlertBody): MaterialAlertDialogBuilder {
        val body: View =
            layoutInflater.inflate(R.layout.action_alert_dialog, container, false)
        alertBody.title?.let { body.alertTitle.setText(it)}
        val dialog = MaterialAlertDialogBuilder(context)
            .setCustomTitle(body)
            dialog.setNegativeButton(R.string.cancelAlertMessage)
            { _,_ ->
            }
            dialog.setPositiveButton(alertBody.actionText ?: R.string.deleteAlertMessage)
            { _, _ ->
               action()
            }
        return dialog
    }

    fun getActionDialogWithParams(context: Context?, layoutInflater: LayoutInflater, container: ViewGroup, action:  (param: Any) -> Unit, param: Any, alertBody: AlertBody): MaterialAlertDialogBuilder {
        val body: View =
            layoutInflater.inflate(R.layout.action_alert_dialog, container, false)
        alertBody.run {
            title?.let { body.alertTitle.text = it  }
            Html.fromHtml(description)?.let { body.alertDescription.text = it }
        }


        val dialog = MaterialAlertDialogBuilder(context)
            .setCustomTitle(body)
        dialog.setNegativeButton(R.string.cancelAlertMessage)
        { _,_ ->
        }
        dialog.setPositiveButton(alertBody.actionText ?: R.string.deleteAlertMessage)
        { _, _ ->
            action(param)
        }
        return dialog
    }

    fun getAboutRestoPassModal(context: Context?, layoutInflater: LayoutInflater, container: ViewGroup?) {
        val view: View =
            layoutInflater.inflate(R.layout.about_restopass_modal, container, false)

        val alertDialog = getInformativeDialog(
            context,
            view
        ).show()

        // FIRST STEP
        view.skipButton.setOnClickListener {
            alertDialog.dismiss()
        }

        view.continueButton.setOnClickListener {
            view.stepOne.visibility = View.GONE
            view.stepTwo.visibility = View.VISIBLE
        }

        //SECOND STEP
        view.backButtonStepTwo.setOnClickListener {
            view.stepTwo.visibility = View.GONE
            view.stepOne.visibility = View.VISIBLE
        }

        view.continueButtonStepTwo.setOnClickListener {
            view.stepTwo.visibility = View.GONE
            view.stepThree.visibility = View.VISIBLE
        }

        //THIRD STEP
        view.backButtonStepThree.setOnClickListener {
            view.stepThree.visibility = View.GONE
            view.stepTwo.visibility = View.VISIBLE
        }

        view.doneButtonStepThree.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    fun getWelcomeMembershipModal(context: Context?, layoutInflater: LayoutInflater, container: ViewGroup?, resources: Resources, membership: Membership) {
        val view: View =
            layoutInflater.inflate(R.layout.welcome_membership_modal, container, false)

        val alertDialog = getInformativeDialog(
            context,
            view
        ).show()


        val welcomeTitle = resources.getString(R.string.welcomeTitle, membership.name)
        val dishes = membership.dishesAmount()
        val welcomeDescription = resources.getString(R.string.welcomeDescription, dishes.toString(),
            membership.restaurants?.size.toString(), membership.visits.toString())

        Glide.with(view).load(membership.img).into(view.welcomeImage)
        view.welcomeTitle.text = welcomeTitle
        view.welcomeDescription.text = welcomeDescription

        view.welcomeDoneButton.setOnClickListener {
            alertDialog.dismiss()
        }

    }
}