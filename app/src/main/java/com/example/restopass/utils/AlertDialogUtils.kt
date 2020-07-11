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
import java.lang.RuntimeException

object AlertDialogUtils {

    fun buildAlertDialog(error : Exception?, layoutInflater : LayoutInflater, container: ViewGroup?, msg : String? = null): MaterialAlertDialogBuilder {
        if(error == null) {
            return buildMessageAlertDialog(layoutInflater,container, msg!!);
        }

        return if(error is ApiFatalException) {
            buildEmptyAlertDialog(layoutInflater,container)
        } else {
            buildMessageAlertDialog(layoutInflater,container,msg ?: error.message!!)
        }
    }

    private fun buildMessageAlertDialog(layoutInflater : LayoutInflater, container: ViewGroup?, msg : String): MaterialAlertDialogBuilder {
        val titleView: View =
            layoutInflater.inflate(R.layout.invitation_error, container, false)
        titleView.invitationErrorTitle.text = msg
        return AlertDialog.getAlertDialog(
            titleView.context,
            titleView
        )
    }

    private fun buildEmptyAlertDialog(layoutInflater : LayoutInflater, container: ViewGroup?): MaterialAlertDialogBuilder {
        val titleView: View =
            layoutInflater.inflate(R.layout.alert_dialog_title, container, false)
        return AlertDialog.getAlertDialog(
            titleView.context, titleView
        )
    }
}