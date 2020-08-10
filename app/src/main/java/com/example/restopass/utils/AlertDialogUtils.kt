package com.example.restopass.utils

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.restopass.R
import com.example.restopass.connection.ApiFatalException
import com.example.restopass.main.common.AlertBody
import com.example.restopass.main.common.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.reload_error_alert_dialog.view.*
import java.lang.Exception

object AlertDialogUtils {

    fun buildAlertDialog(error : Exception?, layoutInflater : LayoutInflater, container: ViewGroup?, view: View? = null, alertBody: AlertBody? = null): MaterialAlertDialogBuilder {
        if(error == null) {
            return buildMessageAlertDialog(layoutInflater,container, view, alertBody?.title, alertBody?.description);
        }

        return if(error is ApiFatalException) {
            buildEmptyAlertDialog(layoutInflater,container, view)
        } else {
            buildMessageAlertDialog(layoutInflater,container,view, alertBody?.title ?: error.message!!, alertBody?.description)
        }
    }

    private fun buildMessageAlertDialog(layoutInflater : LayoutInflater, container: ViewGroup?, view: View?, title : String?, description: String? = null): MaterialAlertDialogBuilder {
        val titleView: View =
            layoutInflater.inflate(R.layout.reload_error_alert_dialog, container, false)
        title?.let {
            titleView.errorTitle.text = it
            titleView.errorTitle.visibility = View.VISIBLE
        }
        description?.let {
            titleView.errorDescription.text = Html.fromHtml(it)
            titleView.errorDescription.visibility = View.VISIBLE
        }
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