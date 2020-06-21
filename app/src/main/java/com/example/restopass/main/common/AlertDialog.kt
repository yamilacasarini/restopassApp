package com.example.restopass.main.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.restopass.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.about_restopass_modal.view.*
import kotlinx.android.synthetic.main.activity_main.*

object AlertDialog {
    fun getAlertDialog(context: Context?, body: View, view: View? = null) : MaterialAlertDialogBuilder {
       return MaterialAlertDialogBuilder(context)
            .setCustomTitle(body)
            .setOnCancelListener {
                view?.findNavController()?.popBackStack()
            }
            .setPositiveButton(R.string.accept)
            { _, _ ->
                view?.findNavController()?.popBackStack()
            }
    }

    fun getInformativeDialog(context: Context?, body: View) : MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context)
            .setView(body)
    }

    fun getAboutRestoPassModal(context: Context?, layoutInflater: LayoutInflater, container: ViewGroup?) {
        val titleView: View =
            layoutInflater.inflate(R.layout.about_restopass_modal, container, false)

        val alertDialog = getInformativeDialog(
            context,
            titleView
        ).show()

        // FIRST STEP
        titleView.skipButton.setOnClickListener {
            alertDialog.dismiss()
        }

        titleView.continueButton.setOnClickListener {
            titleView.stepOne.visibility = View.GONE
            titleView.stepTwo.visibility = View.VISIBLE
        }

        //SECOND STEP
        titleView.backButtonStepTwo.setOnClickListener {
            titleView.stepTwo.visibility = View.GONE
            titleView.stepOne.visibility = View.VISIBLE
        }

        titleView.continueButtonStepTwo.setOnClickListener {
            titleView.stepTwo.visibility = View.GONE
            titleView.stepThree.visibility = View.VISIBLE
        }

        //THIRD STEP
        titleView.backButtonStepThree.setOnClickListener {
            titleView.stepThree.visibility = View.GONE
            titleView.stepTwo.visibility = View.VISIBLE
        }

        titleView.doneButtonStepThree.setOnClickListener {
            alertDialog.dismiss()
        }
    }
}