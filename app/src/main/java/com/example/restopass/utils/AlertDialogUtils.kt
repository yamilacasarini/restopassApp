package com.example.restopass.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.restopass.R
import com.example.restopass.connection.ApiFatalException
import com.example.restopass.main.common.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.invitation_error.view.*
import java.lang.Exception

object AlertDialogUtils {

    fun buildAlertDialog(error : Exception?, layoutInflater : LayoutInflater, container: ViewGroup?, view: View? = null, msg : String? = null): MaterialAlertDialogBuilder {
        if(error == null) {
            return buildMessageAlertDialog(layoutInflater,container, view, msg!!);
        }

        return if(error is ApiFatalException) {
            buildEmptyAlertDialog(layoutInflater,container, view)
        } else {
            buildMessageAlertDialog(layoutInflater,container,view, msg ?: error.message!!)
        }
    }

    private fun buildMessageAlertDialog(layoutInflater : LayoutInflater, container: ViewGroup?, view: View?, msg : String): MaterialAlertDialogBuilder {
        val titleView: View =
            layoutInflater.inflate(R.layout.invitation_error, container, false)
        titleView.invitationErrorTitle.text = msg
        return AlertDialog.getAlertDialog(
            titleView.context, titleView, view
        )
    }

    private fun buildEmptyAlertDialog(layoutInflater : LayoutInflater, container: ViewGroup?, view: View?): MaterialAlertDialogBuilder {
        val titleView: View =
            layoutInflater.inflate(R.layout.error_alert_dialog, container, false)
        return AlertDialog.getAlertDialog(
            titleView.context, titleView, view
        )
    }
}