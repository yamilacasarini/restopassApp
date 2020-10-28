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
import com.example.restopass.common.md5
import com.example.restopass.domain.CreditCard
import com.example.restopass.domain.Membership
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.about_restopass_modal.view.*
import kotlinx.android.synthetic.main.about_restopass_modal.view.stepOne
import kotlinx.android.synthetic.main.action_alert_dialog.view.*
import kotlinx.android.synthetic.main.delete_account_dialog.view.*
import kotlinx.android.synthetic.main.fragment_personal_info.*
import kotlinx.android.synthetic.main.welcome_membership_modal.view.*

open class AlertBody(
    val title: String? = null,
    val description: String? = null,
    val positiveActionText: Int? = null,
    val negativeActionText: Int? = null
)

class AlertCreditCard(resources: Resources, creditCard: CreditCard, membershipName: String) :
    AlertBody(
        resources.getString(R.string.chargeCreditCardTitle, membershipName),
        resources.getString(
            R.string.chargeCreditCardDescription,
            creditCard.type.replace("_", " "),
            creditCard.lastFourDigits
        ),
        R.string.aceptChargeCreditCard,
        R.string.cancelAlertMessage
    )

class AlertMembershipCancel(resources: Resources, membershipName: String, untilCancelDate: String) :
    AlertBody(
        resources.getString(R.string.cancel_membership, membershipName),
        resources.getString(R.string.cancel_membership_body, untilCancelDate),
        R.string.positive_cancel_membership
    )

object AlertDialog {
    fun getAlertDialog(
        context: Context,
        body: View,
        view: View? = null,
        withButton: Boolean = true
    ): MaterialAlertDialogBuilder {
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


    private fun getInformativeDialog(context: Context, body: View): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context!!)
            .setView(body)
    }

    fun getActionDialog(
        context: Context?,
        layoutInflater: LayoutInflater,
        container: ViewGroup,
        action: () -> Unit,
        alertBody: AlertBody,
        cancelAction: () -> Unit = {}
    ): MaterialAlertDialogBuilder {
        val body: View =
            layoutInflater.inflate(R.layout.action_alert_dialog, container, false)
        alertBody.run {
            title?.let { body.alertTitle.text = it }
            description?.let {
                body.alertDescription.text = Html.fromHtml(it)
                body.alertDescription.visibility = View.VISIBLE
            }
        }
        val dialog = MaterialAlertDialogBuilder(context!!)
            .setCustomTitle(body)

        if (alertBody.negativeActionText != null) {
            dialog.setNegativeButton(alertBody.negativeActionText)
            { _, _ ->
                cancelAction()
            }
        }
        dialog.setOnCancelListener {
               cancelAction()
        }
        dialog.setPositiveButton(alertBody.positiveActionText ?: R.string.deleteAlertMessage)
        { _, _ ->
            action()
        }

        return dialog
    }

    fun getActionDialogWithParams(
        context: Context?,
        layoutInflater: LayoutInflater,
        container: ViewGroup,
        action: (param: Any) -> Unit,
        param: Any,
        alertBody: AlertBody
    ): MaterialAlertDialogBuilder {
        val body: View =
            layoutInflater.inflate(R.layout.action_alert_dialog, container, false)
        alertBody.run {
            title?.let { body.alertTitle.text = it }
            description?.let {
                body.alertDescription.text = Html.fromHtml(it)
                body.alertDescription.visibility = View.VISIBLE
            }
        }


        val dialog = MaterialAlertDialogBuilder(context!!)
            .setCustomTitle(body)
        dialog.setNegativeButton(R.string.cancelAlertMessage)
        { _, _ ->
        }
        dialog.setPositiveButton(alertBody.positiveActionText ?: R.string.deleteAlertMessage)
        { _, _ ->
            action(param)
        }
        return dialog
    }

    fun getAboutRestoPassModal(
        context: Context?,
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) {
        val view: View =
            layoutInflater.inflate(R.layout.about_restopass_modal, container, false)

        val alertDialog = getInformativeDialog(
            context!!,
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

    fun getWelcomeMembershipModal(
        context: Context?,
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
        resources: Resources,
        membership: Membership
    ) {
        val view: View =
            layoutInflater.inflate(R.layout.welcome_membership_modal, container, false)

        val alertDialog = getInformativeDialog(
            context!!,
            view
        ).show()


        val welcomeTitle = resources.getString(R.string.welcomeTitle, membership.name)
        val dishes = membership.dishesAmount()
        val welcomeDescription = resources.getString(
            R.string.welcomeDescription, dishes.toString(),
            membership.restaurants?.size.toString(), membership.visits.toString()
        )

        Glide.with(view).load(membership.img).into(view.welcomeImage)
        view.welcomeTitle.text = welcomeTitle
        view.welcomeDescription.text = welcomeDescription

        view.welcomeDoneButton.setOnClickListener {
            alertDialog.dismiss()
        }

    }

    fun getDeleteAccountDialog(
        alertBody: AlertBody,
        action: (String) -> Unit,
        layoutInflater: LayoutInflater,
        personalInfoContainer: ViewGroup,
        context: Context?
    ): MaterialAlertDialogBuilder {
        val body: View =
            layoutInflater.inflate(R.layout.delete_account_dialog, personalInfoContainer, false)
        body.deleteAccountAlertTitle.text = alertBody.title
        body.deleteAccountAlertDescription.text = Html.fromHtml(alertBody.description)

        val dialog = MaterialAlertDialogBuilder(context!!)
            .setCustomTitle(body)

        dialog.setNegativeButton(alertBody.negativeActionText!!)
        { _, _ ->
        }

        dialog.setPositiveButton(alertBody.positiveActionText!!)
        { _ , _ ->
            action(body.deleteAccountInputText.text.toString().md5())
        }

        return dialog
    }
}