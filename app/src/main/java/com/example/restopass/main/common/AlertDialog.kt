package com.example.restopass.main.common

import android.content.Context
import android.view.View
import androidx.navigation.findNavController
import com.example.restopass.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object AlertDialog {
    fun getAlertDialog(context: Context?, titleView: View, view: View? = null, withButton: Boolean = true) : MaterialAlertDialogBuilder {
       val dialog = MaterialAlertDialogBuilder(context)
            .setCustomTitle(titleView)
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
}